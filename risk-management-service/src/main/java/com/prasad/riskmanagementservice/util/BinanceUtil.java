package com.prasad.riskmanagementservice.util;

import org.springframework.context.annotation.Configuration;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class BinanceUtil {

    public String getHmacSHA256Signature(String data, String key) throws Exception {
        Mac sha256HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        sha256HMAC.init(secretKeySpec);
        return bytesToHex(sha256HMAC.doFinal(data.getBytes()));
    }

    public String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
