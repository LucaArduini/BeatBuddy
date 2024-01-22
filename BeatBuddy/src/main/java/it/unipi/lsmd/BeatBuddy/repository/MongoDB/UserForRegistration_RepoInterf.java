package it.unipi.lsmd.BeatBuddy.repository.MongoDB;

import it.unipi.lsmd.BeatBuddy.model.UserForRegistration;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserForRegistration_RepoInterf extends MongoRepository<UserForRegistration, String> {
    ;
}