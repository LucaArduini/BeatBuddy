package it.unipi.lsmd.BeatBuddy.repository.MongoDB;

import it.unipi.lsmd.BeatBuddy.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface Review_MongoInterf extends MongoRepository<Review, String> {
    boolean existsByAlbumIDAndUsername(String albumID, String username);
    Page<Review> findByAlbumID(String albumID, Pageable pageable);
}
