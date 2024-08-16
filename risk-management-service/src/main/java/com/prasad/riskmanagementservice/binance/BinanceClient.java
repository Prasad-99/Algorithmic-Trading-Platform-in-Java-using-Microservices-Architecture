package com.prasad.riskmanagementservice.binance;

import com.prasad.riskmanagementservice.util.BinanceUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Service
@AllArgsConstructor
public class BinanceClient {

    private static final String API_KEY = "1aa5a75c298325749d6e873d3328da0c65d799029688a556a0ca7cad86086688";
    private static final String SECRET_KEY = "c1687138c6aad7b96ae3959db414e017c4287ad9fc29f42aef40ec057a410a0f";
    private static final String BASE_URL = "https://testnet.binance.vision";

    @Scheduled(fixedRate = 5000)
    public void fetchUnrealizedProfit() throws Exception {
        String endpoint = "/fapi/v3/account";
        long timestamp = System.currentTimeMillis();
        String queryString = "timestamp=" + timestamp;
        String signature = getHmacSHA256Signature(queryString, SECRET_KEY);;

        String url = BASE_URL + endpoint + "?" + queryString + "&signature=" + signature;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-MBX-APIKEY", API_KEY);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, String.class);

        System.out.println(response);
//        Map<String, Object> accountInfo = response.getBody();

//        if (accountInfo != null) {
//            double totalUnrealizedProfit = 0.0;
//            for (Map<String, Object> position : (Iterable<Map<String, Object>>) accountInfo.get("positions")) {
//                double unrealizedProfit = Double.parseDouble((String) position.get("unrealizedProfit"));
//                totalUnrealizedProfit += unrealizedProfit;
//            }
//            System.out.println("Total Unrealized Profit: " + totalUnrealizedProfit);
//        }
    }

    private String getHmacSHA256Signature(String data, String key) throws Exception {
        Mac sha256HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        sha256HMAC.init(secretKeySpec);
        return bytesToHex(sha256HMAC.doFinal(data.getBytes()));
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
