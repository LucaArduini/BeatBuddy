package it.unipi.lsmd.BeatBuddy.model.dummy;

import lombok.*;
import org.springframework.data.neo4j.core.Neo4jClient;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumOnlyLikes {
    private String albumName;
    private String[] artistsArray;
    private Integer likes;

    public AlbumOnlyLikes(String albumName, String artistsString, Integer likes) {
        this.albumName = albumName;
        this.artistsArray = artistsString.split(", ");
        this.likes = likes;
    }

    public static ArrayList<AlbumOnlyLikes> getAlbumOnlyLikes(String cypherQuery, Neo4jClient neo4jClient) {
        return (ArrayList<AlbumOnlyLikes>) neo4jClient
                .query(cypherQuery)
                .fetchAs(AlbumOnlyLikes.class)
                .mappedBy((typeSystem, record) -> {
                    String albumName = record.get("albumName").asString();
                    String artistsString = record.get("artistsString").asString();
                    Integer likes = record.get("likes").asInt();
                    return new AlbumOnlyLikes(albumName, artistsString, likes);
                }).all();
    }
}
