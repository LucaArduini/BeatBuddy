//package it.unipi.lsmd.BeatBuddy.controllers.api;
//
//import it.unipi.lsmd.BeatBuddy.repository.Review_Repo;
//import jakarta.servlet.http.HttpSession;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataAccessResourceFailureException;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class WriteReview_RESTCtrl {
//    private static final Logger logger = LoggerFactory.getLogger(WriteReview_RESTCtrl.class);
//
//    @Autowired
//    Review_Repo review_Repo;
//
//    @PostMapping("/api/writeReview")
//    public @ResponseBody String login(HttpSession session,
//                                      @RequestParam("rating") int rating,
//                                      @RequestParam("text") String text,
//                                      @RequestParam("albumID") String albumID,
//                                      @RequestParam("username") String username) {
//        logger.info("Write review attempt from user: " + username);
//
//        try {
//            int outcome_come = review_Repo.insertReviewEverywhere(rating, text, albumID, username);
//
//            if(outcome_come == 0)
//                return "{\"outcome_code\": 0}";     // Review written successfully
//            else if(outcome_come == 1)
//                return "{\"outcome_code\": 1}";     // User already wrote a review for this album
//            else
//                return "{\"outcome_code\": 2}";     // Error while writing the review
//
//        } catch (DataAccessResourceFailureException e) {
//            logger.error("Impossibile connettersi al database", e);
//            return "{\"outcome_code\": 3}";         // Error while connecting to the database
//        }
//    }
//}
