package com.prasad.ordermanagementservice.service;

import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

@Service
@AllArgsConstructor
public class BinanceApiService {

    private final String apiKey = "1aa5a75c298325749d6e873d3328da0c65d799029688a556a0ca7cad86086688";
    private final String secretKey = "c1687138c6aad7b96ae3959db414e017c4287ad9fc29f42aef40ec057a410a0f";
    private final String baseUrl = "https://testnet.binancefuture.com";
    private final Logger log = LoggerFactory.getLogger(BinanceApiService.class);

    public String placeOrder(String symbol, String side, String type, String quantity, String price) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        String endpoint = "/fapi/v1/order";
        String url = baseUrl + endpoint;

        Map<String, String> params = new TreeMap<>();
        params.put("symbol", symbol);
        params.put("side", side);
        params.put("type", type);
        params.put("quantity", quantity);
        params.put("price", price);
        params.put("timeInForce", "GTC");
        params.put("recvWindow", "5000");
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));

        String queryString = getQueryString(params);
        String signature = getHmacSHA256Signature(queryString, secretKey);

        queryString += "&signature=" + signature;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-MBX-APIKEY", apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url + "?" + queryString, HttpMethod.POST, entity, String.class);

        log.info("Order placed successfully : {}", response.getBody());
        return response.getBody();
    }

    public Map<String, String> getOpenOrders(String symbol) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String endpoint = "/fapi/v1/openOrders";
        String url = baseUrl + endpoint;

        Map<String, String> params = new TreeMap<>();
        params.put("symbol", symbol);
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));

        String queryString = getQueryString(params);
        String signature = getHmacSHA256Signature(queryString, secretKey);

        queryString += "&signature=" + signature;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-MBX-APIKEY", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url + "?" + queryString, HttpMethod.GET, entity, String.class);

        Map<String, String> outputMap = new TreeMap<>();
        if (!Objects.equals(response.getBody(), "[]")){
            log.info("Current Open Order : {}", response.getBody());
            JSONArray jsonArray = new JSONArray(response.getBody());
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String status = jsonObject.getString("status");
            String side = jsonObject.getString("side");
            outputMap.put("status", status);
            outputMap.put("side", side);
            return outputMap;
        } else {
            outputMap.put("status", "null");
            outputMap.put("side", "null");
            return outputMap;
        }
    }

    public void cancelOrder(String symbol) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String endpoint = "/fapi/v1/allOpenOrders";
        String url = baseUrl + endpoint;

        Map<String, String> params = new TreeMap<>();
        params.put("symbol", symbol);
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));

        String queryString = getQueryString(params);
        String signature = getHmacSHA256Signature(queryString, secretKey);
        queryString += "&signature=" + signature;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-MBX-APIKEY", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url + "?" + queryString, HttpMethod.DELETE, entity, String.class);

        log.info("Order cancelled successfully : {}", response.getBody());
        response.getBody();
    }


    private String getQueryString(Map<String, String> params) {
        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            queryString.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return queryString.substring(0, queryString.length() - 1);
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
