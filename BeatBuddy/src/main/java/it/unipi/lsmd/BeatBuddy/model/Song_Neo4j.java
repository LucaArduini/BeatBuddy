package it.unipi.lsmd.BeatBuddy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Node("Song")
public class Song_Neo4j {
    @Id @GeneratedValue
    private String id;

    private String songName;
    private String albumName;
    private String artistName;
    private String coverUrl;


    public Song_Neo4j(String songName, String albumName, String artistName, String coverUrl) {
        this.songName = songName;
        this.albumName = albumName;
        this.artistName = artistName;
        this.coverUrl = coverUrl;
    }

    public static ArrayList<Song_Neo4j> getSongNeo4js(String username, String cypherQuery, Neo4jClient neo4jClient) {
        return (ArrayList<Song_Neo4j>) neo4jClient
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
    }
}