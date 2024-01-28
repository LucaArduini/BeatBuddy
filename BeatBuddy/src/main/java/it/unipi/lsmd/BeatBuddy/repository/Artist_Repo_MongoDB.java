package it.unipi.lsmd.BeatBuddy.repository;

import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import it.unipi.lsmd.BeatBuddy.DTO.ArtistDTO;
import it.unipi.lsmd.BeatBuddy.model.Album;
import it.unipi.lsmd.BeatBuddy.model.Artist;
import it.unipi.lsmd.BeatBuddy.model.dummy.ArtistWithAvgRating;
import it.unipi.lsmd.BeatBuddy.model.dummy.ArtistWithLikes;
import it.unipi.lsmd.BeatBuddy.repository.MongoDB.Artist_MongoInterf;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Aggregates.limit;
import static com.mongodb.client.model.Aggregates.lookup;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Aggregates.sort;
import static com.mongodb.client.model.Aggregates.unwind;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;

// ... (altri import necessari)

import java.util.ArrayList;
import java.util.Arrays;
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

    public List<ArtistWithAvgRating> getArtistsWithMinAlbumsByAvgRating_AllTime() {
        MongoClient myMongoClient = MongoClients.create(new ConnectionString("mongodb://10.1.1.18:27017,10.1.1.17:27017,10.1.1.19:27017/?replicaSet=BB&w=1&readPreference=nearest&retryWrites=true"));
        MongoDatabase database = myMongoClient.getDatabase("BeatBuddy");
        MongoCollection<Document> collection = database.getCollection("albums");

        Bson unwindOp1  = unwind("$artists");
        Bson groupOp    = group("$artists",
                avg("avgRating", "$averageRating"),
                sum("albumCount", 1));
        Bson matchOp    = match(gte("albumCount", 3));
        Bson lookupOp   = lookup("artists", "_id", "name", "artistDetails");
        Bson unwindOp2  = unwind("$artistDetails");      // se un documento non ha il campo "artistDetails" o se il campo "artistDetails" è un array vuoto, il documento verrà escluso dalla pipeline di aggregazione.
        Bson sortOp     = sort(descending("avgRating"));
        Bson limitOp    = limit(10);
        Bson projOp     = project(fields(
                computed("_id", "$artistDetails._id"),
                computed("name", "$artistDetails.name"),
                computed("profilePicUrl", "$artistDetails.profilePicUrl"),
                include("avgRating")
        ));

        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
                unwindOp1, groupOp, matchOp, lookupOp,
                unwindOp2, sortOp, limitOp, projOp
        ));

        List<ArtistWithAvgRating> artistsWithLikes = new ArrayList<>();
        result.forEach(doc -> artistsWithLikes.add(ArtistWithAvgRating.mapToArtistWithLikes(doc)));

        myMongoClient.close();
        return artistsWithLikes;
    }

    public List<ArtistWithLikes> getArtistsByLikes_AllTime() {
        MongoClient myMongoClient = MongoClients.create(new ConnectionString("mongodb://10.1.1.18:27017,10.1.1.17:27017,10.1.1.19:27017/?replicaSet=BB&w=1&readPreference=nearest&retryWrites=true"));
        MongoDatabase database = myMongoClient.getDatabase("BeatBuddy");
        MongoCollection<Document> collection = database.getCollection("albums");

        Bson unwindOp1  = unwind("$artists");
        Bson groupOp    = group("$artists",
                sum("likes", "$likes"));
        Bson lookupOp   = lookup("artists", "_id", "name", "artistDetails");
        Bson unwindOp2  = unwind("$artistDetails"); // Unwind per dettagli artista
        Bson sortOp     = sort(descending("likes"));
        Bson limitOp    = limit(10);
        Bson projOp     = project(fields(
                computed("_id", "$artistDetails._id"),
                computed("name", "$artistDetails.name"),
                computed("profilePicUrl", "$artistDetails.profilePicUrl"),
                include("likes")
        ));

        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
                unwindOp1, groupOp, lookupOp, unwindOp2,
                sortOp, limitOp, projOp
        ));

        List<ArtistWithLikes> artistsWithLikes = new ArrayList<>();
        result.forEach(doc -> artistsWithLikes.add(ArtistWithLikes.mapToArtistWithLikes(doc)));

        myMongoClient.close();
        return artistsWithLikes;
    }
}