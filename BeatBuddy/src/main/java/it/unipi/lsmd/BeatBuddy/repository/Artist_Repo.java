package it.unipi.lsmd.BeatBuddy.repository;

import it.unipi.lsmd.BeatBuddy.DTO.ArtistDTO;
import it.unipi.lsmd.BeatBuddy.model.Artist;
import it.unipi.lsmd.BeatBuddy.repository.MongoDB.Artist_MongoInterf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class Artist_Repo {

    @Autowired
    private Artist_MongoInterf artist_RI;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Optional<Artist> getArtistById(String id){
        try {
            System.out.println(artist_RI.findById(id));
            return artist_RI.findById(id);
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            return Optional.empty();
        }
    }

    public List<ArtistDTO> find5ArtistDTO(String term){
        Pageable topFive = PageRequest.of(0, 5);
        return artist_RI.findFirst5ByTitleContaining(term, topFive);
    }
}
