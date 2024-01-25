//package it.unipi.lsmd.BeatBuddy.repository.Neo4j;
//
//
//
//
////USER(username)
////SONG(title, albumName, artists, coverUrl) #artists->artistName, e viene inserito solo il primo artist
////ALBUM(name, artists, coverUrl)
////
////# RELATIONSHIPS
////FOLLOW(User, User)
////LIKES_A(User, Song)
////LIKES_S(User, Album)
////PARTOF(Song, Album) (per ora non lo mettiamo)
//
//import org.neo4j.driver.Driver;
//import org.neo4j.driver.Session;
//import org.neo4j.driver.Transaction;
//import org.neo4j.driver.Values;
//
//public class Neo4j_Repo {
//    private final Driver driver;
//
//    public Neo4j_Repo(Driver driver) {
//        this.driver = driver;
//    }
//
//    public void addFriendship(String user1, String user2) {
//        try (Session session = driver.session()) {
//            // Start a new transaction
//            try (Transaction tx = session.beginTransaction()) {
//                // Define the Cypher query to create a FRIENDS_WITH relationship
//                String cypherQuery = "MATCH (u1:User {username: $user1}), (u2:User {username: $user2}) " +
//                        "CREATE (u1)-[:FRIENDS_WITH]->(u2)";
//
//                // Execute the query with parameters
//                tx.run(cypherQuery, Values.parameters("user1", user1, "user2", user2));
//
//                // Commit the transaction
//                tx.commit();
//            }
//        }
//    }
//}