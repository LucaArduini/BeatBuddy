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

import java.util.List;

@Repository
public class User_Repo_Neo4j {

    @Autowired
    private User_Neo4jInterf user_RI_Neo4j;
    @Autowired
    private Neo4jClient neo4jClient;

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

    /*public Song_Neo4j[] getLikedSongsByUsername(String username) {
        try {
            return findLikedSongsByUsername(username);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }*/

    /*public Album_Neo4j[] getLikedAlbumsByUsername(String username) {
        try {
            return user_RI_Neo4j.findLikedAlbumsByUsername(username).toArray(new Album_Neo4j[0]);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }*/

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

    /*public List<Album_Neo4j> getLikedAlbumsByUsername(String username) {
        try {
            return user_RI_Neo4j.findLikedAlbumsByUsername(username);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }*/

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

        List<Song_Neo4j> likedSongs = (List<Song_Neo4j>) neo4jClient
                .query(cypherQuery)
                .bind(username).to("username")
                .fetchAs(Song_Neo4j.class)
                .mappedBy((typeSystem, record) -> {
                    String songName = record.get("songName").asString();
                    String albumName = record.get("albumName").asString();
                    String artistName = record.get("artistName").asString();
                    String coverUrl = record.get("coverUrl").asString();
                    return new Song_Neo4j(songName, albumName, artistName, coverUrl);
                }).all();

        return likedSongs.toArray(new Song_Neo4j[0]);
    }

    public Album_Neo4j[] findLikedAlbumsByUsername(String username) {
        String cypherQuery = "MATCH (u:User {username: $username})-[:LIKES_A]->(a:Album) " +
                "RETURN a.coverURL AS coverURL, a.albumName AS albumName, " +
                "a.artistName AS artistName";

        List<Album_Neo4j> likedAlbums = (List<Album_Neo4j>) neo4jClient
                .query(cypherQuery)
                .bind(username).to("username")
                .fetchAs(Album_Neo4j.class)
                .mappedBy((typeSystem, record) -> {
                    String albumName = record.get("albumName").asString();
                    String artistName = record.get("artistName").asString();
                    String coverURL = record.get("coverURL").asString();
                    return new Album_Neo4j(albumName, artistName, coverURL);
                }).all();

        return likedAlbums.toArray(new Album_Neo4j[0]);
    }
}
