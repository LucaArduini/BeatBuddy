package it.unipi.lsmd.BeatBuddy.repository.MongoDB;

import it.unipi.lsmd.BeatBuddy.DTO.AlbumDTO;
import it.unipi.lsmd.BeatBuddy.model.Album;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface Album_RepoInterf extends MongoRepository<Album, String> {
    boolean existsByTitle(String title);

    @Query(value = "{ 'title': { $regex: ?0, $options: 'i' } }", fields = "{ 'id': 1, 'artists': 1, 'coverURL': 1, 'title': 1 }")
    List<AlbumDTO> findFirst5ByTitleContaining(String term, Pageable pageable);
}
