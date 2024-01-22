package it.unipi.lsmd.BeatBuddy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class UserForRegistration {
    @Id
    private String id;
    private String name;
    private String surname;
    private String username;
    private String password;
    private String birthDate;
    private String email;
    private ReviewedAlbum[] reviewedAlbums;

    // a differenza di User, non ha il campo isAdmin
}