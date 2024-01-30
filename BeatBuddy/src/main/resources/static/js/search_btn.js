$(document).ready(function () {
    // Ascoltatore per il click sul bottone "Search"
    $("#search_btn").click(function (e) {
        e.preventDefault();
        search();
        $('#search_results').modal('show');
    });

    // Ascoltatore per il tasto "Invio" nel campo di input
    $("#search_input").on("keydown", function (e) {
        if (e.keyCode === 13) { // Codice 13 è il tasto "Invio"
            e.preventDefault();
            e.stopPropagation();
            search();
            $('#search_results').modal('show');
        }
    });

    // Funzione di ricerca
    function search() {
        const searchTerm = $("#search_input").val();
        const category = $("#category_input").val();

        // Codice AJAX qui
        $.ajax({
            url: '/api/search',
            data: { term: searchTerm, category: category },
            dataType : 'json',  // indico che la risposta deve essere deserializzata come JSON
            method: 'GET',

            success: function (arrayResults) {
                if(arrayResults==null || arrayResults.length == 0) {
                    const container = $(".modal-body"); // Sostituisci con l'ID del tuo container HTML
                    container.empty(); // Pulisci il contenuto precedente
                    container.append($("<p>No results found</p>"));
                }
                else if(category == "album") {
                    displayAlbums(arrayResults);
                }
                else if(category == "song") {
                    displaySongs(arrayResults);
                }
                else if(category == "artist") {
                    displayArtists(arrayResults);
                }
                else if(category == "user") {
                    displayUser(arrayResults);
                }
            },
            error: function (xhr, status, error) {
                console.error("Error: " + error);
            }
        });
    }
});

function displayAlbums(albums) {
    const container = $(".modal-body"); // Sostituisci con l'ID del tuo container HTML
    container.empty(); // Pulisci il contenuto precedente

    albums.forEach(function(album) {
        // Crea un div per ogni AlbumDTO
        let albumDiv = $("<div id=\"album_info\" class=\"d-flex mb-1 align-items-center\"></div>");
        albumDiv.append($("<img id=\"album_cover\" class=\"album-cover-sm mt-1 mb-1\">").attr("src", album.coverURL));
        let albumInf = $("<div id=\"album_details\" class=\"album-details-sm d-flex flex-column mt-1\"></div>");
        albumDiv.append(albumInf);
        albumInf.append($("<h4 id=\"album_title\" style=\"font-weight: bold; margin-bottom: 0;\"></h4>").text(album.title));
        let div1 = $("<div class=\"d-flex m-0\"></div>");
        albumInf.append(div1);
        div1.append($("<p style=\"font-size: medium;\" id=\"album_artists\"></p>").text(album.artists.join(", ")));

        // Aggiungi un ascoltatore di eventi click al div dell'album
        albumDiv.click(function() {
            window.location.href = '/albumDetails?albumId=' + album.id;
        });

        // Aggiungi il div al container
        container.append(albumDiv);
    });
}
function displaySongs(songs) {
    const container = $(".modal-body"); // Sostituisci con l'ID del tuo container HTML
    container.empty(); // Pulisci il contenuto precedente

    songs.forEach(function(song) {
        // Crea un div per ogni AlbumDTO
        let songDiv = $("<div id=\"song_info\" class=\"d-flex mb-1 align-items-center\"></div>");
        songDiv.append($("<img id=\"song_cover\" class=\"song-cover-sm mt-1 mb-1\">").attr("src", song.coverURL));
        let songInf = $("<div id=\"song_details\" class=\"song-details-sm d-flex flex-column mt-1\"></div>");
        songDiv.append(songInf);
        songInf.append($("<h4 id=\"song_title\" style=\"font-weight: bold; margin-bottom: 0;\"></h4>").text(song.name));
        let div1 = $("<div class=\"d-flex m-0\"></div>");
        songInf.append(div1);
        div1.append($("<p style=\"font-size: medium;\" id=\"song_artists\"></p>").text(song.artists.join(", ")));

        // Aggiungi un ascoltatore di eventi click al div dell'album
        songDiv.click(function() {
            window.location.href = '/albumDetails?albumId=' + song.albumId;
        });

        // Aggiungi il div al container
        container.append(songDiv);
    });
}
function displayArtists(artists) {
    const container = $(".modal-body"); // Sostituisci con l'ID del tuo container HTML
    container.empty(); // Pulisci il contenuto precedente

    artists.forEach(function(artist) {
        // Crea un div per ogni AlbumDTO
        let artDiv = $("<div id=\"artist_info\" class=\"d-flex mb-1 align-items-center\"></div>");
        artDiv.append($("<img id=\"artist_img\" class=\"artist-img-sm mt-1 mb-1\">").attr("src", artist.profilePicUrl));
        artDiv.append($("<h3 id=\"artist_name\" style=\"font-weight: bold; margin-bottom: 0;\"></h3>").text(artist.name));

        // Aggiungi un ascoltatore di eventi click al div dell'album
        artDiv.click(function() {
            window.location.href = '/artistDetails?artistId=' + artist.id;
        });

        // Aggiungi il div al container
        container.append(artDiv);
    });
}
function displayUser(users){
    const container = $(".modal-body");
    container.empty();

    users.forEach(function (user){
        let usDiv = $("<div id=\"user_info\" class=\"d-flex mb-1 align-items-center\"></div>");
        usDiv.append($("<i class=\"fa fa-user-circle fa-3x me-3\"></i>"))
        let usInf = $("<div class=\"d-flex flex-column\"></div>");
        usDiv.append(usInf);
        usInf.append($("<h3 id=\"user_name\" style=\"font-weight: bold; margin-bottom: 0;\"></h3>").text(user.username));
        usInf.append($("<p id=\"user_full_name\"></p>").text(user.name + " " + user.surname));

        // Aggiungi un ascoltatore di eventi click al div dell'album
        usDiv.click(function() {
            window.location.href = '/user?username=' + user.username;
        });

        // Aggiungi il div al container
        container.append(usDiv);
    });
}