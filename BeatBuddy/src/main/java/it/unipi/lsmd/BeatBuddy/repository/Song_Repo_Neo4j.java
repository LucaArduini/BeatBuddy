package it.unipi.lsmd.BeatBuddy.repository;

import it.unipi.lsmd.BeatBuddy.model.dummy.SongWithLikes;
import it.unipi.lsmd.BeatBuddy.repository.Neo4j.Song_Neo4jInterf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

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
    public List<SongWithLikes> findSongsSortedByLikes_LastWeek() {
        String cypherQuery = "MATCH (s:Song) <-[r:LIKES_S]- (:User) " +
                "WHERE date(r.timestamp) >= date() - duration('P7D') " +
                "WITH s, count(r) as likes " +
                "RETURN DISTINCT s.songName AS songName, s.albumName AS albumName, " +
                "s.artistName AS artistName, s.coverUrl AS coverUrl, likes " +
                "ORDER BY likes DESC " +
                "LIMIT 5";

        return (List<SongWithLikes>) neo4jClient
                .query(cypherQuery)
                .fetchAs(SongWithLikes.class)
                .mappedBy((typeSystem, record) -> {
                    String songName = record.get("songName").asString();
                    String albumName = record.get("albumName").asString();
                    String artistName = record.get("artistName").asString();
                    String coverUrl = record.get("coverUrl").asString();
                    Integer likes = record.get("likes").asInt();
                    return new SongWithLikes(songName, albumName, artistName, coverUrl, likes);
                }).all();
    }

}
