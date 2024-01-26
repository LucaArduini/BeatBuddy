package it.unipi.lsmd.BeatBuddy.utilities;

public final class Constants {
    public static final String folderName_QueryResults = "cachedQueryResults";

    // File names for the query results - Album
    public static final String fileName_RankingAlbumByRating_AllTime = "rankingAlbumWithMinimumReviewsByRating_AllTime.json";
    public static final String fileName_RankingAlbumByLikes_AllTime = "rankingAlbumByLikes_AllTime.json";
    public static final String fileName_RankingAlbumByRating_LastWeek = "rankingAlbumByRating_LastWeek.json";
    public static final String fileName_RankingAlbumByLikes_LastWeek = "rankingAlbumByLikes_LastWeek.json";

    // File names for the query results - Song
    public static final String fileName_RankingSongByLikes_AllTime = "rankingSongByLikes_AllTime.json";
    public static final String fileName_RankingSongByLikes_LastWeek = "rankingSongByLikes_LastWeek.json";

    // File names for the query results - Artist
    public static final String fileName_RankingArtistsByAvgRating_AllTime = "artistsWithMinAlbumsAndSortedByAvgRating_AllTime.json";
    public static final String fileName_RankingArtistsByLikes_AllTime = "artistsSortedByLikes_AllTime.json";

    // Evita la creazione di istanze di questa classe
    private Constants() {
        throw new AssertionError("Classe Constants non istanziabile.");
    }
}
