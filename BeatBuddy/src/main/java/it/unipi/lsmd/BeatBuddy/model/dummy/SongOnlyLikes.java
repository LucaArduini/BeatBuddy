package it.unipi.lsmd.BeatBuddy.model.dummy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jClient;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongOnlyLikes {
    private String coverUrl;
    private String songName;
    private Integer likes;

    public static ArrayList<SongOnlyLikes> getSongOnlyLikes(String cypherQuery, Neo4jClient neo4jClient) {
        return (ArrayList<SongOnlyLikes>) neo4jClient
                .query(cypherQuery)
                .fetchAs(SongOnlyLikes.class)
                .mappedBy((typeSystem, record) -> {
                    String coverUrl = record.get("coverUrl").asString();
                    String songName = record.get("songName").asString();
                    Integer likes = record.get("likes").asInt();
                    return new SongOnlyLikes(coverUrl, songName, likes);
                }).all();
    }
}
