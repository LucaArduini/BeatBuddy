package it.unipi.lsmd.BeatBuddy.controllers.api;

import it.unipi.lsmd.BeatBuddy.model.AlbumLikes;
import it.unipi.lsmd.BeatBuddy.repository.Album_Repo_MongoDB;
import it.unipi.lsmd.BeatBuddy.repository.Album_Repo_Neo4j;
import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.dao.DataAccessResourceFailureException;

import java.util.List;


@RestController
public class AdminFunction_RESTCtrl {
    private static final Logger logger = LoggerFactory.getLogger(AdminFunction_RESTCtrl.class);

    @Autowired
    private Album_Repo_MongoDB album_RepoMongoDB;
    @Autowired
    private Album_Repo_Neo4j album_RepoNeo4j;

    //write a function that executes a query that returns the number of likes for every album and every song
    //presente in the mongoDB, recuperando queste informazioni da neo4j
    @PostMapping("/api/updateNewLikes")
    public @ResponseBody String updateNewLikes(HttpSession session){
        try {
            if(!Utility.isAdmin(session))
                return "{\"outcome_code\": 1}";     // User not found
            System.out.println("SONO QUI 0");

            List<AlbumLikes> newLikesAlbums = album_RepoNeo4j.getNewLikesForAllAlbums();
            if(newLikesAlbums.isEmpty())
                return "{\"outcome_code\": 2}";     // No new likes found
            System.out.println("SONO QUI 1");
            boolean outcome = album_RepoMongoDB.setNewLikesToAlbums(newLikesAlbums);
            if(!outcome)
                return "{\"outcome_code\": 3}";     // Error while updating new likes
            System.out.println("SONO QUI 2");


            //Triple<String, String, Integer>[] getNewLikesForAllSongs()

            return "{\"outcome_code\": 0}";         // Update successful

        } catch (DataAccessResourceFailureException e) {
            logger.error("Impossibile connettersi al database", e);
            return "{\"outcome_code\": 10}";         // Errore di connessione al database
        }
    }
}
