package it.unipi.lsmd.BeatBuddy.model.dummy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jClient;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumWithLikes {
    private String albumName;
    private String artistName;
    private String coverURL;
    private Integer likes;

    public static List<AlbumWithLikes> getAlbumWithLikes(String cypherQuery, Neo4jClient neo4jClient) {
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