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
                displayLikedAlbums(result_a, user);
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
    let increment = 0;
    const container = $("#followed_container");
    container.empty();

    followed.forEach(function (user_tmp){
        let userDiv = $("<div class=\"d-flex user_foll p-3 mb-1\"></div>");
        let userInf = $("<h5 class=\"mb-0\"></h5>");
        userDiv.append(userInf);
        userInf.append($("<i class=\"fa fa-user me-3\"></i>"));
        userInf.append($("<span></span>").text(user_tmp.username));
        if(window.location.href.includes("profilePage")){
            userInf.append($("<button id=\"unfollow_btn_" + increment + "\" class=\"btn btn-danger\">Unfollow</button>"))
        }
        container.append(userDiv);
        let id="unfollow_btn_" + increment;
        $("#" + id).click(function () {
            let userContainer = $(this).closest(".user_foll");
            let username = $("#username").text();

            console.log("Like album button clicked\nMe: " + username + "user: " + user_tmp.username);
            $.ajax({
                url: '/api/removeFollow',
                dataType: 'json',
                type: 'POST',
                data: {
                    user1: username,
                    user2: user_tmp.username
                },
                success: function (response) {
                    console.log(response);
                    if(response.outcome_code == 0){
                        alert("Follow removed");
                    }
                    else if(response.outcome_code == 1)
                        alert("Dislike addition unsuccessful");
                    else
                        alert("Error occurred while adding like to song");
                },
                error: function (xhr, status, error) {
                    console.log("Error: " + error);
                }
            });
        });
        increment++;
    })
}
function displayLikedAlbums(liked){
    let increment = 0;
    const container = $("#liked_albums_container");
    container.empty();

    liked.forEach(function (album_tmp){
        let albumDiv = $("<div class=\"d-flex album_liked gap-1 p-1 align-items-center mb-1 justify-content-between\"></div>");
        albumDiv.append($("<img class=\"album-cover shadow\" style=\"max-width: 100px;\">").attr("src", album_tmp.coverURL));
        let albumInf = $("<div class=\"d-flex flex-column\"></div>");
        albumDiv.append(albumInf);
        albumInf.append($("<h3 style=\"margin-bottom:0; font-weight: bold\"></h3>").text(album_tmp.albumName));
        albumInf.append($("<p style=\"font-size: large; margin-top:0; margin-bottom: 0;\"></p>").text(album_tmp.artistName));
        if(window.location.href.includes("profilePage")){
            albumDiv.append("<button id=\"dislike_A_btn_" + increment + "\" class=\"btn btn-danger\">Dislike</button>");
        }
        container.append(albumDiv);
        let id="dislike_A_btn_" + increment;
        $("#" + id).click(function () {
            let albumContainer = $(this).closest(".album_liked");
            let albumCoverContainer = albumContainer.find("img").first()
            let albumCover = albumCoverContainer.attr("src");
            let username = $("#username").text();
            let albumTitle = albumContainer.find("h3").first().text();

            console.log("Like album button clicked\nCover URL: " + albumCover + "user: " + username + "title: " + albumTitle);
            $.ajax({
                url: '/api/albumDetails/removeLikesAlbum',
                dataType: 'json',
                type: 'POST',
                data: {
                    username: username,
                    coverURL: albumCover
                },
                success: function (response) {
                    console.log(response);
                    if(response.outcome_code == 0){
                        alert("Album disliked");
                    }
                    else if(response.outcome_code == 1)
                        alert("Dislike addition unsuccessful");
                    else
                        alert("Error occurred while adding like to song");
                },
                error: function (xhr, status, error) {
                    console.log("Error: " + error);
                }
            });
        });
        increment++;
    })
}
function displayLikedSongs(liked){
    let increment = 0;
    const container = $("#liked_songs_container");
    container.empty();

    liked.forEach(function (song_tmp){
        let songDiv = $("<div class=\"d-flex song_liked gap-1 p-1 align-items-center mb-1 justify-content-between\"></div>");
        songDiv.append($("<img class=\"album-cover shadow\" style=\"max-width: 100px;\">").attr("src", song_tmp.coverUrl));
        let songInf = $("<div class=\"d-flex flex-column\"></div>");
        songDiv.append(songInf);
        songInf.append($("<h3 style=\"margin-bottom:0; font-weight: bold\"></h3>").text(song_tmp.songName));
        songInf.append($("<p style=\"font-size: large; margin-top:0; margin-bottom: 0;\"></p>").text(song_tmp.albumName));
        songInf.append($("<p style=\"font-size: large; margin-top:0; margin-bottom: 0;\"></p>").text(song_tmp.artistName));
        if(window.location.href.includes("profilePage")){
            songDiv.append($("<button id=\"dislike_S_btn_" + increment + "\" class=\"btn btn-danger\">Dislike</button>"))
        }
        container.append(songDiv);
        let id="dislike_S_btn_" + increment;
        $("#" + id).click(function () {
            let songContainer = $(this).closest(".song_liked");
            let songCoverContainer = songContainer.find("img").first()
            let songCover = songCoverContainer.attr("src");
            let username = $("#username").text();
            let songTitle = songContainer.find("h3").first().text();

            console.log("Like album button clicked\nCover URL: " + songCover + "user: " + username + "title: " + songTitle);
            $.ajax({
                url: '/api/albumDetails/removeLikesSong',
                dataType: 'json',
                type: 'POST',
                data: {
                    username: username,
                    title: songTitle,
                    coverURL: songCover
                },
                success: function (response) {
                    console.log(response);
                    if(response.outcome_code == 0){
                        alert("Song disliked");
                    }
                    else if(response.outcome_code == 1)
                        alert("Dislike addition unsuccessful");
                    else
                        alert("Error occurred while adding like to song");
                },
                error: function (xhr, status, error) {
                    console.log("Error: " + error);
                }
            });
        });
        increment++;
    })
}