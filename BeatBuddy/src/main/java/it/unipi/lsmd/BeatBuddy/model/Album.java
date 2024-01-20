package it.unipi.lsmd.BeatBuddy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "albums")
public class Album {
    @Id
    private String id;
    private String title;
    private String[] artists;
    private String coverURL;
    private Song[] songs;
    private int likes;
    private float averageRating;
    private String year;
    @Field("last_reviews")
    private LastReview[] lastReviews;
}

@Data
@AllArgsConstructor
class LastReview {
    private String username;
    private int rating;
    private String text;
}
