package it.unipi.lsmd.BeatBuddy.controllers.api;

import it.unipi.lsmd.BeatBuddy.DTO.SongDTO;
import it.unipi.lsmd.BeatBuddy.model.Album;
import it.unipi.lsmd.BeatBuddy.model.ReviewLite;
import it.unipi.lsmd.BeatBuddy.model.Song;
import it.unipi.lsmd.BeatBuddy.model.dummy.*;
import it.unipi.lsmd.BeatBuddy.repository.*;
import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.dao.DataAccessResourceFailureException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import it.unipi.lsmd.BeatBuddy.utilities.Constants;

import java.util.ArrayList;
import java.util.List;


@RestController
public class AdminPage_RESTCtrl {
    private static final Logger logger = LoggerFactory.getLogger(AdminPage_RESTCtrl.class);

    @Autowired
    private Album_Repo_MongoDB albumRepo_MongoDB;
    @Autowired
    private Artist_Repo_MongoDB artistRepo_MongoDB;
    @Autowired
    private Review_Repo_MongoDB reviewRepo_MongoDB;
    @Autowired
    private Album_Repo_Neo4j albumRepo_Neo4j;
    @Autowired
    private Song_Repo_Neo4j songRepo_Neo4j;

    @PostMapping("/api/admin/calculateAdminStats")
    public @ResponseBody String calculateAdminStats(HttpSession session){
        if(!Utility.isAdmin(session))
            return "{\"outcome_code\": 1}";     // User is not an admin

        try {
            System.out.println(">> START: Getting admin stats...");

            int dailyLikesOnAlbums = albumRepo_Neo4j.getNumberOfDailyLikesOnAlbums();
            System.out.println("> dailyLikesOnAlbums: " + dailyLikesOnAlbums);

            int dailyLikesOnSongs = albumRepo_Neo4j.getNumberOfDailyLikesOnSongs();
            System.out.println("> dailyLikesOnSongs: " + dailyLikesOnSongs);

            int dailyReviews = reviewRepo_MongoDB.getNumberOfDailyReviews();
            System.out.println("> dailyReviews: " + dailyReviews);

            AdminStats adminStats = new AdminStats(dailyLikesOnAlbums, dailyLikesOnSongs, dailyReviews);
            Utility.writeAdminStats(adminStats);

            System.out.println(">> END: Admin stats calculated successfully");

            //return the json string with the outcome code and the admin stats
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(adminStats);
            System.out.println("{\"outcome_code\": 0, \"admin_stats\": " + json + "}");
            return "{\"outcome_code\": 0, \"admin_stats\": " + json + "}";

        } catch (DataAccessResourceFailureException e) {
            e.printStackTrace();
            return "{\"outcome_code\": 10}";        // Error while connecting to the database
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"outcome_code\": 11}";        // Error while writing to file
        }
    }

    @PostMapping("/api/admin/updateNewLikes")
    public @ResponseBody String updateNewLikesAndAvgRatings(HttpSession session){
        if(!Utility.isAdmin(session))
            return "{\"outcome_code\": 1}";     // User is not an admin

        try {
            System.out.println(">> START: Updating new likes and average ratings...");

            // aggiorna i nuovi likes per gli album
            ArrayList<AlbumOnlyLikes> newLikesAlbums = albumRepo_Neo4j.getNewLikesForAlbums();
            System.out.println("> New likes found (for albums): " + newLikesAlbums.size());
            if(!newLikesAlbums.isEmpty()){
                boolean outcome1 = albumRepo_MongoDB.setLikesToAlbums(newLikesAlbums.toArray(new AlbumOnlyLikes[0]));
                if(!outcome1)
                    return "{\"outcome_code\": 2}";     // Error while updating new likes (for albums)
                else
                    System.out.println(">> New likes updated successfully (for albums)");
            }

            // aggiorna i nuovi likes per le canzoni
            ArrayList<SongOnlyLikes> newLikesSongs = albumRepo_Neo4j.getNewLikesForSongs();
            System.out.println("> New likes found (for songs): " + newLikesSongs.size());
            if(!newLikesSongs.isEmpty()){
                boolean outcome2 = albumRepo_MongoDB.setLikesToSongs(newLikesSongs.toArray(new SongOnlyLikes[0]));
                if(!outcome2)
                    return "{\"outcome_code\": 3}";     // Error while updating new likes (for songs)
                else
                    System.out.println(">> New likes updated successfully (for songs)");
            }

            // aggiorna gli avgRating degli album che hanno ricevuto nuove recensioni
            List<AlbumOnlyAvgRating> newAvgRatings = albumRepo_MongoDB.getAverageRatingForRecentReviews();
            System.out.println("> Found " + newAvgRatings.size() + " albums with recent reviews");
            if(!newAvgRatings.isEmpty()) {
                boolean outcome3 = albumRepo_MongoDB.setAverageRatingForRecentReviews(newAvgRatings);
                if (!outcome3)
                    return "{\"outcome_code\": 4}";     // Error while updating average rating
                else
                    System.out.println(">> Average rating updated successfully");
            }

            System.out.println(">> END: All new likes and average ratings updated successfully");
            return "{\"outcome_code\": 0}";         // Update successful

        } catch (DataAccessResourceFailureException e) {
            e.printStackTrace();
            return "{\"outcome_code\": 10}";        // Error while connecting to the database
        }
    }


    @PostMapping("/api/admin/calculateRankings")
    public @ResponseBody String calculateRankings_AllTime(HttpSession session){
        if(!Utility.isAdmin(session))
            return "{\"outcome_code\": 1}"; // User is not an admin

        try {
            Utility.clearRankingsDirectory(Constants.folderName_QueryResults);
        } catch (IOException e) {
            logger.error("Error while clearing rankings directory", e);
            return "{\"outcome_code\": 12}";
        }

        try {
            System.out.println(">> Calculating rankings...");


        // MongoDB Queries

            // Album
            //ok
            List<Album> rankingAlbumByRating_AllTime = albumRepo_MongoDB.getAlbumsWithMinReviewsByAvgRating_AllTime();
            if(rankingAlbumByRating_AllTime.isEmpty())
                return "{\"outcome_code\": 2}"; // No albums found (sorted by rating)
            removeUnusedFieldsFromAlbums(rankingAlbumByRating_AllTime);
            Utility.writeToFile(rankingAlbumByRating_AllTime, Constants.fileName_RankingAlbumByRating_AllTime, Constants.folderName_QueryResults);
            System.out.println("> Calcolato con successo 1: " + Constants.fileName_RankingAlbumByRating_AllTime);

            //ok
            List<Album> rankingAlbumByLikes_AllTime = albumRepo_MongoDB.getAlbumsByLikes_AllTime();
            if(rankingAlbumByLikes_AllTime.isEmpty())
                return "{\"outcome_code\": 3}"; // No albums found (sorted by likes)
            removeUnusedFieldsFromAlbums(rankingAlbumByRating_AllTime);
            Utility.writeToFile(rankingAlbumByLikes_AllTime, Constants.fileName_RankingAlbumByLikes_AllTime, Constants.folderName_QueryResults);
            System.out.println("> Calcolato con successo 2: " + Constants.fileName_RankingAlbumByLikes_AllTime);

            // Song
            //da controllare una volta aggiornati i like sulle canzoni
            //ma sembra funzionare
            List<SongDTO> rankingSongByLikes_AllTime = albumRepo_MongoDB.getSongsByLikes_AllTime();
            if(rankingSongByLikes_AllTime.isEmpty())
                return "{\"outcome_code\": 4}"; // No songs found (sorted by likes)
            Utility.writeToFile(rankingSongByLikes_AllTime, Constants.fileName_RankingSongByLikes_AllTime, Constants.folderName_QueryResults);
            System.out.println("> Calcolato con successo 3: " + Constants.fileName_RankingSongByLikes_AllTime);

            // Artist
//            // no
//            List<ArtistWithLikes> rankingArtistsByAvgRating_AllTime = artistRepo_MongoDB.getArtistsWithMinAlbumsByAvgRating_AllTime();
//            if(rankingArtistsByAvgRating_AllTime.isEmpty())
//                return "{\"outcome_code\": 5}"; // No artists found (sorted by rating)
//            Utility.writeToFile(rankingArtistsByAvgRating_AllTime, Constants.fileName_RankingArtistsByAvgRating_AllTime, Constants.folderName_QueryResults);
//            System.out.println("> Calcolato con successo 4: " + Constants.fileName_RankingArtistsByAvgRating_AllTime);
//
//            // no
//            List<ArtistWithLikes> rankingArtistsByLikes_AllTime = artistRepo_MongoDB.getArtistsByLikes_AllTime();
//            if(rankingArtistsByLikes_AllTime.isEmpty())
//                return "{\"outcome_code\": 6}"; // No artists found (sorted by likes)
//            Utility.writeToFile(rankingArtistsByLikes_AllTime, Constants.fileName_RankingArtistsByLikes_AllTime, Constants.folderName_QueryResults);
//            System.out.println("> Calcolato con successo 5: " + Constants.fileName_RankingArtistsByLikes_AllTime);


        // Neo4j Queries

            // Album
            //ok
            List<AlbumWithLikes> rankingAlbumByLikes_LastWeek = albumRepo_Neo4j.getAlbumsByLikes_LastWeek();
            if(rankingAlbumByLikes_LastWeek.isEmpty())
                return "{\"outcome_code\": 7}"; // No albums found (sorted by likes, last week)
            Utility.writeToFile(rankingAlbumByLikes_LastWeek, Constants.fileName_RankingAlbumByLikes_LastWeek, Constants.folderName_QueryResults);
            System.out.println("> Calcolato con successo 6: " + Constants.fileName_RankingAlbumByLikes_LastWeek);

            // Song
            //ok
            List<SongWithLikes> rankingSongsByLikes_LastWeek = songRepo_Neo4j.getSongsByLikes_LastWeek();
            if(rankingSongsByLikes_LastWeek.isEmpty())
                return "{\"outcome_code\": 8}"; // No songs found (sorted by likes, last week)
            Utility.writeToFile(rankingSongsByLikes_LastWeek, Constants.fileName_RankingSongByLikes_LastWeek, Constants.folderName_QueryResults);
            System.out.println("> Calcolato con successo 7: " + Constants.fileName_RankingSongByLikes_LastWeek);

            System.out.println(">>> Rankings calculated successfully");
            return "{\"outcome_code\": 0}"; // Update successful

        } catch (DataAccessResourceFailureException e) {
            e.printStackTrace();
            return "{\"outcome_code\": 10}"; // Error while connecting to the database
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"outcome_code\": 11}"; // Error while writing to file
        }
    }

    private void removeUnusedFieldsFromAlbums(List<Album> albums) {
        for(Album album : albums) {
            album.setSongs(new Song[0]);
            album.setLastReviews(new ReviewLite[0]);
            album.setYear("");
        }
    }
}
