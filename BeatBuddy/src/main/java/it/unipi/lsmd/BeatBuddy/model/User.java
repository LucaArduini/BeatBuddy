package it.unipi.lsmd.BeatBuddy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;
    private String birthDate;
    private ReviewedAlbum[] reviewedAlbums;

    @Field("isAdmin")
    private boolean admin;
}

@Data
@AllArgsConstructor
class ReviewedAlbum {
    private String alumTitle;
    private String coverUrl;
    private String artist;
    private int rating;

    public String[] getArtitsArray() {
        String[] artists = artist.split(",");
        for (int i = 0; i < artists.length; i++) {
            artists[i] = artists[i].trim(); // Rimuovi spazi iniziali e finali
        }
        return artists;
    }
}
