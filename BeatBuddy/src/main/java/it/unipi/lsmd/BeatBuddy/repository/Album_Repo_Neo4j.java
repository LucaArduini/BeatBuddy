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

import java.util.List;
import org.springframework.data.neo4j.core.Neo4jClient;

@Service
public class Album_Repo_Neo4j {

    @Autowired
    private Album_Neo4jInterf albumNeo4jRepository;
    @Autowired
    private Neo4jClient neo4jClient;

    @Transactional
    public AlbumOnlyLikes[] getNewLikesForAlbums() {
        try {
            // se findNewLikesForAlbums ritorna un array vuoto,
            // allora anche questa funzione ritorna un array vuoto
            return findNewLikesForAlbums();
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return new AlbumOnlyLikes[0];
        }
    }

    @Transactional
    public SongOnlyLikes[] getNewLikesForSongs() {
        try {
            return findNewLikesForSongs();
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return new SongOnlyLikes[0];
        }
    }

    public AlbumOnlyLikes[] findNewLikesForAlbums() {
        String cypherQuery = "MATCH (a:Album) <-[l:LIKES_A]- (:User) " +
                "RETURN DISTINCT a.coverURL as coverURL, count(l) as likes";

        List<AlbumOnlyLikes> albumOnlyLikesList = (List<AlbumOnlyLikes>) neo4jClient
                .query(cypherQuery)
                // type of objects that the query results should be converted into
                .fetchAs(AlbumOnlyLikes.class)
                .mappedBy((typeSystem, record) -> {
                    String coverURL = record.get("coverURL").asString();
                    Integer likes = record.get("likes").asInt();
                    return new AlbumOnlyLikes(coverURL, likes);
                }).all();

        return albumOnlyLikesList.toArray(new AlbumOnlyLikes[0]);
    }

    public SongOnlyLikes[] findNewLikesForSongs() {
        String cypherQuery = "MATCH (s:Song) <-[l:LIKES_S]- (:User) " +
//                             "WHERE date(l.timestamp) >= date() - duration({days: 1}) " +
                "WITH s, count(l) as likes " +
                "RETURN DISTINCT s.coverUrl as coverUrl, s.songName as songName, likes";

        List<SongOnlyLikes> songOnlyLikesList = (List<SongOnlyLikes>) neo4jClient
                .query(cypherQuery)
                // type of objects that the query results should be converted into
                .fetchAs(SongOnlyLikes.class)
                .mappedBy((typeSystem, record) -> {
                    String coverUrl = record.get("coverUrl").asString();
                    String songName = record.get("songName").asString();
                    Integer likes = record.get("likes").asInt();
                    return new SongOnlyLikes(coverUrl, songName, likes);
                }).all();

        return songOnlyLikesList.toArray(new SongOnlyLikes[0]);
    }

    public List<AlbumWithLikes> getAlbumsSortedByLikes_LastWeek(){
        try {
            return findAlbumsSortedByLikes_LastWeek();
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    public List<AlbumWithLikes> findAlbumsSortedByLikes_LastWeek() {
        String cypherQuery = "MATCH (a:Album) <-[r:LIKES_A]- (:User) " +
                "WHERE date(r.timestamp) >= date() - duration('P7D') " +
                "WITH a, count(r) as likes " +
                "RETURN DISTINCT a.albumName AS albumName, a.artistName AS artistName, " +
                "a.coverURL AS coverURL, likes " +
                "ORDER BY likes DESC " +
                "LIMIT 5";

        return (List<AlbumWithLikes>) neo4jClient
                .query(cypherQuery)
                .fetchAs(AlbumWithLikes.class)
                .mappedBy((typeSystem, record) -> {
                    String albumName = record.get("albumName").asString();
                    String artistName = record.get("artistName").asString();
                    String coverURL = record.get("coverURL").asString();
                    Integer likes = record.get("likes").asInt();
                    return new AlbumWithLikes(albumName, artistName, coverURL, likes);
                }).all();
    }
}
