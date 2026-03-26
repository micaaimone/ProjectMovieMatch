package com.example.demo.model.services.payments.mp;

import com.example.demo.model.entities.subs.SuscripcionEntity;
import com.example.demo.model.services.Subs.PagoService;
import com.example.demo.model.services.Subs.SuscripcionService;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;

import java.math.BigDecimal;

public class MpProcessor {
    private final PagoService pagoService;
    private final SuscripcionService suscripcionService;

    public MpProcessor(PagoService pagoService, SuscripcionService suscripcionService) {
        this.pagoService = pagoService;
        this.suscripcionService = suscripcionService;
    }

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


                //enviarMail(mail, nombre);

            }
        } catch (MPException e) {
            throw new RuntimeException(e);
        } catch (MPApiException e) {
            throw new RuntimeException(e);
        }
    }
}
