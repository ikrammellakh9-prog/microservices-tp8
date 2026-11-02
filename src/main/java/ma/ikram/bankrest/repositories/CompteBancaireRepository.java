package ma.ikram.bankrest.repositories;

import ma.ikram.bankrest.entities.CompteBancaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompteBancaireRepository extends JpaRepository<CompteBancaire, Long> {
}
