package it.unipi.lsmd.BeatBuddy.repository;

import it.unipi.lsmd.BeatBuddy.repository.Neo4j.User_Neo4jInterf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Repository;

@Repository
public class User_Repo_Neo4j {

    @Autowired
    private User_Neo4jInterf user_RI_Neo4j;

    public int insertUser(String username){
        try {
            user_RI_Neo4j.createUser(username);
            return 0; // Inserimento riuscito
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException) {
                // Gestione specifica per errori di connessione al database
                dae.printStackTrace();
                return 1;
            } else {
                // Gestione generica per altri errori di database
                dae.printStackTrace();
                return 2;
            }
        }
    }

    public boolean addFollow(String user1, String user2) {
        try {
            user_RI_Neo4j.addFollow(user1, user2);
            return true;
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }

    public boolean removeFollow(String user1, String user2) {
        try {
            user_RI_Neo4j.removeFollow(user1, user2);
            return true;
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }

    public boolean addLikes_A(String username, String coverURL) {
        try {
            user_RI_Neo4j.addLikes_A(username, coverURL);
            return true;
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }

    public boolean removeLikes_A(String username, String coverURL) {
        try {
            user_RI_Neo4j.removeLikes_A(username, coverURL);
            return true;
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }

    public boolean addLikes_S(String username, String title, String coverURL) {
        try {
            user_RI_Neo4j.addLikes_S(username, title, coverURL);
            return true;
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }

    public boolean removeLikes_S(String username, String title, String coverURL) {
        try {
            user_RI_Neo4j.removeLikes_S(username, title, coverURL);
            return true;
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }
}
