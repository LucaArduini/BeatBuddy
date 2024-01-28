package it.unipi.lsmd.BeatBuddy.model.dummy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistWithAvgRating {
    private ObjectId _id;
    private String name;
    private String profilePicUrl;
    private Double avgRating;

    public static ArtistWithAvgRating mapToArtistWithLikes(Document doc) {
        ObjectId _id = doc.getObjectId("_id");
        String name = doc.getString("name");
        String profilePicUrl = doc.getString("profilePicUrl");
        Double avgRating = doc.getDouble("avgRating");

        return new ArtistWithAvgRating(_id, name, profilePicUrl, avgRating);
    }
}
