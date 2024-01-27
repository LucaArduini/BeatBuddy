package it.unipi.lsmd.BeatBuddy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import it.unipi.lsmd.BeatBuddy.utilities.Utility;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reviews")
public class Review {
    @Id
    private String id;
    private int rating;
    private String text;
    private ObjectId albumID;
    private String username;
    private Date date;

    @Transient
    private String printableDate;


    public Review(int rating, String text, ObjectId albumObjectId, String username, Date date) {
        this.rating = rating;
        this.text = text;
        this.albumID = albumObjectId;
        this.username = username;
        this.date = date;
    }

    public void setPrintableDate() {
        //write the date in the format "yyyy-MM-dd HH:mm"
        try {
            Instant instant = date.toInstant();
            ZoneId zoneId = ZoneId.of("UTC"); // Specifica la zona oraria desiderata
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(zoneId);
            printableDate = formatter.format(instant);
        } catch (Exception e) {
            e.printStackTrace();
            printableDate = null;
        }
    }
}