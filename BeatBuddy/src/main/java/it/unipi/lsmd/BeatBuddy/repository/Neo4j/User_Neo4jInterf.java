package it.unipi.lsmd.BeatBuddy.repository.Neo4j;

import it.unipi.lsmd.BeatBuddy.model.User_Neo4j;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;


public interface User_Neo4jInterf extends Neo4jRepository<User_Neo4j, String> {

    @Query("CREATE (u:User {username: $username})")
    void createUser(String username);

    // cancella l'utente e tutte le relazioni associate ad esso
    @Query("MATCH (u:User {username: $username})-[r]-() DELETE u, r")
    void deleteUser(String username);

    @Query("MATCH (u1:User {username: $user1}), (u2:User {username: $user2}) " +
            "MERGE (u1)-[:FOLLOW]->(u2)")
    void addFollow(String user1, String user2);

    @Query("MATCH (u1:User {username: $user1})-[r:FOLLOW]->(u2:User {username: $user2}) " +
            "DELETE r")
    void removeFollow(String user1, String user2);

    @Query("MATCH (u:User {username: $username}), (a:Album {coverURL: $coverURL}) " +
            "MERGE (u)-[l:LIKES_A {timestamp: datetime()}]->(a)")
    void addLikes_A(String username, String coverURL);

    @Query("MATCH (u:User {username: $username})-[r:LIKES_A]->(a:Album {coverURL: $coverURL}) " +
            "DELETE r")
    void removeLikes_A(String username, String coverURL);

    @Query("MATCH (u:User {username: $username}), (s:Song {songName: $songName, coverUrl: $coverUrl}) " +
            "MERGE (u)-[l:LIKES_S {timestamp: datetime()}]->(s) ")
    void addLikes_S(String username, String songName, String coverUrl);

    @Query("MATCH (u:User {username: $username})-[r:LIKES_S]->(s:Song {songName: $songName, coverUrl: $coverUrl}) " +
            "DELETE r")
    void removeLikes_S(String username, String songName, String coverUrl);


//    FUNZIONI PER DEBUG
    @Query("MATCH (u1:User {username: $user1}), (u2:User {username: $user2}) " +
            "MERGE (u1)-[:FOLLOW]->(u2) " +
            "RETURN count(u1) > 0")
    boolean Boolean_addFollow(String user1, String user2);

    @Query("MATCH (u1:User {username: $user1})-[r:FOLLOW]->(u2:User {username: $user2}) " +
            "DELETE r " +
            "RETURN count(r) > 0")
    boolean Boolean_removeFollow(String user1, String user2);

    @Query("MATCH (u:User {username: $username}), (a:Album {coverURL: $coverURL}) " +
            "MERGE (u)-[l:LIKES_A {timestamp: datetime()}]->(a) " +
            "RETURN count(u) > 0")
    boolean Boolean_addLikes_A(String username, String coverURL);

    @Query("MATCH (u:User {username: $username})-[r:LIKES_A]->(a:Album {coverURL: $coverURL}) " +
            "DELETE r " +
            "RETURN count(r) > 0")
    boolean Boolean_removeLikes_A(String username, String coverURL);

    @Query("MATCH (u:User {username: $username}), (s:Song {songName: $songName, coverUrl: $coverUrl}) " +
            "MERGE (u)-[l:LIKES_S {timestamp: datetime()}]->(s) " +
            "RETURN count(u) > 0")
    boolean Boolean_addLikes_S(String username, String songName, String coverUrl);

    @Query("MATCH (u:User {username: $username})-[r:LIKES_S]->(s:Song {songName: $songName, coverUrl: $coverUrl}) " +
            "DELETE r " +
            "RETURN count(r) > 0")
    boolean Boolean_removeLikes_S(String username, String songName, String coverUrl);
}