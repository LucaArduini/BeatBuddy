package it.unipi.lsmd.BeatBuddy.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDTO {
    private String id;
    private String name;
    private String profilePicUrl;
}
