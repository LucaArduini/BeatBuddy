package it.unipi.lsmd.BeatBuddy.repository.MongoDB;

import it.unipi.lsmd.BeatBuddy.DTO.ArtistDTO;
import it.unipi.lsmd.BeatBuddy.model.Artist;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface Artist_MongoInterf extends MongoRepository<Artist, String> {
    boolean existsByName(String name);
    Optional<Artist> findById(String id);

    @Query(value = "{ 'name': { $regex: ?0, $options: 'i' } }", fields = "{ 'id': 1, 'name': 1, 'profilePicUrl': 1 }")
    List<ArtistDTO> findLimitedArtistsByTitleContaining(String term, Pageable pageable);
}