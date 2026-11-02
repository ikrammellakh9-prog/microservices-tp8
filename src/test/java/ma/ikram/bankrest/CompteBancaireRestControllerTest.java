package ma.ikram.bankrest;

import ma.ikram.bankrest.controllers.CompteBancaireRestController;
import ma.ikram.bankrest.entities.CompteBancaire;
import ma.ikram.bankrest.entities.TypeCompteBancaire;
import ma.ikram.bankrest.repositories.CompteBancaireRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompteBancaireRestControllerTest {

    @Mock
    private CompteBancaireRepository repository;

    @InjectMocks
    private CompteBancaireRestController controller;

    private CompteBancaire c1;
    private CompteBancaire c2;

    @BeforeEach
    void setUp() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        c1 = new CompteBancaire(1L, 18500.0, "MAD", new Date(), TypeCompteBancaire.COURANT);
        c2 = new CompteBancaire(2L, 52000.0, "EUR", new Date(), TypeCompteBancaire.EPARGNE);
    }

    @Test
    @DisplayName("Test 1: Récupérer tous les comptes multi-devises")
    void testGetAllComptes() {
        when(repository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<CompteBancaire> result = controller.getAllComptes();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("MAD", result.get(0).getDevise());
    }

    @Test
    @DisplayName("Test 2: Récupérer un compte par identifiant")
    void testGetCompteById() {
        when(repository.findById(1L)).thenReturn(Optional.of(c1));

        ResponseEntity<CompteBancaire> resp = controller.getCompteById(1L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(c1, resp.getBody());
    }

    @Test
    @DisplayName("Test 3: Compte inexistant retourne 404 Not Found")
    void testGetCompteNotFound() {
        when(repository.findById(88L)).thenReturn(Optional.empty());

        ResponseEntity<CompteBancaire> resp = controller.getCompteById(88L);

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }

    @Test
    @DisplayName("Test 4: Création d'un compte entreprise")
    void testCreateCompte() {
        CompteBancaire newAcc = new CompteBancaire(null, 75000.0, "USD", new Date(), TypeCompteBancaire.ENTREPRISE);
        CompteBancaire savedAcc = new CompteBancaire(3L, 75000.0, "USD", newAcc.getDateCreation(), TypeCompteBancaire.ENTREPRISE);
        when(repository.save(any(CompteBancaire.class))).thenReturn(savedAcc);

        ResponseEntity<CompteBancaire> resp = controller.createCompte(newAcc);

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertEquals(savedAcc, resp.getBody());
    }

    @Test
    @DisplayName("Test 5: Mise à jour du solde et devise")
    void testUpdateCompte() {
        CompteBancaire updateData = new CompteBancaire(null, 22000.0, "MAD", new Date(), TypeCompteBancaire.COURANT);
        when(repository.findById(1L)).thenReturn(Optional.of(c1));
        when(repository.save(any(CompteBancaire.class))).thenReturn(c1);

        ResponseEntity<CompteBancaire> resp = controller.updateCompte(1L, updateData);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(22000.0, c1.getSolde());
    }

    @Test
    @DisplayName("Test 6: Clôture de compte par ID")
    void testDeleteCompte() {
        when(repository.findById(1L)).thenReturn(Optional.of(c1));
        doNothing().when(repository).delete(c1);

        ResponseEntity<Void> resp = controller.deleteCompte(1L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        verify(repository, times(1)).delete(c1);
    }
}
