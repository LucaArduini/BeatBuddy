package it.unipi.lsmd.BeatBuddy.repository;

import it.unipi.lsmd.BeatBuddy.DTO.ArtistDTO;
import it.unipi.lsmd.BeatBuddy.repository.MongoDB.Artist_RepoInterf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class Artist_Repo {

    @Autowired
    private Artist_RepoInterf artist_RI;

    public List<ArtistDTO> find5ArtistDTO(String term){
        Pageable topFive = PageRequest.of(0, 5);
        return artist_RI.findFirst5ByTitleContaining(term, topFive);
    }
}
