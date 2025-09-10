package main.java.it.unicam.cs.filieraagricola.factory;

import main.java.it.unicam.cs.filieraagricola.model.*;

public class UserFactory {
    
    public static User createUser(String tipo, Long id, String name, String... parametriExtra) {
        switch (tipo.toUpperCase()) {
            case "CLIENTE":
                if (parametriExtra.length >= 2) {
                    return new Cliente(id, name, parametriExtra[0], parametriExtra[1]); // email, indirizzo
                }
                throw new IllegalArgumentException("Cliente richiede email e indirizzo");
                
            case "TRASFORMATORE":
                if (parametriExtra.length >= 1) {
                    return new Trasformatore(id, name, parametriExtra[0]); // licenza
                }
                throw new IllegalArgumentException("Trasformatore richiede licenza");
                
            case "CURATORE":
                if (parametriExtra.length >= 1) {
                    return new Curatore(id, name, parametriExtra[0]); // settoreSpecializzazione
                }
                throw new IllegalArgumentException("Curatore richiede settore di specializzazione");
                
            case "OSPITE":
                if (parametriExtra.length >= 1) {
                    return new UtenteOspite(parametriExtra[0]); // sessionId
                }
                throw new IllegalArgumentException("Utente ospite richiede session ID");
                
            case "PRODUTTORE":
            case "DISTRIBUTORE":
            case "ANIMATORE":
            case "GESTORE":
                return new User(id, name, tipo.toUpperCase());
                
            default:
                throw new IllegalArgumentException("Tipo utente non supportato: " + tipo);
        }
    }
    
    public static Cliente createCliente(Long id, String name, String email, String indirizzo) {
        return new Cliente(id, name, email, indirizzo);
    }
    
    public static Trasformatore createTrasformatore(Long id, String name, String licenza) {
        return new Trasformatore(id, name, licenza);
    }
    
    public static Curatore createCuratore(Long id, String name, String settoreSpecializzazione) {
        return new Curatore(id, name, settoreSpecializzazione);
    }
    
    public static UtenteOspite createUtenteOspite(String sessionId) {
        return new UtenteOspite(sessionId);
    }
}