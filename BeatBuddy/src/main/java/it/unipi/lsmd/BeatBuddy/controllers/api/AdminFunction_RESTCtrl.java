package it.unipi.lsmd.BeatBuddy.controllers.api;

import it.unipi.lsmd.BeatBuddy.DTO.SongDTO;
import it.unipi.lsmd.BeatBuddy.model.Album;
import it.unipi.lsmd.BeatBuddy.model.ReviewLite;
import it.unipi.lsmd.BeatBuddy.model.Song;
import it.unipi.lsmd.BeatBuddy.model.dummy.*;
import it.unipi.lsmd.BeatBuddy.repository.Album_Repo_MongoDB;
import it.unipi.lsmd.BeatBuddy.repository.Album_Repo_Neo4j;
import it.unipi.lsmd.BeatBuddy.repository.Artist_Repo_MongoDB;
import it.unipi.lsmd.BeatBuddy.repository.Song_Repo_Neo4j;
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

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import it.unipi.lsmd.BeatBuddy.utilities.Constants;

import java.util.List;


@RestController
public class AdminFunction_RESTCtrl {
    private static final Logger logger = LoggerFactory.getLogger(AdminFunction_RESTCtrl.class);

    @Autowired
    private Album_Repo_MongoDB albumRepo_MongoDB;
    @Autowired
    private Artist_Repo_MongoDB artistRepo_MongoDB;
    @Autowired
    private Album_Repo_Neo4j albumRepo_Neo4j;
    @Autowired
    private Song_Repo_Neo4j songRepo_Neo4j;

    //write a function that executes a query that returns the number of likes for every album and every song
    //presente in the mongoDB, recuperando queste informazioni da neo4j
    @PostMapping("/api/admin/updateNewLikes")
    public @ResponseBody String updateNewLikes(HttpSession session){
        if(!Utility.isAdmin(session))
            return "{\"outcome_code\": 1}";     // User is not an admin

        try {
            AlbumOnlyLikes[] newLikesAlbums = albumRepo_Neo4j.getNewLikesForAlbums();
            if(newLikesAlbums.length == 0)
                return "{\"outcome_code\": 2}";     // No new likes found (for albums)
            System.out.println(newLikesAlbums.length);
            boolean outcome1 = albumRepo_MongoDB.setNewLikesToAlbums(newLikesAlbums);
            if(!outcome1)
                return "{\"outcome_code\": 3}";     // Error while updating new likes

            SongOnlyLikes[] newLikesSongs = albumRepo_Neo4j.getNewLikesForSongs();
            if(newLikesSongs.length == 0)
                return "{\"outcome_code\": 4}";     // No new likes found
            boolean outcome2 = albumRepo_MongoDB.setNewLikesToSongs(newLikesSongs);
            if(!outcome2)
                return "{\"outcome_code\": 5}";     // Error while updating new likes

            return "{\"outcome_code\": 0}";         // Update successful

        } catch (DataAccessResourceFailureException e) {
            logger.error("Impossibile connettersi al database", e);
            return "{\"outcome_code\": 10}";        // Error while connecting to the database
        }
    }

    @PostMapping("/api/admin/calculateRankings")
    public @ResponseBody String calculateRankings_AllTime(HttpSession session){
        if(!Utility.isAdmin(session))
            return "{\"outcome_code\": 1}"; // User is not an admin


        ObjectMapper objectMapper = new ObjectMapper();
        try {
        // MongoDB Queries

            // Album
            List<Album> rankingAlbumByRating_AllTime = albumRepo_MongoDB.getAlbumsWithMinimumReviewsAndSortedByRating_AllTime();
            if(rankingAlbumByRating_AllTime.isEmpty())
                return "{\"outcome_code\": 2}"; // No albums found (sorted by rating)
            removeUnusedFieldsFromAlbums(rankingAlbumByRating_AllTime);
            writeToFile(rankingAlbumByRating_AllTime, Constants.fileName_RankingAlbumByRating_AllTime);
            System.out.println("> Calcolato con successo 1: " + Constants.fileName_RankingAlbumByRating_AllTime);

            List<Album> rankingAlbumByLikes_AllTime = albumRepo_MongoDB.getAlbumsSortedByLikes_AllTime();
            if(rankingAlbumByLikes_AllTime.isEmpty())
                return "{\"outcome_code\": 3}"; // No albums found (sorted by likes)
            writeToFile(rankingAlbumByLikes_AllTime, Constants.fileName_RankingAlbumByLikes_AllTime);
            System.out.println("> Calcolato con successo 2: " + Constants.fileName_RankingAlbumByLikes_AllTime);

            // Song
            List<SongDTO> rankingSongByLikes_AllTime = albumRepo_MongoDB.getSongsSortedByLikes_AllTime();
            if(rankingSongByLikes_AllTime.isEmpty())
                return "{\"outcome_code\": 4}"; // No songs found (sorted by likes)
            writeToFile(rankingSongByLikes_AllTime, Constants.fileName_RankingSongByLikes_AllTime);
            System.out.println("> Calcolato con successo 3: " + Constants.fileName_RankingSongByLikes_AllTime);

            // Artist
            List<ArtistWithLikes> rankingArtistsByAvgRating_AllTime = artistRepo_MongoDB.getArtistsWithMinAlbumsSortedByAvgRating_AllTime();
            if(rankingArtistsByAvgRating_AllTime.isEmpty())
                return "{\"outcome_code\": 5}"; // No artists found (sorted by rating)
            writeToFile(rankingArtistsByAvgRating_AllTime, Constants.fileName_RankingArtistsByAvgRating_AllTime);
            System.out.println("> Calcolato con successo 4: " + Constants.fileName_RankingArtistsByAvgRating_AllTime);

            List<ArtistWithLikes> rankingArtistsByLikes_AllTime = artistRepo_MongoDB.getArtistsSortedByLikes_AllTime();
//            if(rankingArtistsByLikes_AllTime.isEmpty())
//                return "{\"outcome_code\": 6}"; // No artists found (sorted by likes)
//            writeToFile(rankingArtistsByLikes_AllTime, Constants.fileName_RankingArtistsByLikes_AllTime);
            System.out.println("> Calcolato con successo 5: " + Constants.fileName_RankingArtistsByLikes_AllTime);

        // Neo4j Queries

            // Album
            List<AlbumWithLikes> rankingAlbumByLikes_LastWeek = albumRepo_Neo4j.getAlbumsSortedByLikes_LastWeek();
            if(rankingAlbumByLikes_LastWeek.isEmpty())
                return "{\"outcome_code\": 7}"; // No albums found (sorted by likes, last week)
            writeToFile(rankingAlbumByLikes_LastWeek, Constants.fileName_RankingAlbumByLikes_LastWeek);
            System.out.println("> Calcolato con successo 6: " + Constants.fileName_RankingAlbumByLikes_LastWeek);

            // Song
            List<SongWithLikes> rankingSongsByLikes_LastWeek = songRepo_Neo4j.getSongsSortedByLikes_LastWeek();
            if(rankingSongsByLikes_LastWeek.isEmpty())
                return "{\"outcome_code\": 8}"; // No songs found (sorted by likes, last week)
            writeToFile(rankingSongsByLikes_LastWeek, Constants.fileName_RankingSongByLikes_LastWeek);
            System.out.println("> Calcolato con successo 7: " + Constants.fileName_RankingSongByLikes_LastWeek);

            System.out.println(">>> Rankings calculated successfully");

            return "{\"outcome_code\": 0}"; // Update successful
        } catch (DataAccessResourceFailureException e) {
            logger.error("Impossibile connettersi al database", e);
            return "{\"outcome_code\": 10}"; // Error while connecting to the database
        } catch (Exception e) {
            logger.error("Error while writing to file", e);
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

    private void writeToFile(Object data, String fileName) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(data);

        // Ottieni il percorso della directory corrente
        String currentDir = System.getProperty("user.dir");

        // Percorso della sottocartella
        String subFolderPath = currentDir + File.separator + Constants.folderName_QueryResults;

        // Crea la sottocartella se non esiste
        File subFolder = new File(subFolderPath);
        if (!subFolder.exists()) {
            subFolder.mkdir(); // Nota: usa mkdirs() per creare tutte le sottodirectory necessarie
        }

        // Costruisci il percorso completo del file
        Path filePath = Paths.get(subFolder.getPath() + File.separator + fileName);

        // Scrivi nel file
        Files.write(filePath, json.getBytes(), StandardOpenOption.CREATE);
    }

}
