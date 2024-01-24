package it.unipi.lsmd.BeatBuddy.repository.Neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import it.unipi.lsmd.BeatBuddy.model.User;
import org.springframework.data.neo4j.repository.query.Query;

public interface User_Neo4jInterf extends Neo4jRepository<User, String> {

    @Query("MATCH (u1:User {username: $user1}), (u2:User {username: $user2}) " +
            "MERGE (u1)-[:FOLLOW]->(u2)")
    void addFollow(String user1, String user2);

    @Query("MATCH (u1:User {username: $user1})-[r:FOLLOW]->(u2:User {username: $user2}) " +
            "DELETE r")
    void removeFollow(String user1, String user2);

    @Query("MATCH (u:User {username: $username}), (a:Album {coverURL: $coverURL}) " +
            "MERGE (u)-[:LIKES_A]->(a)")
    void addLikes_A(String username, String coverURL);

    @Query("MATCH (u:User {username: $username})-[r:LIKES_A]->(a:Album {coverURL: $coverURL}) " +
            "DELETE r")
    void removeLikes_A(String username, String coverURL);

    @Query("MATCH (u:User {username: $username}), (s:Song {title: $title, albumName: $albumName, artists: $artists}) " +
            "MERGE (u)-[:LIKES_S]->(s)")
    void addLikes_S(String username, String title, String albumName, String artists);

    @Query("MATCH (u:User {username: $username})-[r:LIKES_S]->(s:Song {title: $title, albumName: $albumName, artists: $artists}) " +
            "DELETE r")
    void removeLikes_S(String username, String title, String albumName, String artists);
}
