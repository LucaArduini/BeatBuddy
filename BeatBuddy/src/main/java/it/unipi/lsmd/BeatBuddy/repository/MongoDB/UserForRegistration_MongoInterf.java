package it.unipi.lsmd.BeatBuddy.repository.MongoDB;

import it.unipi.lsmd.BeatBuddy.model.UserForRegistration;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserForRegistration_MongoInterf extends MongoRepository<UserForRegistration, String> {
    ;
    // NON CANCELLARE QUESTA CLASSE
    // anche se qui non sono riportate funzioni, il programma usa funzioni di questa classe
    // iniettate (automaticamente) da Spring
}