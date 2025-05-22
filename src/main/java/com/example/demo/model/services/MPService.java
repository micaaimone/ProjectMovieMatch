package com.example.demo.model.services;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.MercadoPagoClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MPService {
    private RestTemplate restTemplate;
    @Value("${MERCADOPAGO_ACCESS_TOKEN}")
    private String MERCADOPAGO_ACCESS_TOKEN;
    private static final String URL = "https://api.mercadopago.com/checkout/preferences";

    public MPService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }



    public String crearPreferencia(String nombrePreferencia, float precioPreferencia) {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(MERCADOPAGO_ACCESS_TOKEN);



            Map<String, Object> item = new HashMap<>();
            item.put("title", nombrePreferencia);
            item.put("quantity", 1);
            item.put("unit_price", precioPreferencia);

            Map<String, String> backUrls = new HashMap<>();
            backUrls.put("succeless", "httpbin.org/get?back_url=success");
            backUrls.put("failure", "httpbin.org/get?back_url=failure");
            backUrls.put("pending", "httpbin.org/get?back_url=pending");

            Map<String, Object> body = new HashMap<>();
            body.put("items", List.of(item));
            body.put("back_urls", backUrls);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(URL, request, Map.class);

            Map<String, Object> responseBody = response.getBody();
        return responseBody != null ? responseBody.get("init_point").toString() : "";

    }

}
