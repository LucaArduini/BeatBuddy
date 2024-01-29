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
    private String albumName;
    private String[] artistsArray;
    private String songName;
    private Integer likes;

    public SongOnlyLikes(String albumName, String artistsString, String songName, Integer likes) {
        this.albumName = albumName;
        this.artistsArray = artistsString.split(", ");
        this.songName = songName;
        this.likes = likes;
    }

    public static ArrayList<SongOnlyLikes> getSongOnlyLikes(String cypherQuery, Neo4jClient neo4jClient) {
        return (ArrayList<SongOnlyLikes>) neo4jClient
                .query(cypherQuery)
                .fetchAs(SongOnlyLikes.class)
                .mappedBy((typeSystem, record) -> {
                    String albumName = record.get("albumName").asString();
                    String artistsString = record.get("artistsString").asString();
                    String songName = record.get("songName").asString();
                    Integer likes = record.get("likes").asInt();
                    return new SongOnlyLikes(albumName, artistsString, songName, likes);
                }).all();
    }
}
