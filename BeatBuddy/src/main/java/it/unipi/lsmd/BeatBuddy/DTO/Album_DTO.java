package it.unipi.lsmd.BeatBuddy.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class Album_DTO {
    @Id
    private String id;
    private String name;
    private String[] artists;
    private String coverUrl;


}
