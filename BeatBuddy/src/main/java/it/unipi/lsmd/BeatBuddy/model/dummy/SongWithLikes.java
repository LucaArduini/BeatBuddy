package it.unipi.lsmd.BeatBuddy.model.dummy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongWithLikes {
    private String songName;
    private String albumName;
    private String artistName;
    private String coverUrl;
    private Integer likes;
}

