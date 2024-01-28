package it.unipi.lsmd.BeatBuddy.model.dummy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistWithLikes {
    private String _id;
    private String name;
    private String profilePicUrl;
    private Integer likes;

    public static ArtistWithLikes mapToArtistWithLikes(org.bson.Document doc) {
        String _id = doc.getObjectId("_id").toHexString(); // Converto ObjectId in String
        String name = doc.getString("name");
        String profilePicUrl = doc.getString("profilePicUrl");
        Integer likes = doc.getInteger("likes");

        return new ArtistWithLikes(_id, name, profilePicUrl, likes);
    }
}
