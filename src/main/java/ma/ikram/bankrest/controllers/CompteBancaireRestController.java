package ma.ikram.bankrest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.ikram.bankrest.entities.CompteBancaire;
import ma.ikram.bankrest.repositories.CompteBancaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/banque-api")
@Tag(name = "Bank Controller - Ikram Mellakh", description = "Gestion REST des comptes bancaires multi-devises")
public class CompteBancaireRestController {

    @Autowired
    private CompteBancaireRepository repository;

    @Operation(summary = "Lister tous les comptes bancaires (JSON & XML)")
    @GetMapping(value = "/comptes", produces = {"application/json", "application/xml"})
    public List<CompteBancaire> getAllComptes() {
        return repository.findAll();
    }

    @Operation(summary = "Consulter un compte par ID")
    @GetMapping(value = "/comptes/{id}", produces = {"application/json", "application/xml"})
    public ResponseEntity<CompteBancaire> getCompteById(@PathVariable Long id) {
        return repository.findById(id)
                .map(compte -> ResponseEntity.ok().body(compte))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Ouvrir un nouveau compte bancaire (JSON & XML)")
    @PostMapping(value = "/comptes",
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CompteBancaire> createCompte(@RequestBody CompteBancaire compte) {
        if (compte.getDevise() == null || compte.getDevise().isEmpty()) {
            compte.setDevise("MAD");
        }
        CompteBancaire saved = repository.save(compte);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location).body(saved);
    }

    @Operation(summary = "Mettre à jour un compte existant")
    @PutMapping(value = "/comptes/{id}",
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    public ResponseEntity<CompteBancaire> updateCompte(@PathVariable Long id, @RequestBody CompteBancaire details) {
        return repository.findById(id)
                .map(compte -> {
                    compte.setSolde(details.getSolde());
                    compte.setDateCreation(details.getDateCreation());
                    compte.setTypeCompte(details.getTypeCompte());
                    if (details.getDevise() != null) {
                        compte.setDevise(details.getDevise());
                    }
                    CompteBancaire updated = repository.save(compte);
                    return ResponseEntity.ok().body(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Clôturer un compte bancaire par ID")
    @DeleteMapping("/comptes/{id}")
    public ResponseEntity<Void> deleteCompte(@PathVariable Long id) {
        return repository.findById(id)
                .map(compte -> {
                    repository.delete(compte);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
