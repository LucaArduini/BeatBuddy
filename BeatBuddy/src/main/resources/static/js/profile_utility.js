$(document).ready(function() {
    let user = $("#username").text();
    // Aggiungi l'evento click all'elemento "followed-users"
    $("#followed_users_btn").click(function() {
        $.ajax({
            url: "/api/userFollowedUsers",  // Sostituisci con l'URL corretto per ottenere i dati
            data: {username: user},
            dataType: 'json',
            method: "GET",
            success: function(result_f) {
                if(result_f.length === 0){
                    const container = $("#followed_container");
                    container.empty();
                    container.append($("<p>No user followed.</p>"));
                }else{
                    displayFollowed(result_f);
                }
            },
            error: function() {
                alert("Errore nella richiesta Ajax.");
            }
        });
    });
    $("#liked_albums_btn").click(function(){
        $.ajax({
            url: "/api/userLikedAlbums",  // Sostituisci con l'URL corretto per ottenere i dati
            data: {username: user},
            dataType: 'json',
            method: "GET",
            success: function(result_a) {
                displayLikedAlbums(result_a);
            },
            error: function() {
                const container = $("#liked_albums_container");
                container.empty();
                container.append("<p>No album liked.</p>");
            }
        });
    });
    $("#liked_songs_btn").click(function (){
        $.ajax({
            url: "/api/userLikedSongs",  // Sostituisci con l'URL corretto per ottenere i dati
            data: {username: user},
            dataType: 'json',
            method: "GET",
            success: function(result_s) {
                displayLikedSongs(result_s);
            },
            error: function() {
                const container = $("#liked_songs_container");
                container.empty();
                container.append("<p>No song liked.</p>");
            }
        });
    });
});
function displayFollowed(followed) {
    const container = $("#followed_container");
    container.empty();

    followed.forEach(function (user_tmp){
        let userDiv = $("<div class=\"d-flex user_foll p-3 mb-1\"></div>");
        let userInf = $("<h3 class=\"mb-0\"></h3>");
        userDiv.append(userInf);
        userInf.append($("<i class=\"fa fa-user me-3\"></i>"));
        userInf.append($("<span></span>").text(user_tmp.username));
        container.append(userDiv);
    })
}
function displayLikedAlbums(liked){
    const container = $("#liked_albums_container");
    container.empty();

    liked.forEach(function (album_tmp){
        let albumDiv = $("<div class=\"d-flex album_liked gap-1 p-1 align-items-center mb-1\"></div>");
        albumDiv.append($("<img class=\"album-cover shadow\" style=\"max-width: 100px;\">").attr("src", album_tmp.coverURL));
        let albumInf = $("<div class=\"d-flex flex-column\"></div>");
        albumDiv.append(albumInf);
        albumInf.append($("<h3 style=\"margin-bottom:0; font-weight: bold\"></h3>").text(album_tmp.albumName));
        albumInf.append($("<p style=\"font-size: large; margin-top:0; margin-bottom: 0;\"></p>").text(album_tmp.artists));
        container.append(albumDiv);
    })
}
function displayLikedSongs(liked){
    const container = $("#liked_songs_container");
    container.empty();

    liked.forEach(function (song_tmp){
        let songDiv = $("<div class=\"d-flex song_liked gap-1 p-1 align-items-center mb-1\"></div>");
        songDiv.append($("<img class=\"album-cover shadow\" style=\"max-width: 100px;\">").attr("src", song_tmp.coverUrl));
        let songInf = $("<div class=\"d-flex flex-column\"></div>");
        songDiv.append(songInf);
        songInf.append($("<h3 style=\"margin-bottom:0; font-weight: bold\"></h3>").text(song_tmp.songName));
        songInf.append($("<p style=\"font-size: large; margin-top:0; margin-bottom: 0;\"></p>").text(song_tmp.albumName));
        songInf.append($("<p style=\"font-size: large; margin-top:0; margin-bottom: 0;\"></p>").text(song_tmp.artistName));
        container.append(songDiv);
    })
}