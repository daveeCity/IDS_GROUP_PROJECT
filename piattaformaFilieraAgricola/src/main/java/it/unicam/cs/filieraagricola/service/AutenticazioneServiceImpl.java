package it.unicam.cs.filieraagricola.service;
import it.unicam.cs.filieraagricola.DTO.AuthResponseDTO;
import it.unicam.cs.filieraagricola.DTO.LoginRequestDTO;
import it.unicam.cs.filieraagricola.DTO.RegisterRequestDTO;
import it.unicam.cs.filieraagricola.factory.UtenteFactory;
import it.unicam.cs.filieraagricola.model.Utente;
import it.unicam.cs.filieraagricola.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AutenticazioneServiceImpl implements AutenticazioneService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private UtenteFactory utenteFactory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    // @Autowired
    // private JwtTokenProvider tokenProvider; // Assumendo che tu abbia questa classe

    @Override
    public AuthResponseDTO registraUtente(RegisterRequestDTO request) {

        // 1. Controlla se l'utente esiste già
        if (utenteRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Errore: Username già in uso!");
        }
        if (utenteRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Errore: Email già in uso!");
        }

        // 2. Codifica la password
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // 3. Usa la Factory per creare l'utente corretto
        Utente nuovoUtente = utenteFactory.creaUtente(request, hashedPassword);

        // 4. Salva l'utente
        utenteRepository.save(nuovoUtente);

        // 5. Esegui il login automatico per ottenere il token
        return loginUtente(new LoginRequestDTO(request.getUsername(), request.getPassword()));
    }

    @Override
    public AuthResponseDTO loginUtente(LoginRequestDTO request) {

        // 1. Esegui l'autenticazione con Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // 2. Imposta l'autenticazione nel contesto di sicurezza
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Genera il Token JWT (Logica da implementare nel TokenProvider)
        // String jwt = tokenProvider.generateToken(authentication);
        String jwt = "dummy-jwt-token"; // Placeholder

        // 4. Ottieni i dettagli dell'utente per la risposta
        Utente utente = utenteRepository.findByUsername(request.getUsername()).get();
        String ruolo = utente.getClass().getAnnotation(javax.persistence.DiscriminatorValue.class).value();

        return new AuthResponseDTO(jwt, utente.getUsername(), ruolo);
    }
}