package it.unicam.cs.filieraagricola.service;

import it.unicam.cs.filieraagricola.DTO.PaccoProdottoDTO;
import it.unicam.cs.filieraagricola.DTO.PaccoProdottoRequestDTO;
import it.unicam.cs.filieraagricola.mapper.ProdottoMapper;
import it.unicam.cs.filieraagricola.model.Distributore;
import it.unicam.cs.filieraagricola.model.PaccoProdotto;
import it.unicam.cs.filieraagricola.model.Prodotto;
import it.unicam.cs.filieraagricola.repository.PaccoProdottoRepository;
import it.unicam.cs.filieraagricola.repository.ProdottoRepository;
import it.unicam.cs.filieraagricola.repository.UtenteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
public class PaccoProdottoServiceImpl implements PaccoProdottoService {

    @Autowired private PaccoProdottoRepository paccoProdottoRepository;
    @Autowired private ProdottoRepository prodottoRepository;
    @Autowired private UtenteRepository utenteRepository;
    @Autowired private ProdottoMapper prodottoMapper;

    @Override
    public List<PaccoProdottoDTO> getPacchettiByDistributore(Long distributoreId) {
        return paccoProdottoRepository.findByDistributoreId(distributoreId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaccoProdottoDTO getPaccoById(Long paccoId) {
        PaccoProdotto pacco = findPaccoById(paccoId);
        return convertToDTO(pacco);
    }

    @Override
    public PaccoProdottoDTO creaPacco(PaccoProdottoRequestDTO request, Long distributoreId) {
        // 1. Trova il Distributore
        Distributore distributore = (Distributore) utenteRepository.findById(distributoreId)
                .orElseThrow(() -> new EntityNotFoundException("Distributore non trovato"));

        // 2. Trova i prodotti da includere
        List<Prodotto> prodottiInclusi = prodottoRepository.findAllById(request.getProdottoIds());

        // 3. Crea il pacco
        PaccoProdotto pacco = new PaccoProdotto();
        pacco.setNome(request.getNome());
        pacco.setDescrizione(request.getDescrizione());
        pacco.setPrezzo(request.getPrezzo());
        pacco.setDistributore(distributore);
        pacco.setProdotti(prodottiInclusi);

        PaccoProdotto salvato = paccoProdottoRepository.save(pacco);
        return convertToDTO(salvato);
    }

    @Override
    public void deletePacco(Long paccoId) {
        if (!paccoProdottoRepository.existsById(paccoId)) {
            throw new EntityNotFoundException("Pacco non trovato");
        }
        paccoProdottoRepository.deleteById(paccoId);
    }

    // --- Metodi Privati di Utilità ---

    private PaccoProdotto findPaccoById(Long id) {
        return paccoProdottoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pacco non trovato con ID: " + id));
    }

    private PaccoProdottoDTO convertToDTO(PaccoProdotto pacco) {
        PaccoProdottoDTO dto = new PaccoProdottoDTO();
        dto.setId(pacco.getId());
        dto.setNome(pacco.getNome());
        dto.setDescrizione(pacco.getDescrizione());
        dto.setPrezzo(pacco.getPrezzo());
        dto.setDistributoreId(pacco.getDistributore().getId());
        dto.setNomeDistributore(pacco.getDistributore().getNomeAzienda());
        dto.setProdottiInclusi(
                pacco.getProdotti().stream()
                        .map(prodottoMapper::toDTO)
                        .collect(Collectors.toList())
        );
        return dto;
    }
}