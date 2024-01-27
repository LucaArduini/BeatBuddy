package it.unipi.lsmd.BeatBuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
    private ReviewLite[] lastReviews;



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

        //create the string in the format "z hr X min Y sec", if z=0 then it is omitted
        int hours = totalDuration / 3600000;
        int minutes = (totalDuration % 3600000) / 60000;
        int seconds = ((totalDuration % 3600000) % 60000) / 1000;

        String totalDuration_minSec = "";
        if (hours != 0)
            totalDuration_minSec += hours + " hr ";
        totalDuration_minSec += minutes + " min " + seconds + " sec";

        return totalDuration_minSec;
    }

    @JsonIgnore
    public String getArtistsAsString() {
        String artistsString = "";
        for (String artist : artists) {
            artistsString += artist + ", ";
        }

        if(artistsString.length() == 0)
            return "";
        else
            return artistsString.substring(0, artistsString.length() - 2);
    }
}