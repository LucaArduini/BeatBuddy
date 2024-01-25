package it.unipi.lsmd.BeatBuddy.repository;

import it.unipi.lsmd.BeatBuddy.DTO.AlbumDTO;
import it.unipi.lsmd.BeatBuddy.DTO.SongDTO;
import it.unipi.lsmd.BeatBuddy.model.Album;
import it.unipi.lsmd.BeatBuddy.repository.MongoDB.Album_MongoInterf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class Album_Repo {

    @Autowired
    private Album_MongoInterf album_RI;

    @Autowired
    private MongoTemplate mongoTemplate;

    public boolean existsById(String id){
        try {
            return album_RI.existsById(id);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }

    public Album getAlbumById(String id){
        try {
            Optional<Album> result = album_RI.findById(id);
            return result.orElse(null);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    public Album getAlbumByTitleAndArtist(String title, String artist){
        try {
            List<Album> result = album_RI.findByTitleAndArtist(title, artist);
            if (result.isEmpty())
                return null;
            else if (result.size() > 1){
                //throw new IllegalStateException("Multiple albums with same title and artist");
                System.err.println("Multiple albums found with the same title and artist");
                return result.get(0);
            }
            else
                return result.get(0);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    public List<AlbumDTO> find5AlbumsDTO(String term){
        try {
            Pageable topFive = PageRequest.of(0, 5);
            return album_RI.findLimitedAlbumsByTitleContaining(term, topFive);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    public List<SongDTO> find5SongsDTO(String term) {
        Query query = new Query();
        query.addCriteria(Criteria.where("songs.name").regex(term, "i"));
        query.limit(5); // Questo limita il numero di album, non di canzoni

        List<Album> albums = mongoTemplate.find(query, Album.class);

        // Uso uno stream per elaborare la lista di album
        return albums.stream()
                // Converto ogni album in uno stream delle sue canzoni
                .flatMap(album -> Arrays.stream(album.getSongs())
                        // Filtro le canzoni di ogni album per mantenere solo quelle che contengono il termine di ricerca nel loro nome
                        .filter(song -> song.getName().toLowerCase().contains(term.toLowerCase()))
                        // Mappo ogni canzone in un oggetto SongDTO, creando una nuova rappresentazione con i dettagli necessari
                        .map(song -> new SongDTO(
                                song.getName(),
                                album.getTitle(),
                                album.getId(),
                                album.getCoverURL(),
                                album.getArtists()
                        )))
                // Applico un secondo limite per assicurarmi che vengano restituite solo 5 canzoni in totale
                .limit(5)
                // Raccogli i risultati filtrati e mappati in una lista
                .collect(Collectors.toList());
    }

//    public int insertReviewIntoAlbum(String albumID, String username, int rating, String text){
//        Album targetAlbum = getAlbumById(albumID).orElse(null);
//        if (targetAlbum == null) {
//            return 1; // Album non trovato
//        }
//
//        ReviewLite tmp_reviewLite = new ReviewLite(username, rating);
//
//
//
//
//        System.out.println("Review inserita con successo in username: " + username);
//    }
}