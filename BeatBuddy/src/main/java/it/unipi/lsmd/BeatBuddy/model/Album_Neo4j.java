package it.unipi.lsmd.BeatBuddy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.GeneratedValue;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Node("Album")
public class Album_Neo4j {
    @Id @GeneratedValue
    private String id;

    private String albumName;
    private String artistName;
    private String coverURL;


    public Album_Neo4j(String albumName, String artists, String coverURL) {
        this.albumName = albumName;
        this.artistName = artists;
        this.coverURL = coverURL;
    }

    public static ArrayList<Album_Neo4j> getAlbumNeo4js(String username, String cypherQuery, Neo4jClient neo4jClient) {
        return (ArrayList<Album_Neo4j>) neo4jClient
                .query(cypherQuery)
                .bind(username).to("username")
                .fetchAs(Album_Neo4j.class)
                .mappedBy((typeSystem, record) -> {
                    String albumName = record.get("albumName").asString();
                    String artistName = record.get("artistName").asString();
                    String coverURL = record.get("coverURL").asString();
                    return new Album_Neo4j(albumName, artistName, coverURL);
                }).all();
    }
}