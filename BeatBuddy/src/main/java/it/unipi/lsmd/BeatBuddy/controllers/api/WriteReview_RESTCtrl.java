package it.unipi.lsmd.BeatBuddy.controllers.api;

import it.unipi.lsmd.BeatBuddy.model.Album;
import it.unipi.lsmd.BeatBuddy.model.ReviewedAlbum;
import it.unipi.lsmd.BeatBuddy.repository.Album_Repo_MongoDB;
import it.unipi.lsmd.BeatBuddy.repository.Review_Repo_MongoDB;
import it.unipi.lsmd.BeatBuddy.repository.User_Repo_MongoDB;
import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WriteReview_RESTCtrl {

    @Autowired
    Review_Repo_MongoDB review_RepoNeo4j;
    @Autowired
    Album_Repo_MongoDB album_RepoMongoDB;
    @Autowired
    User_Repo_MongoDB user_Repo_Mongo;

    /**
     * Handles the writing of a user review for an album.
     * This method validates the review data and inserts it into the database collections.
     *
     * @param session The HttpSession to check if the user is logged in.
     * @param rating The rating provided by the user (between 1 and 5).
     * @param text The text of the review.
     * @param albumID The unique identifier of the album being reviewed.
     * @param username The username of the user submitting the review.
     * @return A JSON string containing an outcome code (0 for success, non-zero for errors).
     */
    @PostMapping("/api/writeReview")
    @Transactional
    public @ResponseBody String writeReview(HttpSession session,
                                      @RequestParam("rating") int rating,
                                      @RequestParam("text") String text,
                                      @RequestParam("albumID") String albumID,
                                      @RequestParam("username") String username) {

        try {
            if(!Utility.isLogged(session))
                return "{\"outcome_code\": 1}";     // User not logged in
            if(!Utility.getUsername(session).equals(username))
                return "{\"outcome_code\": 2}";     // Usernames don't match
            if(!album_RepoMongoDB.existsById(albumID))
                return "{\"outcome_code\": 3}";     // Album doesn't exist
            if(rating < 1 || rating > 5)
                return "{\"outcome_code\": 4}";     // Rating out of range
            if(review_RepoNeo4j.existsByAlbumIDAndUsername(albumID, username))
                return "{\"outcome_code\": 5}";     // User has already written a review for this album


            Album album = album_RepoMongoDB.getAlbumById(albumID);
            if(album == null)
                return "{\"outcome_code\": 3}";     // Album doesn't exist

        // insert della review nella collection reviews
            boolean outcomeInsertIntoReview = review_RepoNeo4j.insertReview(rating, text, new ObjectId(album.getId()), username);
            if(!outcomeInsertIntoReview)
                return "{\"outcome_code\": 6}";     // Error while writing the review into the collection reviews

        // insert della review nella collection users (aggiornamento reviewedAlbums)
            ReviewedAlbum reviewedAlbum = new ReviewedAlbum(album.getTitle(), album.getCoverURL(), album.getArtistsAsString(), rating);

            boolean outcomeInsertIntoUser = user_Repo_Mongo.insertReviewIntoUser(username, reviewedAlbum);
            if(!outcomeInsertIntoUser)
                return "{\"outcome_code\": 7}";     // Error while writing the review into the collection users

        // insert della review nella collection albums (aggiornamento lastReviews)
            int outcomeInsertIntoAlbum = album_RepoMongoDB.insertReviewIntoAlbum(album.getId(), username, rating, text);
            if (outcomeInsertIntoAlbum == 1) {
                return "{\"outcome_code\": 8}";     // Album not found
            } else if (outcomeInsertIntoAlbum == 2) {
                return "{\"outcome_code\": 9}";     // Violation of uniqueness constraint
            } else if (outcomeInsertIntoAlbum == 3) {
                return "{\"outcome_code\": 10}";     // Violation of data integrity
            } else if (outcomeInsertIntoAlbum == 4) {
                return "{\"outcome_code\": 11}";     // Other exceptions related to data access
            }

        // Se tutto è andato a buon fine, ritorna un json con outcome_code = 0
            return "{\"outcome_code\": 0}";         // Review successfully written

        } catch (DataAccessResourceFailureException e) {
            e.printStackTrace();
            return "{\"outcome_code\": 12}";         // Error while connecting to the database
        }
    }
}
