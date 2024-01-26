package it.unipi.lsmd.BeatBuddy.repository.MongoDB;

import it.unipi.lsmd.BeatBuddy.DTO.AlbumDTO;
import it.unipi.lsmd.BeatBuddy.model.Album;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface Album_MongoInterf extends MongoRepository<Album, String> {
    boolean existsById(String id);
    boolean existsByTitle(String title);
    Optional<Album> findById(String id);

    @Query(value = "{ 'title': ?0, 'artists': { $in: [?1] } }")
    List<Album> findByTitleAndArtist(String title, String artist);

    @Query(value = "{ 'title': { $regex: ?0, $options: 'i' } }", fields = "{ 'id': 1, 'artists': 1, 'coverURL': 1, 'title': 1 }")
    List<AlbumDTO> findLimitedAlbumsByTitleContaining(String term, Pageable pageable);

    // NB: Non puoi eseguire un'operazione di aggiornamento direttamente
    // all'interno di un'annotazione @Query

}
