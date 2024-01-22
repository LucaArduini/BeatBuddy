package it.unipi.lsmd.BeatBuddy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewedAlbum {
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
