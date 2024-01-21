package it.unipi.lsmd.BeatBuddy.repository;

import it.unipi.lsmd.BeatBuddy.model.User;
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

//    @Autowired
//    private MongoOperations mongoOperations;

    public Optional<User> getUserByUsername(String username) {
        try {
            System.out.println("sono qui, cerco l'utente: " + username);
            System.out.println(user_RI.existsByUsername(username));
            return user_RI.findByUsername(username);
        } catch (DataAccessException dae) {
            // Controlla se l'eccezione è relativa a una mancata connessione al database
            if (dae instanceof DataAccessResourceFailureException) {
                throw (DataAccessResourceFailureException) dae;
            }
            dae.printStackTrace();
            return Optional.empty();
        }
    }

    public boolean insertUser(User user){
        try {
            user_RI.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
