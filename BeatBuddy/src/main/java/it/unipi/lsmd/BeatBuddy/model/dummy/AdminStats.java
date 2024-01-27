package it.unipi.lsmd.BeatBuddy.model.dummy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminStats {
    private int dailyLikesOnAlbums;
    private int dailyLikesOnSongs;
    private int dailyReviews;
}
