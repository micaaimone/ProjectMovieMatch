package com.example.demo.model.services.payments;

import com.example.demo.model.entities.subs.SuscripcionEntity;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {
    private final Map<String, IPayment> providers = new HashMap<>();

    public PaymentService(List<IPayment> providerList) {
        providerList.forEach(p ->
                providers.put(p.getName(), p)
        );
    }

    public String crearPago(String provider, SuscripcionEntity sub) throws MPException, MPApiException {
        return getProvider(provider).createPayment(sub);
    }

    public IPayment getProvider(String name) {
        if (!providers.containsKey(name)) {
            throw new IllegalArgumentException("Proveedor desconocido: " + name);
        }
        return providers.get(name);
    }
}
