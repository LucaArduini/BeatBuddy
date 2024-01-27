package it.unipi.lsmd.BeatBuddy.repository;

import it.unipi.lsmd.BeatBuddy.DTO.ArtistDTO;
import it.unipi.lsmd.BeatBuddy.model.Artist;
import it.unipi.lsmd.BeatBuddy.model.dummy.ArtistWithLikes;
import it.unipi.lsmd.BeatBuddy.repository.MongoDB.Artist_MongoInterf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class Artist_Repo_MongoDB {

    @Autowired
    private Artist_MongoInterf artist_RI;
    @Autowired
    private MongoTemplate mongoTemplate;

    public Artist getArtistById(String id){
        try {
            Optional<Artist> result = artist_RI.findById(id);
            return result.orElse(null);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    public List<ArtistDTO> find5ArtistsDTO(String term){
        try {
            Pageable topFive = PageRequest.of(0, 5);
            return artist_RI.findLimitedArtistsByTitleContaining(term, topFive);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    /*public List<ArtistWithLikes> getArtistsWithMinAlbumsByAvgRating_AllTime() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind("artists"), // Appiattisce il vettore degli artisti
                Aggregation.lookup("artists", "artists", "name", "artistDetails"), // Esegue il join con la collection artists
                Aggregation.unwind("artistDetails", true), // Appiattisce i dettagli degli artisti
                Aggregation.group("artists")
                        .avg("averageRating").as("avgRating")
                        .count().as("albumCount")
                        .first("artistDetails.image").as("profilePicUrl")
                        .first("artistDetails.name").as("artistName"),
                Aggregation.match(Criteria.where("albumCount").gte(3)), // Considera solo artisti con almeno 3 album
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "avgRating")), // Ordina per valutazione media decrescente
                Aggregation.limit(5), // Limita a 5 artisti
                Aggregation.project()
                        .and("artistName").as("id")
                        .and("artistName").as("name") // Modifica qui
                        .and("profilePicUrl").as("profilePicUrl")
                        .and("likes").as("likes")
                        .and("avgRating").as("avgRating")
        );

        AggregationResults<ArtistWithLikes> results = mongoTemplate.aggregate(aggregation, "albums", ArtistWithLikes.class);
        return results.getMappedResults();
    }

    public List<ArtistWithLikes> getArtistsByLikes_AllTime() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind("artists"), // Appiattisce il vettore degli artisti
                Aggregation.group("artists") // Raggruppa in base agli artisti
                        .sum("likes").as("likes"), // Calcola la somma dei likes degli album
                Aggregation.lookup("artists", "artists", "name", "artistDetails"), // Aggiunge dettagli degli artisti
                Aggregation.unwind("artistDetails"), // Appiattisce i dettagli degli artisti
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "likes")), // Ordina per il totale dei likes decrescente
                Aggregation.limit(5), // Limita a 5 artisti
                Aggregation.project() // Seleziona i campi necessari
                        .and("artistName").as("id")
                        .and("artistName").as("name") // Modifica qui
                        .and("profilePicUrl").as("profilePicUrl")
                        .and("likes").as("likes")
                        .and("avgRating").as("avgRating")
        );

        AggregationResults<ArtistWithLikes> results = mongoTemplate.aggregate(aggregation, "albums", ArtistWithLikes.class);
        return results.getMappedResults();
    }*/
}
