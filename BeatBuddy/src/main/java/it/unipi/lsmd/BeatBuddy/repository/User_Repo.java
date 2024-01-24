package it.unipi.lsmd.BeatBuddy.repository;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

import it.unipi.lsmd.BeatBuddy.DTO.UserDTO;
import it.unipi.lsmd.BeatBuddy.model.ReviewedAlbum;
import it.unipi.lsmd.BeatBuddy.model.User;
import it.unipi.lsmd.BeatBuddy.model.UserForRegistration;
import it.unipi.lsmd.BeatBuddy.repository.MongoDB.UserForRegistration_MongoInterf;
import it.unipi.lsmd.BeatBuddy.repository.MongoDB.User_MongoInterf;
import it.unipi.lsmd.BeatBuddy.repository.Neo4j.User_Neo4jInterf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class User_Repo {
    @Autowired
    private User_MongoInterf user_RI_MongoDB;
    @Autowired
    private UserForRegistration_MongoInterf userForRegistration_RI;
    @Autowired
    private Album_Repo album_Repo;
    @Autowired
    private User_Neo4jInterf user_RI_Neo4j;

//------------------------- FUNZIONI PER MongoDB -------------------------
    public Optional<User> getUserByUsername(String username) {
        try {
            System.out.println("sono qui, cerco l'utente: " + username);
            System.out.println(user_RI_MongoDB.existsByUsername(username));
            return user_RI_MongoDB.findByUsername(username);
        } catch (DataAccessException dae) {
            // Controllo se l'eccezione è relativa a una mancata connessione al database
            if (dae instanceof DataAccessResourceFailureException) {
                throw (DataAccessResourceFailureException) dae;
            }
            dae.printStackTrace();
            return Optional.empty();
        }
    }
  
    public List<UserDTO> find5UserDTO(String term){
        Pageable topFive = PageRequest.of(0, 5);
        return user_RI_MongoDB.findFirst5ByUsernameContaining(term, topFive);
    }

    public int insertUserMongoDB(String name, String surname, String username, String password, String birthDate, String email) {
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

//    public int insertReviewIntoUser(String albumID, int rating, String username) {
//        Album targetAlbum = album_Repo.getAlbumById(albumID).orElse(null);
//        if (targetAlbum == null) {
//            return 1; // Album non trovato
//        }
//
//        ReviewedAlbum tmp_reviewedAlbum = new ReviewedAlbum(targetAlbum.getTitle(), targetAlbum.getCoverURL(), targetAlbum.getArtistsString(), rating);
//
//        try {
//            int outcome = addReviewedAlbum(username, tmp_reviewedAlbum);
//            if (outcome != 0) {
//                return 3; // Errore durante l'inserimento della review
//            }
//            return 0; // Inserimento riuscito
//
//        } catch (DataAccessException dae) {
//            if (dae instanceof DataAccessResourceFailureException) {
//                // Gestione specifica per errori di connessione al database
//                dae.printStackTrace();
//                return 2;
//            } else {
//                // Gestione generica per altri errori di database
//                dae.printStackTrace();
//                return 3;
//            }
//        }
//    }

//    public int addReviewedAlbum(String username, ReviewedAlbum tmp_reviewedAlbum){
//        try {
//            user_RI_MongoDB.addReviewedAlbum(username, tmp_reviewedAlbum);
//            System.out.println("Review inserita con successo in username: " + username);
//            return 0; // Inserimento riuscito
//        } catch (DataAccessException dae) {
//            if (dae instanceof DataAccessResourceFailureException) {
//                // Gestione specifica per errori di connessione al database
//                dae.printStackTrace();
//                return 2;
//            } else {
//                // Gestione generica per altri errori di database
//                dae.printStackTrace();
//                return 3;
//            }
//        }
//    }

    public int checkUserDataExistence(String email, String username) {
        Optional<User> userOpt = user_RI_MongoDB.findByEmailOrUsername(email, username);
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

//------------------------- FUNZIONI PER Neo4j -------------------------
    public int insertUserNeo4j(String username){
        try {
            user_RI_Neo4j.createUser(username);
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

    public boolean addFollow(String user1, String user2) {
        try {
            user_RI_Neo4j.addFollow(user1, user2);
            return true;
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            return false;
        }
    }

    public boolean removeFollow(String user1, String user2) {
        try {
            user_RI_Neo4j.removeFollow(user1, user2);
            return true;
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            return false;
        }
    }

    public boolean addLikes_A(String username, String coverURL) {
        try {
            user_RI_Neo4j.addLikes_A(username, coverURL);
            return true;
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            return false;
        }
    }

    public boolean removeLikes_A(String username, String coverURL) {
        try {
            user_RI_Neo4j.removeLikes_A(username, coverURL);
            return true;
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            return false;
        }
    }

    public boolean addLikes_S(String username, String title, String coverURL) {
        try {
            user_RI_Neo4j.addLikes_S(username, title, coverURL);
            return true;
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            return false;
        }
    }

    public boolean removeLikes_S(String username, String title, String coverURL) {
        try {
            user_RI_Neo4j.removeLikes_S(username, title, coverURL);
            return true;
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            return false;
        }
    }

//    public boolean addLikes_S(String username, String title, String albumName, String artists) {
//        try {
//            user_RI_Neo4j.addLikes_S(username, title, albumName, artists);
//            return true;
//        } catch (DataAccessException dae) {
//            dae.printStackTrace();
//            return false;
//        }
//    }

//    public boolean removeLikes_S(String username, String title, String albumName, String artists) {
//        try {
//            user_RI_Neo4j.removeLikes_S(username, title, albumName, artists);
//            return true;
//        } catch (DataAccessException dae) {
//            dae.printStackTrace();
//            return false;
//        }
//    }

}
