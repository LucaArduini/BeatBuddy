package it.unipi.lsmd.BeatBuddy.repository;

import it.unipi.lsmd.BeatBuddy.model.Song_Neo4j;
import it.unipi.lsmd.BeatBuddy.model.dummy.SongWithLikes;
import it.unipi.lsmd.BeatBuddy.repository.Neo4j.Song_Neo4jInterf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Song_Repo_Neo4j {

    @Autowired
    private Song_Neo4jInterf songNeo4jRepository;

    @Autowired
    private Neo4jClient neo4jClient;

    public List<SongWithLikes> getSongsByLikes_LastWeek(){
        try {
            return findSongsSortedByLikes_LastWeek();
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }
    private List<SongWithLikes> findSongsSortedByLikes_LastWeek() {
        String cypherQuery = "MATCH (s:Song) <-[r:LIKES_S]- (:User) " +
                "WHERE date(r.timestamp) >= date() - duration('P7D') " +
                "WITH s, count(r) as likes " +
                "RETURN DISTINCT s.songName AS songName, s.albumName AS albumName, " +
                "s.artistName AS artistName, s.coverUrl AS coverUrl, likes " +
                "ORDER BY likes DESC " +
                "LIMIT 5";

        return SongWithLikes.getSongWithLikes(cypherQuery, neo4jClient);
    }

    public ArrayList<Song_Neo4j> getSuggestedSongs_ByTaste(String username){
        try {
            return findSuggestedSongs_ByTaste(username);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    // dato un utente, restituisce le canzoni consigliate in base ai suoi gusti
    // (canzoni che piacciono ad utenti che hanno gusti simili)
    private ArrayList<Song_Neo4j> findSuggestedSongs_ByTaste(String username) {
        String cypherQuery = "MATCH (targetUser:User {username: $username})-[:LIKES_S]->(likedSong:Song) " +
                             "MATCH (similarUser:User)-[:LIKES_S]->(likedSong) " +
                             "WHERE targetUser <> similarUser " +
                             "MATCH (similarUser)-[:LIKES_S]->(recommendedSong:Song) " +
                             "WHERE NOT (targetUser)-[:LIKES_S]->(recommendedSong) " +
                             "WITH recommendedSong, COUNT(*) AS recommendationStrength " +
                             "RETURN recommendedSong.songName AS songName, " +
                                    "recommendedSong.albumName AS albumName, " +
                                    "recommendedSong.artistName AS artistName, " +
                                    "recommendedSong.coverUrl AS coverUrl " +
                             "ORDER BY recommendationStrength " +
                             "LIMIT 10";

        return Song_Neo4j.getSongNeo4js(username, cypherQuery, neo4jClient);
    }

    public ArrayList<Song_Neo4j> getSuggestedSongs_ByFollowed(String username){
        try {
            return findSuggestedSongs_ByFollowed(username);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    private ArrayList<Song_Neo4j> findSuggestedSongs_ByFollowed(String username) {
        String cypherQuery = "MATCH (targetUser:User)-[:FOLLOW]->(friend:User) " +
                             "WHERE targetUser.username=$username " +
                             "MATCH (friend)-[r:LIKES_S]->(recommendedSong:Song) " +
                             "WHERE NOT (targetUser)-[:LIKES_S]->(recommendedSong) " +
                             "WITH recommendedSong, COUNT(*) AS recommendationStrength " +
                             "RETURN recommendedSong.songName AS songName, " +
                                     "recommendedSong.albumName AS albumName, " +
                                     "recommendedSong.artistName AS artistName, " +
                                     "recommendedSong.coverUrl AS coverUrl " +
                             "ORDER BY recommendationStrength DESC " +
                             "LIMIT 10";

        return Song_Neo4j.getSongNeo4js(username, cypherQuery, neo4jClient);
    }
}
