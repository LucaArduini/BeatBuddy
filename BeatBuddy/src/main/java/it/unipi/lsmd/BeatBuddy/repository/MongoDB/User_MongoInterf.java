package it.unipi.lsmd.BeatBuddy.repository.MongoDB;

import it.unipi.lsmd.BeatBuddy.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.Optional;

public interface User_MongoInterf extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("{ $or: [ { 'email': ?0 }, { 'username': ?1 } ] }")
    Optional<User> findByEmailOrUsername(String email, String username);
}
