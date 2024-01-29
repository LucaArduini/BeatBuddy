package it.unipi.lsmd.BeatBuddy.repository;

import it.unipi.lsmd.BeatBuddy.model.dummy.AlbumOnlyLikes;
import it.unipi.lsmd.BeatBuddy.model.dummy.AlbumWithLikes;
import it.unipi.lsmd.BeatBuddy.model.dummy.SongOnlyLikes;
import it.unipi.lsmd.BeatBuddy.repository.Neo4j.Album_Neo4jInterf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.neo4j.core.Neo4jClient;

@Service
public class Album_Repo_Neo4j {

    @Autowired
    private Album_Neo4jInterf albumNeo4jRepository;
    @Autowired
    private Neo4jClient neo4jClient;

    public int getNumberOfDailyLikesOnAlbums() {
        try {
            return findNumberOfDailyLikesOnAlbums();
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return -1;
        }
    }

    private int findNumberOfDailyLikesOnAlbums() {
        String cypherQuery = "MATCH (a:Album) <-[r:LIKES_A]- (:User) " +
                "WHERE date(r.timestamp) >= date() - duration({days: 1}) " +
                "RETURN COUNT(r) AS likes";

        return neo4jClient.query(cypherQuery).fetchAs(Integer.class).one().get();
    }

    public int getNumberOfDailyLikesOnSongs() {
        try {
            return findNumberOfDailyLikesOnSongs();
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return -1;
        }
    }

    private int findNumberOfDailyLikesOnSongs() {
        String cypherQuery = "MATCH (s:Song) <-[r:LIKES_S]- (:User) " +
                "WHERE date(r.timestamp) >= date() - duration({days: 1}) " +
                "RETURN COUNT(r) AS likes";

        return neo4jClient.query(cypherQuery).fetchAs(Integer.class).one().get();
    }

    public ArrayList<AlbumOnlyLikes> getNewLikesForAlbums() {
        try {
            // se findNewLikesForAlbums ritorna un array vuoto,
            // allora anche questa funzione ritorna un array vuoto
            return findNewLikesForAlbums();
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return new ArrayList<AlbumOnlyLikes>();
        }
    }

    private ArrayList<AlbumOnlyLikes> findNewLikesForAlbums() {
        String cypherQuery = "MATCH (a:Album) <-[r:LIKES_A]- (:User) " +
                "WHERE date(r.timestamp) >= date() - duration({days: 1}) " +
                "MATCH (a) <-[l:LIKES_A]- (:User) " +
                "WITH a, COUNT(l) AS likes " +
                "RETURN a.albumName AS albumName, a.artistName AS artistsString, likes";

        return AlbumOnlyLikes.getAlbumOnlyLikes(cypherQuery, neo4jClient);
    }

    public ArrayList<SongOnlyLikes> getNewLikesForSongs() {
        try {
            return findNewLikesForSongs();
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return new ArrayList<SongOnlyLikes>();
        }
    }

    private ArrayList<SongOnlyLikes> findNewLikesForSongs() {
        String cypherQuery = "MATCH (s:Song) <-[r:LIKES_S]- (:User) " +
                             "WHERE date(r.timestamp) >= date() - duration({days: 1}) " +
                             "MATCH (s) <-[l:LIKES_S]- (:User) " +
                             "WITH s, COUNT(l) AS likes " +
                             "RETURN s.albumName AS albumName, s.artistName AS artistsString, s.songName as songName, likes";

        return SongOnlyLikes.getSongOnlyLikes(cypherQuery, neo4jClient);
    }

    public List<AlbumWithLikes> getAlbumsByLikes_LastWeek() {
        String cypherQuery = "MATCH (a:Album) <-[r:LIKES_A]- (:User) " +
                "WHERE date(r.timestamp) >= date() - duration('P7D') " +
                "WITH a, count(r) as likes " +
                "RETURN DISTINCT a.albumName AS albumName, a.artistName AS artistName, " +
                "a.coverURL AS coverURL, likes " +
                "ORDER BY likes DESC " +
                "LIMIT 10";

        return AlbumWithLikes.getAlbumWithLikes(cypherQuery, neo4jClient);
    }
}
