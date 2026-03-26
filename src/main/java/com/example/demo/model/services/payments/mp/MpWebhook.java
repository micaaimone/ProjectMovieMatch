package com.example.demo.model.services.payments.mp;

import com.example.demo.model.services.Subs.PagoService;

import java.util.Map;

public class MpWebhook {
    private final MpProcessor mpProcessor;

    public MpWebhook(MpProcessor mpProcessor) {
        this.mpProcessor = mpProcessor;
    }

    public void recibirPago(Map<String, Object> body){
        try {
            if ("payment".equals(body.get("type"))) {
                Map<String, Object> data = (Map<String, Object>) body.get("data");
                if (data != null && data.get("id") != null) {
                    Long paymentId = Long.valueOf(data.get("id").toString());
                    mpProcessor.procesarPago(paymentId);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
