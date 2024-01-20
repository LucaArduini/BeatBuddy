package it.unipi.lsmd.BeatBuddy.repository;

import it.unipi.lsmd.BeatBuddy.DTO.AlbumDTO;
import it.unipi.lsmd.BeatBuddy.DTO.converters.AlbumToAlbumDTOConverter;
import it.unipi.lsmd.BeatBuddy.model.Album;
import it.unipi.lsmd.BeatBuddy.repository.MongoDB.Album_RepoInterf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class Album_Repo {

    @Autowired
    private Album_RepoInterf album_RI;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AlbumToAlbumDTOConverter converter;

    public List<AlbumDTO> find5AlbumsDTO(String term){
        Pageable topFive = PageRequest.of(0, 5);
        return album_RI.findFirst5ByTitleContaining(term, topFive);
    }
}
