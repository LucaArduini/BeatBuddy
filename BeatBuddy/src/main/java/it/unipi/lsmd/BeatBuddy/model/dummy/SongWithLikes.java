package it.unipi.lsmd.BeatBuddy.model.dummy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jClient;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongWithLikes {
    private String songName;
    private String albumName;
    private String artistName;
    private String coverUrl;
    private Integer likes;

    public static List<SongWithLikes> getSongWithLikes(String cypherQuery, Neo4jClient neo4jClient){
        return (List<SongWithLikes>) neo4jClient.query(cypherQuery)
                .fetchAs(SongWithLikes.class)
                .mappedBy((typeSystem, record) -> new SongWithLikes(
                        record.get("songName").asString(),
                        record.get("albumName").asString(),
                        record.get("artistName").asString(),
                        record.get("coverUrl").asString(),
                        record.get("likes").asInt()
                ))
                .all();
    }
}

