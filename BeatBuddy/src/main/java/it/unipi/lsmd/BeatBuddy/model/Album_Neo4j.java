package it.unipi.lsmd.BeatBuddy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.GeneratedValue;

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
        this.artists = artists;
        this.coverURL = coverURL;
    }
}