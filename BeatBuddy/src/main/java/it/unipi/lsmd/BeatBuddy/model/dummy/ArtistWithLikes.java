package it.unipi.lsmd.BeatBuddy.model.dummy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistWithLikes {
//    @Field("artistDetails._id")
    private String id;
//    @Field("artists")
    private String name;
//    @Field("artistDetails.image")
    private String profilePicUrl;
    //private Integer likes;
//    @Field("avgRating")
    private Double avgRating;

//    public ArtistWithLikes(String id, String name, String profilePicUrl, Integer likes) {
//        this.id = id;
//        this.name = name;
//        this.profilePicUrl = profilePicUrl;
//        this.likes = likes;
//    }

//    public ArtistWithLikes(String id, String name, String profilePicUrl, Double avgRating) {
//        this.id = id;
//        this.name = name;
//        this.profilePicUrl = profilePicUrl;
//        this.avgRating = avgRating;
//    }
}
