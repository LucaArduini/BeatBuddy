package it.unipi.lsmd.BeatBuddy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class Song {
    private String name;
    private int duration_ms;
    private int likes;

    @Transient
    private String duration_minSec;

    // Costruttore personalizzato per convertire la durata in millisecondi in "min:sec"
    public Song(String name, int duration_ms, int likes) {
        this.name = name;
        this.duration_ms = duration_ms;
        this.likes = likes;
        this.duration_minSec = convertToMinSec(duration_ms);
    }

    public String convertToMinSec(int duration_ms) {
        long seconds = duration_ms / 1000;
        long minutes = seconds / 60;
        seconds %= 60;
        return minutes + ":" + String.format("%02d", seconds);
    }
}
