package it.unipi.lsmd.BeatBuddy.model.dummy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumOnlyAvgRating {
    private ObjectId albumID;
    private Double averageRating;

    public void roundAverageRating() {
        this.averageRating = Math.round(this.averageRating * 1000.0) / 1000.0;
    }
}
