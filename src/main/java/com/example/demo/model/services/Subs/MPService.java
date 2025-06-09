package com.example.demo.model.services.Subs;

import com.example.demo.model.entities.subs.SuscripcionEntity;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.*;
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
        String ngrokUrl = "https://859e-2803-9800-9995-6e65-c4b9-3187-984a-ed8f.ngrok-free.app";
        //el monto en la api de mp es bigDecimal
        BigDecimal pf = BigDecimal.valueOf(sub.getMonto());

        MercadoPagoConfig.setAccessToken(MERCADOPAGO_ACCESS_TOKEN);
        PreferenceClient preferenceClient = new PreferenceClient();

        //el item es como la estructura del pago(descripcion, cantidad, precio)
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

        //aca guardamos informacion del comprador para enviarle mail
        PreferencePayerRequest payer =
                PreferencePayerRequest.builder()
                        //.email(sub.getUsuario().getEmail())
                        .name(sub.getUsuario().getNombre())
                        .build();

        //creamos la backURL y agregamos las estructuras hechas al request
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .backUrls(
                        PreferenceBackUrlsRequest.builder()
                                .success(ngrokUrl + "/mp/success")
                                .failure(ngrokUrl + "/mp/failure")
                                .pending(ngrokUrl + "/mp/pending")
                                .build())
                .payer(payer)
                .items(items)
                .notificationUrl(ngrokUrl + "/mp/notification")
                .externalReference(String.valueOf(sub.getId_suscripcion()))
                .build();

        Preference preference = preferenceClient.create(preferenceRequest);

        return preference.getInitPoint();

    }

    //recibimos el body de mp al realizar el pago
    public void recibirPago( Map<String, Object> body){
        try {
            if ("payment".equals(body.get("type"))) {
                Map<String, Object> data = (Map<String, Object>) body.get("data");
                if (data != null && data.get("id") != null) {
                    Long paymentId = Long.valueOf(data.get("id").toString());
                    procesarPago(paymentId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void procesarPago(Long id){
        try{
            PaymentClient paymentClient = new PaymentClient();
            Payment payment = paymentClient.get(id);

            //datos del comprador para enviar mail
            String mail = payment.getPayer().getEmail();
            String nombre = payment.getPayer().getFirstName();

            //si el status de pago es aprobado activamos la sub y guardamos la info del pago
            if("approved".equals(payment.getStatus())){
                BigDecimal monto = payment.getTransactionAmount();
                Long idSub = Long.valueOf(payment.getExternalReference());
                suscripcionService.activarSuscripion(idSub);
                SuscripcionEntity sub = suscripcionService.findByIdEntity(idSub);
                //agregar metodo de mail
                pagoService.anadirPago(sub, monto);

            }
        } catch (MPException e) {
            throw new RuntimeException(e);
        } catch (MPApiException e) {
            throw new RuntimeException(e);
        }

    }

}
