package com.example.demo.model.services.payments.mp;

import com.example.demo.model.entities.subs.SuscripcionEntity;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MpPreference {
    @Value("${MERCADOPAGO_ACCESS_TOKEN}")
    private String MERCADOPAGO_ACCESS_TOKEN;
    //url publica para solicitar preferencia
    private String ngrok = "";

    public String crearPreferencia(SuscripcionEntity sub) throws MPException, MPApiException {
        MercadoPagoConfig.setAccessToken(MERCADOPAGO_ACCESS_TOKEN);

        PreferenceClient preferenceClient = new PreferenceClient();

        //el item es como la estructura del pago(descripcion, cantidad, precio)
        PreferenceItemRequest itemRequest =
                PreferenceItemRequest.builder()
                        .id(UUID.randomUUID().toString())
                        .title("Suscripcion :" + sub.getPlan().getTipo().name().toLowerCase())
                        .description("Gracias por suscribirse a Movie-Match")
                        .quantity(1)
                        .unitPrice(sub.getMonto())
                        .currencyId("ARS")
                        .build();

        //creamos la backURL y agregamos las estructuras hechas al request
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .backUrls(backUrl(sub))
                .payer(payerRequest(sub))
                .items(List.of(itemRequest))
                .notificationUrl(ngrok + "/mp/notification")
                .externalReference(String.valueOf(sub.getId_suscripcion()))
                .metadata(metadata(sub))
                .build();

        Preference preference = preferenceClient.create(preferenceRequest);
        return preference.getInitPoint();
    }

    private PreferencePayerRequest payerRequest(SuscripcionEntity sub) {
        //al final la info del payer sirve poco, ya que se modifica segun el origen del pago
        //por eso usamos metadata para guardar datos
        return PreferencePayerRequest.builder()
                        .email(sub.getUsuario().getEmail())
                        .name(sub.getUsuario().getNombre())
                        .build();
    }

    private Map<String, Object> metadata(SuscripcionEntity sub) {
        return Map.of(
                "email", sub.getUsuario().getEmail(),
                "nombre", sub.getUsuario().getNombre()
        );
    }

    private PreferenceBackUrlsRequest backUrl(SuscripcionEntity sub) {
        return PreferenceBackUrlsRequest.builder()
                .success(ngrok + "/success")
                .failure(ngrok + "/failure")
                .pending(ngrok + "/pending")
                .build();
    }

}
