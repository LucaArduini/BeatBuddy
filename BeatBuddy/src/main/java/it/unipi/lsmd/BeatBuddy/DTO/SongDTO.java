package it.unipi.lsmd.BeatBuddy.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongDTO {
    private String name;
    private String albumTitle;
    private String albumId;
    private String coverURL;
    private String[] artists;
    private int likes;

    public SongDTO(String name, String albumTitle, String albumId, String coverURL, String[] artists) {
        this.name = name;
        this.albumTitle = albumTitle;
        this.albumId = albumId;
        this.coverURL = coverURL;
        this.artists = artists;
    }
}
