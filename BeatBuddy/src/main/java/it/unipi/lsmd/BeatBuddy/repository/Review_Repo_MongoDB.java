package it.unipi.lsmd.BeatBuddy.repository;

import it.unipi.lsmd.BeatBuddy.model.Review;
import it.unipi.lsmd.BeatBuddy.repository.MongoDB.Review_MongoInterf;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class Review_Repo_MongoDB {

    @Autowired
    private Review_MongoInterf review_RI_MongoDB;
    @Autowired
    private MongoTemplate mongoTemplate;

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

    public int getNumberOfDailyReviews() {
        try {
            Date twentyFourHoursAgo = new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000));

            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("date").gte(twentyFourHoursAgo)), 
                    Aggregation.group().count().as("totalReviews")
            );

            AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "reviews", Map.class);
            Map<String, Integer> result = results.getUniqueMappedResult();

            return (result != null) ? result.get("totalReviews") : 0;

        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException) {
                throw dae;
            }
            dae.printStackTrace();
            return -1;
        }
    }
}
