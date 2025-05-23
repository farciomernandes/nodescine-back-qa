package com.cine.sk.cinesk.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ApiUtil {

    /**
     * The method below creates a header for authentication
     * for oAuth1.0a to be used in the request
     *
     * @param method         POST, PUT
     * @param baseUrl        url without query params
     * @param consumerKey    consumer key
     * @param consumerSecret consumer secret
     */
    public static String generateAuth1Header(String method, String baseUrl, String consumerKey, String consumerSecret) {
        String httpMethod = method.toUpperCase();
        String nonce = generateNonce();
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);

        Map<String, String> parameters = new HashMap<>();

        parameters.put("oauth_consumer_key", consumerKey);
        parameters.put("oauth_nonce", nonce);
        parameters.put("oauth_signature_method", "HMAC-SHA1");
        parameters.put("oauth_timestamp", timestamp);
        parameters.put("oauth_version", "1.0");

        parameters = organizeParameters(parameters);

        String signature = null;
        try {
            signature = generateOAuth1Signature(httpMethod, baseUrl, consumerSecret, parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Map<String, String> headerParameters = new HashMap<>();
        headerParameters.put("oauth_consumer_key", consumerKey);
        headerParameters.put("oauth_nonce", nonce);
        headerParameters.put("oauth_signature", encode(signature));

        headerParameters.put("oauth_signature_method", "HMAC-SHA1");
        headerParameters.put("oauth_timestamp", timestamp);
        headerParameters.put("oauth_version", "1.0");

        String header = generateOAuth1Header(headerParameters);
        return header;
    }

    private static Map<String, String> organizeParameters(Map<String, String> parameters) {
        List<Map.Entry<String, String>> entryList = new ArrayList<>(parameters.entrySet());
        entryList.sort(Map.Entry.comparingByKey());

        LinkedHashMap<String, String> sortedParameters = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : entryList) {
            sortedParameters.put(entry.getKey(), entry.getValue());
        }

        return sortedParameters;
    }

    /**
     * The method below generates a header for oAuth1.0a
     *
     * @param parameters
     * @return String
     */
    private static String generateOAuth1Header(Map<String, String> parameters) {
        List<Map.Entry<String, String>> sortedParams = new ArrayList<>(parameters.entrySet());
        sortedParams.sort(Map.Entry.comparingByKey());

        StringBuilder header = new StringBuilder("OAuth ");
        for (Map.Entry<String, String> entry : sortedParams) {
            header.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\", ");
        }

        if (header.length() > 7) {
            header.setLength(header.length() - 2);
        }
        return header.toString();
    }

    /**
     * The method below generates a nonce
     *
     * @return String
     */
    private static String generateNonce() {
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder nonce = new StringBuilder();
        Random rand = new Random();

        for (int i = 0; i < 32; i++) {
            nonce.append(characters.charAt(rand.nextInt(characters.length())));
        }

        return nonce.toString();
    }

    /**
     * The method below generates the signature for oAuth1.0a
     *
     * @param method
     * @param baseUrl
     * @param consumerSecret
     * @param parameters
     * @return String
     * @throws Exception
     */
    private static String generateOAuth1Signature(String method, String baseUrl, String consumerSecret, Map<String, String> parameters) throws Exception {
        SortedSet<String> keys = new TreeSet<>(parameters.keySet());
        StringJoiner joiner = new StringJoiner("&");
        for (String key : keys) {
            joiner.add(encode(key) + "=" + encode(parameters.get(key)));
        }

        String finalParameters = joiner.toString();
        StringJoiner finalJoin = new StringJoiner("&");
        finalJoin.add(method.toUpperCase());
        finalJoin.add(encode(baseUrl));
        finalJoin.add(encode(finalParameters));

        StringJoiner signingKeyJoiner = new StringJoiner("&");
        signingKeyJoiner.add(consumerSecret + "&");

        final String signingKeyStr = signingKeyJoiner.toString();
        byte[] keyBytes = (signingKeyStr).getBytes(StandardCharsets.UTF_8);
        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA1");

        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(key);

        String result = new String(Base64.getEncoder().encode(mac.doFinal(finalJoin.toString().getBytes(
                StandardCharsets.UTF_8))), StandardCharsets.UTF_8).trim();

        return result;
    }

    /**
     * The method below encodes a string
     *
     * @param value
     * @return String
     */
    public static String encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to encode", e);
        }
    }
}
