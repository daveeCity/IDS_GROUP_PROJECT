package main.java.it.unicam.cs.filieraagricola.service;

import main.java.it.unicam.cs.filieraagricola.model.User;
import main.java.it.unicam.cs.filieraagricola.factory.UserFactory;
import main.java.it.unicam.cs.filieraagricola.util.IdGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ServizioAutenticazione {
    private Map<String, User> utentiRegistrati = new HashMap<>();
    private Map<String, String> credenziali = new HashMap<>(); // username -> password
    private User utenteCorrente = null;
    
    public User registra(String username, String password, String tipoUtente, String... parametriExtra) {
        if (utentiRegistrati.containsKey(username)) {
            throw new IllegalArgumentException("Username già esistente: " + username);
        }
        
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password deve avere almeno 6 caratteri");
        }
        
        Long id = IdGenerator.generateId();
        User nuovoUtente = UserFactory.createUser(tipoUtente, id, username, parametriExtra);
        
        utentiRegistrati.put(username, nuovoUtente);
        credenziali.put(username, password);
        
        System.out.println("✅ Utente registrato: " + nuovoUtente);
        return nuovoUtente;
    }
    
    public boolean login(String username, String password) {
        if (!utentiRegistrati.containsKey(username)) {
            System.out.println("❌ Username non trovato: " + username);
            return false;
        }
        
        if (!credenziali.get(username).equals(password)) {
            System.out.println("❌ Password errata per: " + username);
            return false;
        }
        
        utenteCorrente = utentiRegistrati.get(username);
        System.out.println("✅ Login effettuato: " + utenteCorrente.getName() + " (" + utenteCorrente.getRole() + ")");
        return true;
    }
    
    public void logout() {
        if (utenteCorrente != null) {
            System.out.println("👋 Logout: " + utenteCorrente.getName());
            utenteCorrente = null;
        }
    }
    
    public boolean isLoggedIn() {
        return utenteCorrente != null;
    }
    
    public Optional<User> getUtenteCorrente() {
        return Optional.ofNullable(utenteCorrente);
    }
    
    public boolean hasRole(String role) {
        return utenteCorrente != null && utenteCorrente.getRole().equals(role);
    }
    
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        if (!login(username, oldPassword)) {
            return false;
        }
        
        if (newPassword.length() < 6) {
            throw new IllegalArgumentException("Nuova password deve avere almeno 6 caratteri");
        }
        
        credenziali.put(username, newPassword);
        System.out.println("🔑 Password cambiata per: " + username);
        return true;
    }
    
    public int getNumeroUtentiRegistrati() {
        return utentiRegistrati.size();
    }
}