package com.example.demo.model.services.Subs;

import com.example.demo.model.entities.subs.SuscripcionEntity;
import com.example.demo.model.services.Email.EmailService;
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
    private final EmailService emailService;


    public MPService(RestTemplate restTemplate, SuscripcionService suscripcionService, PagoService pagoService, EmailService emailService) {
        this.restTemplate = restTemplate;
        this.suscripcionService = suscripcionService;
        this.pagoService = pagoService;
        this.emailService = emailService;
    }


    public String crearPreferencia(SuscripcionEntity sub) throws MPException, MPApiException {

        //RECORDAR CAMBIAR LA URL EN NOTIFICACIONES EN LA WEB DE MP
        String ngrokUrl = "https://685f-2803-9800-9995-6e65-3deb-e0c5-4ffe-b90b.ngrok-free.app";
        //el monto en la api de mp es bigDecimal
        BigDecimal pf = BigDecimal.valueOf(sub.getMonto());

        MercadoPagoConfig.setAccessToken(MERCADOPAGO_ACCESS_TOKEN);
        PreferenceClient preferenceClient = new PreferenceClient();

        //el item es como la estructura del pago(descripcion, cantidad, precio)
        PreferenceItemRequest itemRequest =
                PreferenceItemRequest.builder()
                        .id(UUID.randomUUID().toString())
                        .title("Suscripcion :" + sub.getPlan().getTipo().name().toLowerCase())
                        .description("Gracias por suscribirse a Movie-Match")
                        .quantity(1)
                        .unitPrice(pf)
                        .currencyId("ARS")
                        .build();
        List<PreferenceItemRequest> items = new ArrayList<>();
        items.add(itemRequest);

        //al final la info del payer sirve poco, ya que se modifica segun el origen del pago
        //por eso usamos metadata para guardar datos

        //aca guardamos informacion del comprador para enviarle mail
        PreferencePayerRequest payer =
                PreferencePayerRequest.builder()
                        .email(sub.getUsuario().getEmail())
                        .name(sub.getUsuario().getNombre())
                        .build();

        //creamos metadata para poder conseguir los datos del user seteados en el usuario
        //mas que nada porque es una cuenta de prueba y los datos de payer se basan en la cuenta tester
        Map<String, Object> metadata = Map.of(
                "nombre", sub.getUsuario().getNombre(),
                "email", sub.getUsuario().getEmail()
        );

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
                .metadata(metadata)
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
            throw new RuntimeException(e.getMessage());
        }
    }

    //actuvamos la sub, creamos el registro de pago y enviamos mail de configuracion
    public void procesarPago(Long id){
        try{
            //MP puede enviar varias notificaciones (200, 201, 202) y crear registros duplicados
            //por eso verificamos q no exista el pago segun la id original de mp
            if(pagoService.existsByIdMP(id)){
                return;
            }
            PaymentClient paymentClient = new PaymentClient();
            Payment payment = paymentClient.get(id);

            //datos del comprador para enviar mail
            String mail = payment.getMetadata().get("email").toString();
            String nombre = payment.getMetadata().get("nombre").toString();

            //si el status de pago es aprobado activamos la sub y guardamos la info del pago
            if("approved".equals(payment.getStatus())){

                BigDecimal monto = payment.getTransactionAmount();
                Long idSub = Long.valueOf(payment.getExternalReference());

                suscripcionService.activarSuscripion(idSub);

                SuscripcionEntity sub = suscripcionService.findByIdEntity(idSub);
                pagoService.anadirPago(sub, monto, id);


                enviarMail(mail, nombre);

            }
        } catch (MPException e) {
            throw new RuntimeException(e);
        } catch (MPApiException e) {
            throw new RuntimeException(e);
        }

    }

    public void enviarMail(String mail, String nombre){
        String asunto= "Pago exitoso";
        String msj = "Hola "+ nombre+ " Muchas gracias por su suscripcion a Movie-Match, nos aseguraremos que reciba el mejor trato posible!";

        emailService.sendEmail(mail, asunto, msj);
    }


}
