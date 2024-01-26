package it.unipi.lsmd.BeatBuddy.repository.Neo4j;

import it.unipi.lsmd.BeatBuddy.model.Song_Neo4j;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface Song_Neo4jInterf extends Neo4jRepository<Song_Neo4j, String> {
    ;
}
