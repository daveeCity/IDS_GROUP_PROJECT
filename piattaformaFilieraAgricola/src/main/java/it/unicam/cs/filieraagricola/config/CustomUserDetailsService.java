package it.unicam.cs.filieraagricola.config;

import it.unicam.cs.filieraagricola.model.Utente;
import it.unicam.cs.filieraagricola.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Recupera l'utente generico (Hibernate istanzierà la sottoclasse corretta: Produttore, Acquirente, ecc.)
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + username));

        // 2. Determina il ruolo basandosi sulla Classe Java dell'istanza
        String ruolo = ricavaRuoloDaClasse(utente);

        // 3. Costruisci l'User di Spring Security
        return User.builder()
                .username(utente.getUsername())
                .password(utente.getPassword())
                .roles(ruolo) // Spring aggiungerà automaticamente il prefisso "ROLE_"
                .build();
    }

    /**
     * Metodo helper robusto per ricavare il ruolo.
     * Usa la Reflection Java invece del campo nel DB per evitare NullPointer
     * durante la creazione dei dati demo.
     */
    private String ricavaRuoloDaClasse(Utente utente) {
        if (utente == null) {
            return "UTENTE"; // Fallback di sicurezza
        }

        // USA QUESTO: getClass().getSimpleName() non è mai null per oggetti reali
        String nomeClasse = utente.getClass().getSimpleName();

        // Verifica di sicurezza (anche se getSimpleName non torna null, meglio essere paranoici)
        if (nomeClasse == null) {
            return "UTENTE";
        }

        // GESTIONE PROXY HIBERNATE (es. Produttore$HibernateProxy$...)
        if (nomeClasse.contains("$")) {
            nomeClasse = nomeClasse.split("\\$")[0];
        }

        return nomeClasse.toUpperCase();
    }
}