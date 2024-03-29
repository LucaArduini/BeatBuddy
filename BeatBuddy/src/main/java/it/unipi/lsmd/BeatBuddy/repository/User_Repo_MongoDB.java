package it.unipi.lsmd.BeatBuddy.repository;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

import it.unipi.lsmd.BeatBuddy.DTO.UserDTO;
import it.unipi.lsmd.BeatBuddy.model.ReviewedAlbum;
import it.unipi.lsmd.BeatBuddy.model.User;
import it.unipi.lsmd.BeatBuddy.model.UserForRegistration;
import it.unipi.lsmd.BeatBuddy.repository.MongoDB.UserForRegistration_MongoInterf;
import it.unipi.lsmd.BeatBuddy.repository.MongoDB.User_MongoInterf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.Optional;

@Repository
public class User_Repo_MongoDB {

    @Autowired
    private User_MongoInterf user_RI_MongoDB;
    @Autowired
    private UserForRegistration_MongoInterf userForRegistration_RI;
    @Autowired
    private MongoTemplate mongoTemplate;

    public User getUserByUsername(String username) {
        try {
            Optional<User> result = user_RI_MongoDB.findByUsername(username);
            return result.orElse(null);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }
  
    public List<UserDTO> find5UserDTO(String term){
        try {
            Pageable topFive = PageRequest.of(0, 5);
            return user_RI_MongoDB.findLimitedUsersByUsernameContaining(term, topFive);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    public int insertUser(String name, String surname, String username, String password, String birthDate, String email) {
        try {
            if (user_RI_MongoDB.existsByUsername(username)) {
                return 1; // Username già esistente
            }
            if (user_RI_MongoDB.existsByEmail(email)) {
                return 2; // Email già esistente
            }

            // Hashing della password
            String hashedPassword = Hashing.sha256()
                    .hashString(password, StandardCharsets.UTF_8)
                    .toString();

            // Creazione di un nuovo utente
            UserForRegistration newUser = new UserForRegistration(null, name, surname, username, hashedPassword, birthDate, email, new ReviewedAlbum[0]);
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

    public boolean insertReviewIntoUser(String username, ReviewedAlbum reviewedAlbum) {
        try {
            addReviewedAlbumAsFirstIntoUser(username, reviewedAlbum);
            return true;
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }

    public void addReviewedAlbumAsFirstIntoUser(String username, ReviewedAlbum reviewedAlbum) {
        Query query = new Query(Criteria.where("username").is(username));
        Update update = new Update().push("reviewedAlbums").atPosition(0).each(reviewedAlbum);
        mongoTemplate.updateFirst(query, update, User.class);
    }
}
