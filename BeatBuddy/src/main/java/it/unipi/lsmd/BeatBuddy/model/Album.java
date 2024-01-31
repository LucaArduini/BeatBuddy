package it.unipi.lsmd.BeatBuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
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

    public Album (ObjectId objId, String title, String[] artists, String coverURL, Song[] songs, int likes, float averageRating, String year, ReviewLite[] lastReviews) {
        this.id = objId.toString();
        this.title = title;
        this.artists = artists;
        this.coverURL = coverURL;
        this.songs = songs;
        this.likes = likes;
        this.averageRating = averageRating;
        this.year = year;
        this.lastReviews = lastReviews;
    }

    public Album (ObjectId objId, String title, String[] artists, String coverURL, int likes, float averageRating) {
        this.id = objId.toString();
        this.title = title;
        this.artists = artists;
        this.coverURL = coverURL;
        this.likes = likes;
        this.averageRating = averageRating;
    }

    public static Album mapToAlbum(org.bson.Document doc) {
        ObjectId id = doc.getObjectId("id");
        String title = doc.getString("title");
        String[] artists = doc.getList("artists", String.class).toArray(new String[0]);
        String coverURL = doc.getString("coverURL");
        int likes = doc.getInteger("likes");
        float averageRating = doc.getDouble("averageRating").floatValue();

        // campi di cui non è necessario fare il mapping
        Song[] songs = new Song[0];
        String year = ""; // Inizializza year con una stringa vuota
        ReviewLite[] lastReviews = new ReviewLite[0];

        return new Album(id, title, artists, coverURL, likes, averageRating);
    }

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