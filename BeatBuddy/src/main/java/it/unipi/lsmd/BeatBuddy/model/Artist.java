package it.unipi.lsmd.BeatBuddy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@Document(collection = "artists")
public class Artist {
    @Id
    @Field("_id")
    private String id;
    private String name;
    private String profilePicUrl;
    private AlbumLite[] albums;
}

@Data
@AllArgsConstructor
class AlbumLite {
    private String albumTitle;
    private String coverUrl;
}