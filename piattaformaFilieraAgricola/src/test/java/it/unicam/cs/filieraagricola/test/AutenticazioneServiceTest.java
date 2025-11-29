package it.unicam.cs.filieraagricola.test;

import it.unicam.cs.filieraagricola.DTO.AuthResponseDTO;
import it.unicam.cs.filieraagricola.DTO.LoginRequestDTO;
import it.unicam.cs.filieraagricola.DTO.RegisterRequestDTO;
import it.unicam.cs.filieraagricola.factory.UtenteFactory;
import it.unicam.cs.filieraagricola.model.Acquirente;
import it.unicam.cs.filieraagricola.model.Utente;
import it.unicam.cs.filieraagricola.repository.UtenteRepository;
import it.unicam.cs.filieraagricola.service.AutenticazioneServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AutenticazioneServiceTest {

    @Mock
    private UtenteRepository utenteRepository;

    @Mock
    private UtenteFactory utenteFactory;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock // Mockiamo anche l'oggetto Authentication restituito dal manager
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private AutenticazioneServiceImpl autenticazioneService;

    private RegisterRequestDTO registerRequest;
    private LoginRequestDTO loginRequest;
    private Utente utenteMock;

    @BeforeEach
    void setUp() {
        // Setup Security Context per evitare NullPointer su SecurityContextHolder
        SecurityContextHolder.setContext(securityContext);

        // Dati di registrazione
        registerRequest = new RegisterRequestDTO();
        registerRequest.setUsername("testUser");
        registerRequest.setEmail("test@email.com");
        registerRequest.setPassword("password");
        registerRequest.setRuolo("ACQUIRENTE");

        // Dati di login
        loginRequest = new LoginRequestDTO("testUser", "password");

        // Utente mockato (Acquirente)
        utenteMock = new Acquirente("testUser", "test@email.com", "encodedPassword");
        utenteMock.setId(1L);
        // IMPORTANTE: Simuliamo ciò che farebbe Hibernate popolando il campo ruoloString
        utenteMock.setRuoloString("ACQUIRENTE");
    }

    // --- TEST 1: REGISTRAZIONE ---

    @Test
    void testRegistraUtente_Successo() {
        // Arrange
        // 1. Controllo esistenza (deve ritornare vuoto) -> Poi per il login automatico (deve ritornare pieno)
        when(utenteRepository.findByUsername("testUser"))
                .thenReturn(Optional.empty())       // 1a chiamata (controllo)
                .thenReturn(Optional.of(utenteMock)); // 2a chiamata (login interno)

        when(utenteRepository.findByEmail("test@email.com")).thenReturn(Optional.empty());

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        when(utenteFactory.creaUtente(any(RegisterRequestDTO.class), eq("encodedPassword")))
                .thenReturn(utenteMock);

        // Mock del login interno
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // Act
        AuthResponseDTO response = autenticazioneService.registraUtente(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("testUser", response.getUsername());
        assertEquals("ACQUIRENTE", response.getRuolo());

        // Verifiche
        verify(utenteRepository).save(utenteMock); // È stato salvato?
        verify(authenticationManager).authenticate(any()); // Ha fatto il login automatico?
    }

    @Test
    void testRegistraUtente_UsernameEsistente() {
        // Arrange: Username già presente
        when(utenteRepository.findByUsername("testUser")).thenReturn(Optional.of(utenteMock));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                autenticazioneService.registraUtente(registerRequest)
        );

        assertEquals("Errore: Username già in uso!", exception.getMessage());
        verify(utenteRepository, never()).save(any()); // Non deve salvare nulla
    }

    @Test
    void testRegistraUtente_EmailEsistente() {
        // Arrange: Username ok, ma Email presente
        when(utenteRepository.findByUsername("testUser")).thenReturn(Optional.empty());
        when(utenteRepository.findByEmail("test@email.com")).thenReturn(Optional.of(utenteMock));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                autenticazioneService.registraUtente(registerRequest)
        );

        assertEquals("Errore: Email già in uso!", exception.getMessage());
        verify(utenteRepository, never()).save(any());
    }

    // --- TEST 2: LOGIN ---

    @Test
    void testLoginUtente_Successo() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(utenteRepository.findByUsername("testUser")).thenReturn(Optional.of(utenteMock));

        // Act
        AuthResponseDTO response = autenticazioneService.loginUtente(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("testUser", response.getUsername());
        assertEquals("ACQUIRENTE", response.getRuolo()); // Verifica che legga ruoloString
        assertNotNull(response.getToken()); // Verifica che ci sia il dummy token

        // Verifica che il contesto di sicurezza sia stato aggiornato
        verify(securityContext).setAuthentication(authentication);
    }

    @Test
    void testLoginUtente_CredenzialiErrate() {
        // Arrange: Simuliamo che l'AuthenticationManager lanci eccezione
        when(authenticationManager.authenticate(any()))
                .thenThrow(new RuntimeException("Bad Credentials")); // O BadCredentialsException specifica di Spring

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                autenticazioneService.loginUtente(loginRequest)
        );
    }
}