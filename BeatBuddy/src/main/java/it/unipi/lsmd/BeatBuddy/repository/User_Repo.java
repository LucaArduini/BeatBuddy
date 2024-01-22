package it.unipi.lsmd.BeatBuddy.repository;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;

import it.unipi.lsmd.BeatBuddy.model.ReviewedAlbum;
import it.unipi.lsmd.BeatBuddy.model.User;
import it.unipi.lsmd.BeatBuddy.model.UserForRegistration;
import it.unipi.lsmd.BeatBuddy.repository.MongoDB.UserForRegistration_RepoInterf;
import it.unipi.lsmd.BeatBuddy.repository.MongoDB.User_RepoInterf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class User_Repo {
    @Autowired
    private User_RepoInterf user_RI;
    @Autowired
    private UserForRegistration_RepoInterf userForRegistration_RI;

//    @Autowired
//    private MongoOperations mongoOperations;

    public Optional<User> getUserByUsername(String username) {
        try {
            System.out.println("sono qui, cerco l'utente: " + username);
            System.out.println(user_RI.existsByUsername(username));
            return user_RI.findByUsername(username);
        } catch (DataAccessException dae) {
            // Controllo se l'eccezione è relativa a una mancata connessione al database
            if (dae instanceof DataAccessResourceFailureException) {
                throw (DataAccessResourceFailureException) dae;
            }
            dae.printStackTrace();
            return Optional.empty();
        }
    }

    public int insertUser(String name, String surname, String username, String password, String birthDate, String email) {
        try {
            if (user_RI.existsByUsername(username)) {
                return 1; // Username già esistente
            }
            if (user_RI.existsByEmail(email)) {
                return 2; // Email già esistente
            }

            // Hashing della password
            String hashedPassword = Hashing.sha256()
                    .hashString(password, StandardCharsets.UTF_8)
                    .toString();

            // Creazione di un nuovo utente
            UserForRegistration newUser = new UserForRegistration();
            newUser.setName(name);
            newUser.setSurname(surname);
            newUser.setUsername(username);
            newUser.setPassword(hashedPassword);
            newUser.setBirthDate(birthDate);
            newUser.setEmail(email);
            newUser.setReviewedAlbums(new ReviewedAlbum[0]);
            //NB: la classe UserForRegistration è uguale a User, ma senza il campo 'admin'

            // Salvataggio del nuovo utente
            userForRegistration_RI.save(newUser);
            return 0; // Inserimento riuscito

        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException) {
                // Gestione specifica per errori di connessione al database
                dae.printStackTrace();
                return 3;
            } else {
                // Gestione generica per altri errori di database
                dae.printStackTrace();
                return 4;
            }
        }
    }

    public int checkUserDataExistence(String email, String username) {
        Optional<User> userOpt = user_RI.findByEmailOrUsername(email, username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (email.equals(user.getEmail())) {
                return 1;
            } else if (username.equals(user.getUsername())) {
                return 2;
            }
        }
        return 0;
    }


}
