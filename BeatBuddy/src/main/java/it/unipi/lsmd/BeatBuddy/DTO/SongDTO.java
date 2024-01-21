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
}
