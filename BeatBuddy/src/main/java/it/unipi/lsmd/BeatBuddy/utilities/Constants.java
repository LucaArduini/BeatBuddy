package it.unipi.lsmd.BeatBuddy.utilities;

import java.io.File;

public final class Constants {
    // Folder names for the folders
    public static final String folderName_QueryResults = "cachedQueryResults";
    public static final String folderName_AdminStats = folderName_QueryResults + File.separator + "adminStats";

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
