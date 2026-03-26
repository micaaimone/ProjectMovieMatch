package com.example.demo.model.services.payments;

import com.example.demo.model.entities.subs.SuscripcionEntity;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;

import java.util.Map;

public interface IPayment {
    String createPayment(SuscripcionEntity sub) throws MPException, MPApiException;

    void handleWebhook(Map<String, Object> body);

    void processPayment(Long Id);

    String getName();

}
