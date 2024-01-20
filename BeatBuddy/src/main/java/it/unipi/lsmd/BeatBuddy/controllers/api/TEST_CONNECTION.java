package it.unipi.lsmd.BeatBuddy.controllers.api;

import com.google.gson.Gson;
import it.unipi.lsmd.BeatBuddy.repository.User_Repo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TEST_CONNECTION {
    private static final Logger logger = LoggerFactory.getLogger(TEST_CONNECTION.class);

    @Autowired
    User_Repo user_Repo;

    @GetMapping("/api/testConnection")
    public @ResponseBody String testDatabaseConnection() {
        try {
            long userCount = user_Repo.user_RI.count(); // Conta il numero di documenti nella collezione User
            return new Gson().toJson("{\"connection\": true, \"userCount\": " + userCount + "}");
        } catch (Exception e) {
            logger.error("Errore di connessione al database: ", e);
            return new Gson().toJson("{\"connection\": false}");
        }
    }
}