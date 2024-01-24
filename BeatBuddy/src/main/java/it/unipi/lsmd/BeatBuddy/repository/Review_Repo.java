package it.unipi.lsmd.BeatBuddy.repository;

import it.unipi.lsmd.BeatBuddy.model.Review;
import it.unipi.lsmd.BeatBuddy.repository.MongoDB.Review_MongoInterf;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class Review_Repo {

    @Autowired
    private Review_MongoInterf review_RI_MongoDB;

//    Pageable topFive = PageRequest.of(0, 5);
//        return album_RI.findFirst5ByTitleContaining(term, topFive);

    public List<Review> getReviewsByAlbumID(String albumID) {
        try {
            ObjectId albumObjectId = new ObjectId(albumID);
            // Imposto il limite di 300 reviews per pagina
            PageRequest pageable = PageRequest.of(0, 300);
            System.out.println("nella repo: " + review_RI_MongoDB.findByAlbumID(albumObjectId, pageable).getContent());
            return review_RI_MongoDB.findByAlbumID(albumObjectId, pageable).getContent();
        } catch (DataAccessException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
    public int insertReview(int rating, String text, String albumID, String username) {
        try {
            ObjectId albumObjectId = new ObjectId(albumID);
            if(review_RI_MongoDB.existsByAlbumIDAndUsername(albumObjectId, username))
                return 1;
//            Review review = new Review(null, rating, text, albumID, username, ###);
//            review_RI_MongoDB.save(review);
            return 0;
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            return 1;
        }
    }
}
