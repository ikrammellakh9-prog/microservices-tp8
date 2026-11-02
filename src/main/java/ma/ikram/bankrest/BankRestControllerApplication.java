package ma.ikram.bankrest;

import ma.ikram.bankrest.entities.CompteBancaire;
import ma.ikram.bankrest.entities.TypeCompteBancaire;
import ma.ikram.bankrest.repositories.CompteBancaireRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class BankRestControllerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankRestControllerApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CompteBancaireRepository repository) {
        return args -> {
            repository.save(new CompteBancaire(null, 18500.0, "MAD", new Date(), TypeCompteBancaire.COURANT));
            repository.save(new CompteBancaire(null, 52000.0, "EUR", new Date(), TypeCompteBancaire.EPARGNE));
            repository.save(new CompteBancaire(null, 120000.0, "USD", new Date(), TypeCompteBancaire.ENTREPRISE));

            System.out.println("=== BankRestControllerApplication (Ikram Mellakh) Initialisee ===");
            repository.findAll().forEach(System.out::println);
        };
    }
}
