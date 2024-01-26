package it.unipi.lsmd.BeatBuddy.model.dummy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistWithLikes {
    private String id;
    private String name;
    private String profilePicUrl;
    private Integer likes;
    private Double avgRating;

    public ArtistWithLikes(String id, String name, String profilePicUrl, Integer likes) {
        this.id = id;
        this.name = name;
        this.profilePicUrl = profilePicUrl;
        this.likes = likes;
    }

    public ArtistWithLikes(String id, String name, String profilePicUrl, Double avgRating) {
        this.id = id;
        this.name = name;
        this.profilePicUrl = profilePicUrl;
        this.avgRating = avgRating;
    }
}
