package it.unipi.lsmd.BeatBuddy.repository;

import it.unipi.lsmd.BeatBuddy.model.AlbumLikes;
import it.unipi.lsmd.BeatBuddy.repository.Neo4j.Album_Neo4jInterf;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import org.neo4j.driver.Record;

import java.util.ArrayList;
import java.util.List;

@Service
public class Album_Repo_Neo4j {

    private final Album_Neo4jInterf albumNeo4jRepository;

    @Autowired
    public Album_Repo_Neo4j(Album_Neo4jInterf albumNeo4jRepository) {
        this.albumNeo4jRepository = albumNeo4jRepository;
    }

    @Transactional
    public List<AlbumLikes> getNewLikesForAllAlbums() {
        try {
            System.out.println("SONO QUI #############");

            System.out.println(albumNeo4jRepository.getNewLikesForAlbums());
            System.out.println("SONO QUI !!!!!!");
            List<AlbumLikes> albumLikesList = new ArrayList<>();

//            System.out.println("SONO QUI ??????");
//
//            for (Record record : records) {
//                String coverURL = record.get("coverURL").asString();
//                Integer likes = record.get("likes").asInt();
//                albumLikesList.add(new AlbumLikes(coverURL, likes));
//            }
//
//            System.out.println("SONO QUI #############");
//            System.out.println(albumLikesList);

            return albumLikesList;
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return List.of();
        }
    }

    @Transactional
    public Triple<String, String, Integer>[] getNewLikesForAllSongs() {
        try {
            return albumNeo4jRepository.getNewLikesForSongs();
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return new Triple[0];
        }
    }
}
