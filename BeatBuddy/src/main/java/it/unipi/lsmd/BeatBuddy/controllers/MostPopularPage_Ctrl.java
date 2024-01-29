package it.unipi.lsmd.BeatBuddy.controllers;

import it.unipi.lsmd.BeatBuddy.DTO.SongDTO;
import it.unipi.lsmd.BeatBuddy.model.Album;
import it.unipi.lsmd.BeatBuddy.model.dummy.AlbumWithLikes;
import it.unipi.lsmd.BeatBuddy.model.dummy.ArtistWithAvgRating;
import it.unipi.lsmd.BeatBuddy.model.dummy.SongWithLikes;
import it.unipi.lsmd.BeatBuddy.model.dummy.ArtistWithLikes;
import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import it.unipi.lsmd.BeatBuddy.utilities.Constants;

@Controller
public class MostPopularPage_Ctrl {

    @Autowired
    @Qualifier("webApplicationContext")
    private ResourceLoader resourceLoader;

    @RequestMapping("/mostPopularPage")
    public String mostPopularPage(HttpSession session,
                                  Model model,
                                  @RequestParam("type") String type) {
        if (!type.equals("artists") && !type.equals("albums") && !type.equals("songs"))
            return "error/genericError";
        else {
            model.addAttribute("logged", (Utility.isLogged(session)) ? true : false);
            model.addAttribute("type", type);

            try {
                if (type.equals("albums")) {
                    List<Album> rankingAlbumByRating_AllTime = readJsonData(Constants.fileName_RankingAlbumByRating_AllTime, new TypeReference<List<Album>>() {});
                    model.addAttribute("rankingAlbumByRating_AllTime", rankingAlbumByRating_AllTime);

                    List<Album> rankingAlbumByLikes_AllTime = readJsonData(Constants.fileName_RankingAlbumByLikes_AllTime, new TypeReference<List<Album>>() {});
                    model.addAttribute("rankingAlbumByLikes_AllTime", rankingAlbumByLikes_AllTime);

                    List<AlbumWithLikes> rankingAlbumByLikes_LastWeek = readJsonData(Constants.fileName_RankingAlbumByLikes_LastWeek, new TypeReference<List<AlbumWithLikes>>() {});
                    model.addAttribute("rankingAlbumByLikes_LastWeek", rankingAlbumByLikes_LastWeek);
                }
                else if (type.equals("songs")) {
                    List<SongDTO> rankingSongByLikes_AllTime = readJsonData(Constants.fileName_RankingSongByLikes_AllTime, new TypeReference<List<SongDTO>>() {});
                    model.addAttribute("rankingSongByLikes_AllTime", rankingSongByLikes_AllTime);

                    List<SongWithLikes> rankingSongsByLikes_LastWeek = readJsonData(Constants.fileName_RankingSongByLikes_LastWeek, new TypeReference<List<SongWithLikes>>() {});
                    model.addAttribute("rankingSongsByLikes_LastWeek", rankingSongsByLikes_LastWeek);
                }
                else if (type.equals("artists")) {
                    List<ArtistWithAvgRating> rankingArtistsByAvgRating_AllTime = readJsonData(Constants.fileName_RankingArtistsByAvgRating_AllTime, new TypeReference<List<ArtistWithAvgRating>>() {});
                    model.addAttribute("rankingArtistsByAvgRating_AllTime", rankingArtistsByAvgRating_AllTime);

                    List<ArtistWithLikes> rankingArtistsByLikes_AllTime = readJsonData(Constants.fileName_RankingArtistsByLikes_AllTime, new TypeReference<List<ArtistWithLikes>>() {});
                    model.addAttribute("rankingArtistsByLikes_AllTime", rankingArtistsByLikes_AllTime);
                }
                else
                    return "error/genericError";

            } catch (IOException e) {
                e.printStackTrace();
                return "error/genericError";
            }

            return "mostPopularPage";
        }
    }

    private <T> List<T> readJsonData(String jsonFileName, TypeReference<List<T>> typeReference) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        // Ottieni il percorso della directory corrente del server
        String currentDir = System.getProperty("user.dir");
        // Aggiungi il nome della cartella al percorso
        Path jsonFilePath = Paths.get(currentDir, Constants.folderName_QueryResults, jsonFileName);

        // Verifica che il file esista prima di procedere
        if (!Files.exists(jsonFilePath)) {
            throw new IOException("File not found: " + jsonFilePath.toString());
        }

        // Leggi il contenuto del file JSON
        String jsonContent = new String(Files.readAllBytes(jsonFilePath));

        // Deserializza il contenuto JSON in una lista di oggetti del tipo specificato
        return objectMapper.readValue(jsonContent, typeReference);
    }

}