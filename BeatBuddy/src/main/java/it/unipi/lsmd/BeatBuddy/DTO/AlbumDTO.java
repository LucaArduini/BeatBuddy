package it.unipi.lsmd.BeatBuddy.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDTO {
    private String id;
    private String[] artists;
    private String coverURL;
    private String title;
}
