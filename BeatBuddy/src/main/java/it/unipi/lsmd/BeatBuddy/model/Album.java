package it.unipi.lsmd.BeatBuddy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@Document(collection = "albums")
public class Album {
    @Id
    @Field("_id")
    private String id;
    private String name;
    private String[] artists;
    private String coverUrl;
    private Song[] songs;
    private int likes;
    private float averageRating;
    private LastReview[] lastReviews;
}
