package it.unipi.lsmd.BeatBuddy.repository;

import it.unipi.lsmd.BeatBuddy.model.Album_Neo4j;
import it.unipi.lsmd.BeatBuddy.model.Song_Neo4j;
import it.unipi.lsmd.BeatBuddy.model.User_Neo4j;
import it.unipi.lsmd.BeatBuddy.repository.Neo4j.User_Neo4jInterf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class User_Repo_Neo4j {

    @Autowired
    private User_Neo4jInterf user_RI_Neo4j;
    @Autowired
    private Neo4jClient neo4jClient;

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

    public String addFollow(String user1, String user2) {
        try {
            String created = user_RI_Neo4j.addFollow(user1, user2);
            return created;
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return "ERR";
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

    public String[] addLikes_A(String username, String coverURL) {
        try {
            String[] created = user_RI_Neo4j.addLikes_A(username, coverURL);
            return created;
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            String[] err = {"ERR"};
            return err;
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

    public String addLikes_S(String username, String title, String coverURL) {
        try {
            String created = user_RI_Neo4j.addLikes_S(username, title, coverURL);
            return created;
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return "ERR";
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

    public Song_Neo4j[] getLikedSongsByUsername(String username) {
        try {
            return findLikedSongsByUsername(username);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    public Album_Neo4j[] getLikedAlbumsByUsername(String username) {
        try {
            return findLikedAlbumsByUsername(username);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    public List<User_Neo4j> getFollowedUsersByUsername(String username) {
        try {
            return user_RI_Neo4j.findFollowedUsersByUsername(username);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    public Song_Neo4j[] findLikedSongsByUsername(String username) {
        String cypherQuery = "MATCH (u:User {username: $username})-[:LIKES_S]->(s:Song) " +
                             "RETURN s.songName AS songName, s.albumName AS albumName, " +
                             "s.artistName AS artistName, s.coverUrl AS coverUrl";

        return Song_Neo4j.getSongNeo4js(username, cypherQuery, neo4jClient).toArray(new Song_Neo4j[0]);
    }

    public Album_Neo4j[] findLikedAlbumsByUsername(String username) {
        String cypherQuery = "MATCH (u:User {username: $username})-[:LIKES_A]->(a:Album) " +
                             "RETURN a.coverURL AS coverURL, a.albumName AS albumName, " +
                             "a.artistName AS artistName";

        return Album_Neo4j.getAlbumNeo4js(username, cypherQuery, neo4jClient).toArray(new Album_Neo4j[0]);
    }

    public ArrayList<User_Neo4j> findSuggestedUserstoFollow(String username) {
        String cypherQuery = "MATCH (targetUser:User)-[:FOLLOW]->(friend:User) " +
                             "WHERE targetUser.username=$username " +
                             "MATCH (friend)-[r:FOLLOW]->(recommendedUser:User) " +
                             "WHERE NOT (targetUser)-[:FOLLOW]->(recommendedUser) " +
                             "WITH recommendedUser, COUNT(*) AS recommendationStrength " +
                             "RETURN recommendedUser.username AS username " +
                             "ORDER BY recommendationStrength DESC " +
                             "LIMIT 10";

        return User_Neo4j.getUserNeo4js(username, cypherQuery, neo4jClient);
    }
}
