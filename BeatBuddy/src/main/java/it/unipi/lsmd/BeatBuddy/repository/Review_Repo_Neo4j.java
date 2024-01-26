package it.unipi.lsmd.BeatBuddy.repository;

import it.unipi.lsmd.BeatBuddy.model.Review;
import it.unipi.lsmd.BeatBuddy.repository.MongoDB.Review_MongoInterf;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class Review_Repo_Neo4j {

    @Autowired
    private Review_MongoInterf review_RI_MongoDB;

    public boolean existsByAlbumIDAndUsername(String albumID, String username) {
        try {
            return review_RI_MongoDB.existsByAlbumIDAndUsername(new ObjectId(albumID) , username);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }

    public boolean insertReview(int rating, String text, ObjectId albumObjectId, String username) {
        try {
            Review review = new Review(rating, text, albumObjectId, username, new Date());
            review_RI_MongoDB.save(review);
            return true;
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }

    public List<Review> getReviewsByAlbumID(String albumID) {
        try {
            ObjectId albumObjectId = new ObjectId(albumID);
            // Imposto il limite di 300 reviews per pagina
            PageRequest pageable = PageRequest.of(0, 300);
            return review_RI_MongoDB.findLimitedReviewsByAlbumID(albumObjectId, pageable).getContent();
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    public Review getReviewByAlbumIDAndUsername(String albumID, String username) {
        try {
            ObjectId albumObjectId = new ObjectId(albumID);
            Optional<Review> result = review_RI_MongoDB.findByAlbumIDAndUsername(albumObjectId, username);
            return result.orElse(null);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }
}
