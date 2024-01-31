package it.unipi.lsmd.BeatBuddy.utilities;

import com.mongodb.ConnectionString;

import java.io.File;

public final class Constants {
    // Folder names
    public static final String folderName_MainFolder = "data_BeatBuddy";
    public static final String folderName_QueryResults = folderName_MainFolder + File.separator + "cachedQueryResults";
    public static final String folderName_AdminStats = folderName_MainFolder + File.separator + "adminStats";
    public static final String folderName_DatabaseUpdateScript = folderName_MainFolder + File.separator + "databaseUpdateScript";

    // File names for the statistics for the administrator
    public static final String fileName_AdminStats = "adminStats.json";

    // File names for the query results - Album
    public static final String fileName_RankingAlbumByRating_AllTime = "rankingAlbumWithMinReviewsByAvgRating_AllTime.json";
    public static final String fileName_RankingAlbumByLikes_AllTime = "rankingAlbumByLikes_AllTime.json";
    public static final String fileName_RankingAlbumByRating_LastWeek = "rankingAlbumByRating_LastWeek.json";
    public static final String fileName_RankingAlbumByLikes_LastWeek = "rankingAlbumByLikes_LastWeek.json";

    // File names for the query results - Song
    public static final String fileName_RankingSongByLikes_AllTime = "rankingSongByLikes_AllTime.json";
    public static final String fileName_RankingSongByLikes_LastWeek = "rankingSongByLikes_LastWeek.json";

    // File names for the query results - Artist
    public static final String fileName_RankingArtistsByAvgRating_AllTime = "rankingArtistsWithMinAlbumsByAvgRating_AllTime.json";
    public static final String fileName_RankingArtistsByLikes_AllTime = "rankingArtistsByLikes_AllTime.json";

    // Evita la creazione di istanze di questa classe
    private Constants() {
        throw new AssertionError("Classe Constants non istanziabile.");
    }
}
