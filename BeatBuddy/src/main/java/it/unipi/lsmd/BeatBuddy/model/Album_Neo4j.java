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
    private String artists;
    private String coverURL;
}