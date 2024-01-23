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



    public void calculateDurations_MinSec() {
        for (Song song : this.songs)
            song.setDuration_minSec(song.convertToMinSec(song.getDuration_ms()));
    }

    // Getter per il campo 'songs' che calcola 'duration_minSec' per ciascuna canzone
    public Song[] getSongs() {
        if (songs != null) {
            for (Song song : songs) {
                song.setDuration_minSec(song.convertToMinSec(song.getDuration_ms()));
            }
        }
        return songs;
    }

    public String calculateTotalDuration() {
        int totalDuration = 0;
        for (Song song : this.songs)
            totalDuration += song.getDuration_ms();

        //create the string in the format "X min Y sec"
        long seconds = totalDuration / 1000;
        long minutes = seconds / 60;
        seconds %= 60;
        return minutes + " min " + String.format("%02d", seconds) + " sec";
    }
}

@Data
@AllArgsConstructor
class LastReview {
    private String username;
    private int rating;
    private String text;
}
