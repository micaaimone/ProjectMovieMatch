package com.example.demo.model.services.payments.mp;

import com.example.demo.model.entities.subs.SuscripcionEntity;
import com.example.demo.model.services.payments.IPayment;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;

import java.util.Map;

public class MpPayment implements IPayment {

    private final MpPreference preference;
    private final MpProcessor processor;
    private final MpWebhook webhook;

    public MpPayment(MpPreference preference, MpProcessor processor, MpWebhook webhook) {
        this.preference = preference;
        this.processor = processor;
        this.webhook = webhook;
    }

    @Override
    public String createPayment(SuscripcionEntity sub) throws MPException, MPApiException {
        return preference.crearPreferencia(sub);
    }

    @Override
    public void handleWebhook(Map<String, Object> body) {
        webhook.recibirPago(body);
    }

    @Override
    public void processPayment(Long Id) {

    }

    @Override
    public String getName() {
        return "mercadoPago";
    }
}
