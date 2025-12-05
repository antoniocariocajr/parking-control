package com.bill.parking_control.services;

import com.bill.parking_control.dtos.payment.PaymentResponseDTO;
import com.bill.parking_control.persistences.entities.Payment.PaymentMethod;

public interface CheckoutService {
    PaymentResponseDTO checkout(String sessionId, PaymentMethod paymentMethod);

}
