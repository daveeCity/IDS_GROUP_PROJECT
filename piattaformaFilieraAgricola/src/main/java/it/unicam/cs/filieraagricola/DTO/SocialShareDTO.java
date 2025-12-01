package it.unicam.cs.filieraagricola.DTO;

import lombok.Data;

@Data
public class SocialShareDTO {
    private String titolo;
    private String descrizione;
    private String shareUrl;
    private String imageUrl;
    private String testoMessaggio;

    //Costruttore vuoto
    public SocialShareDTO() {}

    //Costruttore completo
    public SocialShareDTO(String titolo, String descrizione, String shareUrl, String testoMessaggio) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.shareUrl = shareUrl;
        this.testoMessaggio = testoMessaggio;
    }
}