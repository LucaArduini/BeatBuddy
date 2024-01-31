package it.unipi.lsmd.BeatBuddy.repository.Neo4j;

import it.unipi.lsmd.BeatBuddy.model.User_Neo4j;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;


public interface User_Neo4jInterf extends Neo4jRepository<User_Neo4j, String> {

    @Query("MERGE (u:User {username: $username})")
    void createUser(String username);

    // cancella l'utente e tutte le relazioni associate ad esso
    @Query("MATCH (u:User {username: $username})-[r]-() " +
            "DELETE u, r")
    void deleteUser(String username);

    @Query("MATCH (u1:User {username: $user1}), (u2:User {username: $user2}) " +
            "OPTIONAL MATCH (u1)-[f:FOLLOW]->(u2) " +
            "WITH u1, u2, f, CASE WHEN f IS NULL THEN 'CREATED' ELSE 'EXISTING' END AS status " +
            "MERGE (u1)-[:FOLLOW]->(u2) " +
            "RETURN status")
    String addFollow(String user1, String user2);

    @Query("MATCH (u1:User {username: $user1})-[r:FOLLOW]->(u2:User {username: $user2}) " +
            "DELETE r")
    void removeFollow(String user1, String user2);
    
    @Query("MATCH (u:User {username: $username}), (a:Album {albumName: $albumName, artistName: $artistName}) " +
            "MERGE (u)-[l:LIKES_A]->(a) " +
            "WITH u, l, a, CASE WHEN l.timestamp IS NULL THEN true ELSE false END AS isNew " +
            "LIMIT(1)" +
            "SET l.timestamp = datetime() " +
            "RETURN CASE WHEN isNew = true THEN 'CREATED' ELSE 'EXISTING' END")
    String addLikes_A(String username, String albumName, String artistName);

    @Query("MATCH (u:User {username: $username})-[r:LIKES_A]->(a:Album {albumName: $albumName, artistName: $artistName}) " +
            "DELETE r")
    void removeLikes_A(String username, String albumName, String artistName);

    @Query("MATCH (u:User {username: $username}), (s:Song {albumName: $albumName, artistName: $artistName, songName: $songName}) " +
            "MERGE (u)-[l:LIKES_S]->(s) " +
            "WITH u, l, s, CASE WHEN l.timestamp IS NULL THEN true ELSE false END AS isNew " +
            "LIMIT (1)" +
            "SET l.timestamp = datetime() " +
            "RETURN CASE WHEN isNew = true THEN 'CREATED' ELSE 'EXISTING' END")
    String addLikes_S(String username, String albumName, String artistName, String songName);


    @Query("MATCH (u:User {username: $username})-[r:LIKES_S]->(s:Song {albumName: $albumName, artistName: $artistName, songName: $songName}) " +
            "DELETE r")
    void removeLikes_S(String username, String albumName, String artistName, String songName);

    @Query("MATCH (u:User {username: $username})-[:FOLLOW]->(followed) " +
            "RETURN COUNT(followed)")
    int countFollowedUsers(String username);

    @Query("MATCH (u:User {username: $username})-[:FOLLOW]->(followed:User) " +
            "RETURN followed")
    List<User_Neo4j> findFollowedUsersByUsername(String username);
}