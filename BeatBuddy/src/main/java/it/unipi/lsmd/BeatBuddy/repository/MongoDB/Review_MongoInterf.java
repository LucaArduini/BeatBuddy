package it.unipi.lsmd.BeatBuddy.repository.MongoDB;

import it.unipi.lsmd.BeatBuddy.model.Review;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface Review_MongoInterf extends MongoRepository<Review, String> {
    // Ricorda che in Review abbiamo il tipo di dato ObjectId per albumID
    // quindi ricorda di convertire l'albumID in ObjectId prima di passarlo
    boolean existsByAlbumIDAndUsername(ObjectId albumObjectID, String username);
    Optional<Review> findByAlbumIDAndUsername(ObjectId albumObjectID, String username);
    Page<Review> findLimitedReviewsByAlbumID(ObjectId albumObjectID, Pageable pageable);

}
