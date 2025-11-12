package it.unicam.cs.filieraagricola.DTO;

import lombok.Data;

@Data
public class CheckoutRequestDTO {
    // Es. "CARTA_DI_CREDITO", "PAYPAL"
    private String tipoPagamento;
}
