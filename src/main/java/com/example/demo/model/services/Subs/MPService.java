package com.example.demo.model.services.Subs;

import com.example.demo.model.entities.subs.SuscripcionEntity;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

@Service
public class MPService {
    private RestTemplate restTemplate;
    @Value("${MERCADOPAGO_ACCESS_TOKEN}")
    private String MERCADOPAGO_ACCESS_TOKEN;
    private final SuscripcionService suscripcionService;
    private final PagoService pagoService;



    public MPService(RestTemplate restTemplate, SuscripcionService suscripcionService, PagoService pagoService) {
        this.restTemplate = restTemplate;
        this.suscripcionService = suscripcionService;
        this.pagoService = pagoService;
    }


    public String crearPreferencia(SuscripcionEntity sub) throws MPException, MPApiException {
        String ngrokUrl = "https://52bb-2803-9800-9995-6e65-70d1-1a7f-3fb8-a29a.ngrok-free.app";
        BigDecimal pf = BigDecimal.valueOf(sub.getMonto());

                MercadoPagoConfig.setAccessToken(MERCADOPAGO_ACCESS_TOKEN);
                PreferenceClient preferenceClient = new PreferenceClient();

                PreferenceItemRequest itemRequest =
                        PreferenceItemRequest.builder()
                                .id(UUID.randomUUID().toString())
                                .title(sub.getPlan().getTipo().name())
                                .quantity(1)
                                .unitPrice(pf)
                                .currencyId("ARS")
                                .build();
                List<PreferenceItemRequest> items = new ArrayList<>();
                items.add(itemRequest);

                PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                        .backUrls(
                                PreferenceBackUrlsRequest.builder()
                                        .success(ngrokUrl + "/mp/success")
                                        .failure(ngrokUrl + "/mp/failure")
                                        .pending(ngrokUrl + "/mp/pending")
                                        .build())
                        .items(items)
                        .notificationUrl(ngrokUrl + "/mp/notification")
                        .externalReference(String.valueOf(sub.getId_suscripcion()))
                        .build();

                Preference preference = preferenceClient.create(preferenceRequest);

                return preference.getInitPoint();

    }

    public void procesarPago(Long id){
        try{
            PaymentClient paymentClient = new PaymentClient();
            Payment payment = paymentClient.get(id);

            //si el status de pago es aprobado activamos la sub y guardamos la info del pago
            if("approved".equals(payment.getStatus())){
                BigDecimal monto = payment.getTransactionAmount();
                Long idSub = Long.valueOf(payment.getExternalReference());
                suscripcionService.activarSuscripion(idSub);
                SuscripcionEntity sub = suscripcionService.findByIdEntity(idSub);

                pagoService.anadirPago(sub, monto);

                /*posible idea para hacerlo mas especifico
                switch (payment.getStatus()) {
                case "approved":
                    break;
                case "pending":
                    break;
                case "rejected":
                    break;  */
            }
        } catch (MPException e) {
            throw new RuntimeException(e);
        } catch (MPApiException e) {
            throw new RuntimeException(e);
        }

    }

}
