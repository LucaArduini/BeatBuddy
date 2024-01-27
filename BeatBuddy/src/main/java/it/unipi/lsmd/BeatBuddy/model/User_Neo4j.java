package it.unipi.lsmd.BeatBuddy.model;

import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Node("User")
public class User_Neo4j {
    @Id @GeneratedValue
    private String id;

    private String username;

    public User_Neo4j(String username) {
        this.username = username;
    }

    public static ArrayList<User_Neo4j> getUserNeo4js(String username, String cypherQuery, Neo4jClient neo4jClient) {
        return (ArrayList<User_Neo4j>) neo4jClient
                .query(cypherQuery)
                .bind(username).to("username")
                .fetchAs(User_Neo4j.class)
                .mappedBy((typeSystem, record) -> {
                    String username1 = record.get("username").asString();
                    return new User_Neo4j(username1);
                }).all();
    }
}