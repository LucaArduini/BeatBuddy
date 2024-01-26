package it.unipi.lsmd.BeatBuddy.repository;

import it.unipi.lsmd.BeatBuddy.DTO.AlbumDTO;
import it.unipi.lsmd.BeatBuddy.DTO.SongDTO;
import it.unipi.lsmd.BeatBuddy.model.Album;
import it.unipi.lsmd.BeatBuddy.model.dummy.AlbumOnlyLikes;
import it.unipi.lsmd.BeatBuddy.model.ReviewLite;
import it.unipi.lsmd.BeatBuddy.model.Song;
import it.unipi.lsmd.BeatBuddy.model.dummy.SongOnlyLikes;
import it.unipi.lsmd.BeatBuddy.repository.MongoDB.Album_MongoInterf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class Album_Repo_MongoDB {

    @Autowired
    private Album_MongoInterf album_RI_Mongo;

    @Autowired
    private MongoTemplate mongoTemplate;

    public boolean existsById(String id){
        try {
            return album_RI_Mongo.existsById(id);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }

    public Album getAlbumById(String id){
        try {
            Optional<Album> result = album_RI_Mongo.findById(id);
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
            List<Album> result = album_RI_Mongo.findByTitleAndArtist(title, artist);
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

//    public List<Album> getAlbumsSortedByRating(){
//        try {
//            Pageable topFive = PageRequest.of(0, 5);
//            return album_RI_Mongo.findAlbumsSortedByRating(topFive);
//        } catch (DataAccessException dae) {
//            if (dae instanceof DataAccessResourceFailureException)
//                throw dae;
//            dae.printStackTrace();
//            return null;
//        }
//    }

    public List<Album> getAlbumsWithMinimumReviewsAndSortedByRating_AllTime() {
        int minReviews = 5;
        Pageable pageable = Pageable.ofSize(5);

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.lookup("reviews", "_id", "albumID", "albumReviews"), // Join con la collection reviews
                Aggregation.unwind("albumReviews", true), // Appiattisce l'array di reviews
                Aggregation.group("_id")
                        .first("title").as("title")
                        .first("artists").as("artists")
                        .first("coverURL").as("coverURL")
                        .first("songs").as("songs")
                        .first("likes").as("likes")
                        .first("averageRating").as("averageRating")
                        .first("year").as("year")
                        .first("lastReviews").as("lastReviews")
                        .count().as("reviewCount"), // Conta il numero di recensioni
                Aggregation.match(Criteria.where("reviewCount").gte(minReviews)), // Filtra gli album con almeno 5 recensioni
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "averageRating")), // Ordina in base alla media delle recensioni
                Aggregation.limit(pageable.getPageSize()) // Applica la paginazione
        ).withOptions(AggregationOptions.builder().allowDiskUse(true).build()); // Permette l'utilizzo del disco

        AggregationResults<Album> results = mongoTemplate.aggregate(aggregation, "albums", Album.class);
        return results.getMappedResults();
    }

    public List<Album> getAlbumsSortedByLikes_AllTime(){
        try {
            Pageable topFive = PageRequest.of(0, 5);
            return album_RI_Mongo.findAlbumsSortedByLikes_AllTime(topFive);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    public List<SongDTO> getSongsSortedByLikes_AllTime() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind("songs"), // Appiattisce l'array di canzoni
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "songs.likes")), // Ordina le canzoni in base ai likes
                Aggregation.limit(5), // Limita il risultato alle prime 5 canzoni
                Aggregation.project() // Proietta i campi necessari nel formato SongDTO
                        .and("songs.name").as("name")
                        .and("title").as("albumTitle")
                        .and("_id").as("albumId")
                        .and("coverURL").as("coverURL")
                        .and("artists").as("artists")
                        .and("songs.likes").as("likes")
        );

        AggregationResults<SongDTO> results = mongoTemplate.aggregate(aggregation, Album.class, SongDTO.class);
        return results.getMappedResults();
    }

    public List<AlbumDTO> find5AlbumsDTO(String term){
        try {
            Pageable topFive = PageRequest.of(0, 5);
            return album_RI_Mongo.findLimitedAlbumsByTitleContaining(term, topFive);
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

    public int insertReviewIntoAlbum(String albumID, String username, int rating, String text) {
        try {
            Album album = getAlbumById(albumID);
            if (album == null) {
                return 1; // Album non trovato
            }

            LinkedList<ReviewLite> lastReviews = new LinkedList<>(Arrays.asList(album.getLastReviews()));
            lastReviews.addFirst(new ReviewLite(username, rating, text));
            while (lastReviews.size() > 5) {
                lastReviews.removeLast();
            }

            album.setLastReviews(lastReviews.toArray(new ReviewLite[0]));
            album_RI_Mongo.save(album);
            return 0; // Successo
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            return 2; // Violazione del vincolo di unicità
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return 3; // Violazione dell'integrità dei dati
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException) {
                throw dae;
            }
            dae.printStackTrace();
            return 4; // Altre eccezioni relative all'accesso ai dati
        }
    }

//    public void addReviewToAlbum(String albumId, ReviewLite review) {
//        Query query = new Query(Criteria.where("id").is(albumId));
//        Update.PushOperatorBuilder update = new Update().push("lastReviews").slice(-5);
//        mongoTemplate.updateFirst(query, update, Album.class);
//    }

    @Transactional
    public boolean setNewLikesToAlbums(AlbumOnlyLikes[] likeList) {
        try {
            BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, Album.class);

            for (AlbumOnlyLikes like : likeList) {
                Query query = new Query(Criteria.where("coverURL").is(like.getCoverURL()));
                Update update = new Update().set("likes", like.getLikes());
                bulkOps.updateOne(query, update);
            }

            bulkOps.execute();
            return true;
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean setNewLikesToSongs(SongOnlyLikes[] likeList) {
        try {
            BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, Song.class);

            for (SongOnlyLikes like : likeList) {
                Query query = new Query(Criteria.where("coverURL").is(like.getCoverUrl()));
                Update update = new Update().set("likes", like.getLikes());
                bulkOps.updateOne(query, update);
            }

            bulkOps.execute();
            return true;
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }
}