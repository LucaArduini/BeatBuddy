package it.unipi.lsmd.BeatBuddy.model.dummy;

import lombok.*;
import org.springframework.data.neo4j.core.Neo4jClient;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumOnlyLikes {
    private String coverURL;
    private Integer likes;

    public static ArrayList<AlbumOnlyLikes> getAlbumOnlyLikes(String cypherQuery, Neo4jClient neo4jClient) {
        return (ArrayList<AlbumOnlyLikes>) neo4jClient
                .query(cypherQuery)
                .fetchAs(AlbumOnlyLikes.class)
                .mappedBy((typeSystem, record) -> {
                    String coverURL = record.get("coverURL").asString();
                    Integer likes = record.get("likes").asInt();
                    return new AlbumOnlyLikes(coverURL, likes);
                }).all();
    }
}
