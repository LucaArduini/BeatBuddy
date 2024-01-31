$(document).ready(function() {
    let user = $("#username").text();
    // Button for view the complete list of followed user
    $("#followedUsers_btn").click(function() {
        $.ajax({
            url: "/api/userFollowedUsers",
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
                alert("Error on the AJAX request.");
            }
        });
    });
    // Button for view the complete list of albums liked
    $("#liked_albums_btn").click(function(){
        $.ajax({
            url: "/api/userLikedAlbums",
            data: {username: user},
            dataType: 'json',
            method: "GET",
            success: function(result_a) {
                if(result_a.length === 0){
                    const container = $("#liked_albums_container");
                    container.empty();
                    container.append("<p>No album liked.</p>");
                }
                displayLikedAlbums(result_a);
            },
            error: function() {
                alert("Error on the AJAX request.");
            }
        });
    });
    // Button for view the complete list of songs liked
    $("#liked_songs_btn").click(function (){
        $.ajax({
            url: "/api/userLikedSongs",
            data: {username: user},
            dataType: 'json',
            method: "GET",
            success: function(result_s) {
                if(result_s.length === 0){
                    const container = $("#liked_songs_container");
                    container.empty();
                    container.append("<p>No song liked.</p>");
                }
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

    // For each user find with the ajax request, create his container with all his information and append it in the main container
    // for the followed users
    followed.forEach(function (user_tmp){
        let userDiv = $("<div class=\"d-flex user_foll p-3 mb-1\"></div>");
        let userInf = $("<h5 class=\"mb-0\"></h5>");
        userDiv.append(userInf);
        userInf.append($("<i class=\"fa fa-user me-3\"></i>"));
        userInf.append($("<span></span>").text(user_tmp.username));

        // If we are in our profile page, for each user we follow we have the button to unfollow him
        if(window.location.href.includes("profilePage")){
            userDiv.append($("<button id=\"unfollow_btn_" + increment + "\" class=\"btn btn-danger ms-auto\">Unfollow</button>"))
        }
        $(userInf).click(function (){
            // Before go to the page with the details of the selected user, we have to encode the special characters
            let encoded_user = encodeURIComponent(user_tmp.username);
            window.location.href = "/user?username=" + encoded_user;
        })
        container.append(userDiv);
        let id="unfollow_btn_" + increment;
        $("#" + id).click(function () {
            // AJAX call to unfollow a user, for each user followed
            let username = $("#username").text();
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
                    if(response.outcome_code === 0){
                        alert("Follow removed");
                    }
                    else if(response.outcome_code === 1)
                        alert("Follow removal unsuccessful");
                    else
                        alert("Error occurred while removing a follow");
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

    // For each album find with the ajax request, create his container with all his information and append it in the main container
    // for the liked albums
    liked.forEach(function (album_tmp){
        let albumDiv = $("<div class=\"d-flex album_liked gap-1 p-1 align-items-center mb-1\"></div>");
        albumDiv.append($("<img class=\"album-cover shadow\" style=\"max-width: 100px;\">").attr("src", album_tmp.coverURL));
        let albumInf = $("<div class=\"d-flex flex-column album_inf\"></div>");
        albumDiv.append(albumInf);
        albumInf.append($("<h3 style=\"margin-bottom:0; font-weight: bold\"></h3>").text(album_tmp.albumName));
        albumInf.append($("<p style=\"font-size: large; margin-top:0; margin-bottom: 0;\"></p>").text(album_tmp.artistName));

        // If we are in our profile page, for each album we like we have the button to dislike it
        if(window.location.href.includes("profilePage")){
            albumDiv.append("<button id=\"dislike_A_btn_" + increment + "\" class=\"btn btn-danger ms-auto\">Dislike</button>");
        }
        $(albumInf).click(function (){
            // Before go to the page with the details of the selected album, we have to encode the special characters
            let send_string_artist;
            if(album_tmp.artistName.includes(",")){
                let artist_parts = album_tmp.artistName.split(",");
                let substring = artist_parts[0];
                send_string_artist = encodeURIComponent(substring)
            } else {
                send_string_artist = encodeURIComponent(album_tmp.artistName);
            }
            window.location.href = "/albumDetails?title=" + encodeURIComponent(album_tmp.albumName) + "&artist=" + send_string_artist;
        })
        container.append(albumDiv);
        let id="dislike_A_btn_" + increment;
        $("#" + id).click(function () {
            // AJAX call to dislike an album, for each album liked
            let username = $("#username").text();
            let albumTitle = album_tmp.albumName
            let artistsAsString = album_tmp.artistName;

            $.ajax({
                url: '/api/albumDetails/removeLikesAlbum',
                dataType: 'json',
                type: 'POST',
                data: {
                    username: username,
                    albumTitle: albumTitle,
                    artists: artistsAsString
                },
                success: function (response) {
                    console.log(response);
                    if(response.outcome_code === 0){
                        alert("Album disliked");
                    }
                    else if(response.outcome_code === 1)
                        alert("Dislike addition unsuccessful");
                    else
                        alert("Error occurred while removing like to album");
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

    // For each song find with the ajax request, create his container with all his information and append it in the main container
    // for the liked songs
    liked.forEach(function (song_tmp){
        let songDiv = $("<div class=\"d-flex song_liked gap-1 p-1 align-items-center mb-1\"></div>");
        songDiv.append($("<img class=\"album-cover shadow\" style=\"max-width: 100px;\">").attr("src", song_tmp.coverUrl));
        let songInf = $("<div class=\"d-flex flex-column song_inf\"></div>");
        songDiv.append(songInf);
        songInf.append($("<h3 style=\"margin-bottom:0; font-weight: bold\"></h3>").text(song_tmp.songName));
        songInf.append($("<p style=\"font-size: large; margin-top:0; margin-bottom: 0;\"></p>").text(song_tmp.albumName));
        songInf.append($("<p style=\"font-size: large; margin-top:0; margin-bottom: 0;\"></p>").text(song_tmp.artistName));

        // If we are in our profile page, for each song we like we have the button to dislike it
        if(window.location.href.includes("profilePage")){
            songDiv.append($("<button id=\"dislike_S_btn_" + increment + "\" class=\"btn btn-danger ms-auto\">Dislike</button>"))
        }
        $(songInf).click(function (){
            // Before go to the page with the details of the album of the selected song, we have to encode the special characters
            let send_string_artist;
            if(song_tmp.artistName.includes(",")){
                let artist_parts = song_tmp.artistName.split(",");
                let substring = artist_parts[0];
                send_string_artist = encodeURIComponent(substring)
            } else {
                send_string_artist = encodeURIComponent(song_tmp.artistName);
            }
            window.location.href = "/albumDetails?title=" + encodeURIComponent(song_tmp.albumName) + "&artist=" + send_string_artist;
        })
        container.append(songDiv);
        let id="dislike_S_btn_" + increment;
        $("#" + id).click(function () {
            // AJAX call to dislike a song, for each song liked
            let songContainer = $(this).closest(".song_liked");
            let username = $("#username").text();
            let albumTitle = song_tmp.albumName;
            let artistsAsString = song_tmp.artistName;
            let songTitle = songContainer.find("h3").first().text();

            $.ajax({
                url: '/api/albumDetails/removeLikesSong',
                dataType: 'json',
                type: 'POST',
                data: {
                    username: username,
                    albumTitle: albumTitle,
                    artists: artistsAsString,
                    songName: songTitle
                },
                success: function (response) {
                    if(response.outcome_code == 0){
                        alert("Song disliked");
                    }
                    else if(response.outcome_code == 1)
                        alert("Dislike addition unsuccessful");
                    else
                        alert("Error occurred while removing like to song");
                },
                error: function (xhr, status, error) {
                    console.log("Error: " + error);
                }
            });
        });
        increment++;
    })
}