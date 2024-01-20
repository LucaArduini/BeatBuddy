package it.unipi.lsmd.BeatBuddy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@Document(collection = "users")
public class Review {
    @Id
    @Field("_id")
    private String id;
    // ####
    // DA FARE POI PERCHè MI SERVE DI VEDERE I NOMI DEI CAMPI
    // NELLA COLLECTION
}