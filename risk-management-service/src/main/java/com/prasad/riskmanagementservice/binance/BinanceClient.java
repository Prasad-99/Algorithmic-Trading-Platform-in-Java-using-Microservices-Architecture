package com.prasad.riskmanagementservice.binance;

import com.prasad.riskmanagementservice.entity.AccountMetrics;
import com.prasad.riskmanagementservice.repository.AccountMetricRepository;
import com.prasad.riskmanagementservice.util.BinanceUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BinanceClient {

    private static final String API_KEY = "1aa5a75c298325749d6e873d3328da0c65d799029688a556a0ca7cad86086688";
    private static final String SECRET_KEY = "c1687138c6aad7b96ae3959db414e017c4287ad9fc29f42aef40ec057a410a0f";
    private static final String BASE_URL = "https://testnet.binancefuture.com";
    private BinanceUtil binanceUtil;
    private AccountMetricRepository accountMetricRepository;

    @Scheduled(fixedRate = 1000)
    public void fetchRiskParameters() throws Exception {
        List<String> positionRisk = fetchPositionRisk();
        String accountsInfo = fetchAccountsInfo();
        AccountMetrics accountMetrics = new AccountMetrics();
        accountMetrics.setSymbol(positionRisk.get(0));
        accountMetrics.setTimestamp(LocalDateTime.now());
        accountMetrics.setBalance(Double.parseDouble(accountsInfo));
        accountMetrics.setPositionSize(Double.parseDouble(positionRisk.get(1)));
        accountMetrics.setUnrealizedProfitLoss(Double.parseDouble(positionRisk.get(2)));
        accountMetricRepository.deleteAll();
        accountMetricRepository.save(accountMetrics);
    }


    private List<String> fetchPositionRisk() throws Exception {
        String endpoint = "/fapi/v2/positionRisk";
        long timestamp = System.currentTimeMillis();
        String queryString ="symbol=" + "BTCUSDT" + "&" + "timestamp=" + timestamp;
        String signature = binanceUtil.getHmacSHA256Signature(queryString, SECRET_KEY);

        String url = BASE_URL + endpoint + "?" + queryString + "&signature=" + signature;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-MBX-APIKEY", API_KEY);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, String.class);
        JSONArray jsonArray = new JSONArray(response.getBody());
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        String symbol = jsonObject.getString("symbol");
        String positionAmt = jsonObject.getString("positionAmt");
        String unrealizedProfit = jsonObject.getString("unRealizedProfit");

        List<String> params = new ArrayList<>();
        params.add(symbol);
        params.add(positionAmt);
        params.add(unrealizedProfit);

        return params;
    }

    private String fetchAccountsInfo() throws Exception {
        String endpoint = "/fapi/v3/account";
        long timestamp = System.currentTimeMillis();
        String queryString = "timestamp=" + timestamp;
        String signature = binanceUtil.getHmacSHA256Signature(queryString, SECRET_KEY);
        String url = BASE_URL + endpoint + "?" + queryString + "&signature=" + signature;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-MBX-APIKEY", API_KEY);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, String.class);
        JSONArray jsonArray = new JSONArray("["+response.getBody()+"]");
        JSONObject jsonObject = jsonArray.getJSONObject(0);

        return jsonObject.getString("availableBalance");
    }
}
