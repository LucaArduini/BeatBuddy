package it.unipi.lsmd.BeatBuddy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@AllArgsConstructor
@Document(collection = "albums")
public class User {
    @Id
    @Field("_id")
    private String id;
    private String name;
    private String surname;
    private String password;
    private String[] artists;
    private Date birthday;

}
