package it.unipi.lsmd.BeatBuddy.repository.Neo4j;

import it.unipi.lsmd.BeatBuddy.model.Album_Neo4j;
import it.unipi.lsmd.BeatBuddy.model.dummy.AlbumWithLikes;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface Album_Neo4jInterf extends Neo4jRepository<Album_Neo4j, String> {

    @Query("MATCH (a:Album) WHERE a.albumName =~ $albumName RETURN a")
    Album_Neo4j getAlbumByName(String albumName);
}
