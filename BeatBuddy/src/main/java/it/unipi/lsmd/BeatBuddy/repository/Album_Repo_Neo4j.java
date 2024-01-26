package it.unipi.lsmd.BeatBuddy.repository;

import it.unipi.lsmd.BeatBuddy.model.dummy.AlbumLikes;
import it.unipi.lsmd.BeatBuddy.model.dummy.SongLikes;
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
    public AlbumLikes[] getNewLikesForAlbums() {
        try {
            // se findNewLikesForAlbums ritorna un array vuoto,
            // allora anche questa funzione ritorna un array vuoto
            return findNewLikesForAlbums();
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return new AlbumLikes[0];
        }
    }

    @Transactional
    public SongLikes[] getNewLikesForSongs() {
        try {
            return findNewLikesForSongs();
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return new SongLikes[0];
        }
    }

    public AlbumLikes[] findNewLikesForAlbums() {
        String cypherQuery = "MATCH (a:Album) <-[l:LIKES_A]- (:User) " +
                "RETURN DISTINCT a.coverURL as coverURL, count(l) as likes";

        List<AlbumLikes> albumLikesList = (List<AlbumLikes>) neo4jClient
                .query(cypherQuery)
                // type of objects that the query results should be converted into
                .fetchAs(AlbumLikes.class)
                .mappedBy((typeSystem, record) -> {
                    String coverURL = record.get("coverURL").asString();
                    Integer likes = record.get("likes").asInt();
                    return new AlbumLikes(coverURL, likes);
                }).all();

        return albumLikesList.toArray(new AlbumLikes[0]);
    }

    public SongLikes[] findNewLikesForSongs() {
        String cypherQuery = "MATCH (s:Song) <-[l:LIKES_S]- (:User) " +
//                             "WHERE date(l.timestamp) >= date() - duration({days: 1}) " +
                "WITH s, count(l) as likes " +
                "RETURN DISTINCT s.coverUrl as coverUrl, s.songName as songName, likes";

        List<SongLikes> songLikesList = (List<SongLikes>) neo4jClient
                .query(cypherQuery)
                // type of objects that the query results should be converted into
                .fetchAs(SongLikes.class)
                .mappedBy((typeSystem, record) -> {
                    String coverUrl = record.get("coverUrl").asString();
                    String songName = record.get("songName").asString();
                    Integer likes = record.get("likes").asInt();
                    return new SongLikes(coverUrl, songName, likes);
                }).all();

        return songLikesList.toArray(new SongLikes[0]);
    }
}
