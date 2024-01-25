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
public class Review_Repo {

    @Autowired
    private Review_MongoInterf review_RI_MongoDB;

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

//    public int insertReviewEverywhere(int rating, String text, String albumID, String username) {
//        try {
//            ObjectId albumObjectId = new ObjectId(albumID);
//
//            // insert della review nella collection reviews
//            if(review_RI_MongoDB.existsByAlbumIDAndUsername(albumObjectId, username))
//                return 1;
//            Review review = new Review(rating, text, albumObjectId, username, new Date());
//            review_RI_MongoDB.save(review);
//
//            // insert della review nella collection users
//            int outcome = user_Repo.insertReviewIntoUser(albumID, rating, username);
//            if(outcome != 0)
//                return 2;
//
//            // insert della review nella collection albums (aggiornamento lastReviews)
////            outcome = album_Repo.insertReviewIntoAlbum(###);
//
//
//
//            return 0;
//        } catch (DataAccessException dae) {
//            dae.printStackTrace();
//            return 2;
//        }
//    }
}
