package it.unipi.lsmd.BeatBuddy.repository.MongoDB;

import it.unipi.lsmd.BeatBuddy.model.Review;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Pageable;

public interface Review_MongoInterf extends MongoRepository<Review, String> {
    // Ricorda che in Review abbiamo il tipo di dato ObjectId per albumID
    // quindi ricorda di convertire l'albumID in ObjectId prima di passarlo
    boolean existsByAlbumIDAndUsername(ObjectId albumID, String username);
    Page<Review> findByAlbumID(ObjectId albumID, Pageable pageable);
}
