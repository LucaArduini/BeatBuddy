package it.unipi.lsmd.BeatBuddy.repository;

import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import it.unipi.lsmd.BeatBuddy.DTO.ArtistDTO;
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
import org.springframework.stereotype.Repository;

import static com.mongodb.client.model.Accumulators.*;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;

import java.util.*;

@Repository
public class Artist_Repo_MongoDB {

    @Autowired
    private Artist_MongoInterf artist_RI;

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
        Bson sortOp     = sort(descending("avgRating"));
        Bson limitOp1   = limit(30);
        Bson lookupOp   = lookup("artists", "_id", "name", "artistDetails");
        Bson matchOp2   = match(ne("artistDetails", Collections.EMPTY_LIST));
        Bson limitOp2   = limit(10);
        Bson projOp     = project(fields(
                computed("_id", new Document("$arrayElemAt", Arrays.asList("$artistDetails._id", 0))),
                computed("name", new Document("$arrayElemAt", Arrays.asList("$artistDetails.name", 0))),
                computed("profilePicUrl", new Document("$arrayElemAt", Arrays.asList("$artistDetails.profilePicUrl", 0))),
                include("avgRating")
        ));

        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
                unwindOp1, groupOp, matchOp, sortOp, /*limitOp1,*/ lookupOp, matchOp2, limitOp2, projOp
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
        Bson sortOp     = sort(descending("likes"));
        Bson limitOp1   = limit(30);
        Bson lookupOp   = lookup("artists", "_id", "name", "artistDetails");
        Bson matchOp2   = match(ne("artistDetails", Collections.EMPTY_LIST));
        Bson limitOp2   = limit(10);
        Bson projOp     = project(fields(
                computed("_id", new Document("$arrayElemAt", Arrays.asList("$artistDetails._id", 0))),
                computed("name", new Document("$arrayElemAt", Arrays.asList("$artistDetails.name", 0))),
                computed("profilePicUrl", new Document("$arrayElemAt", Arrays.asList("$artistDetails.profilePicUrl", 0))),
                include("likes")
        ));

        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
                unwindOp1, groupOp, sortOp, /*limitOp1,*/ lookupOp, matchOp2, limitOp2, projOp
        ));

        List<ArtistWithLikes> artistsWithLikes = new ArrayList<>();
        result.forEach(doc -> artistsWithLikes.add(ArtistWithLikes.mapToArtistWithLikes(doc)));

        myMongoClient.close();
        return artistsWithLikes;
    }
}