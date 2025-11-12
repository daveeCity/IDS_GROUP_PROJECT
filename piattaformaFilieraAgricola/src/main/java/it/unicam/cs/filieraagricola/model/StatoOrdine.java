package it.unicam.cs.filieraagricola.model;


public enum StatoOrdine {
    PENDENTE,   // Ordine creato, in attesa di pagamento
    CONFERMATO, // Pagamento ricevuto
    SPEDITO,
    CONSEGNATO,
    ANNULLATO
}