$(document).ready(function () {
    // Ascoltatore per il click sul bottone "Search"
    $("#search_btn").click(function (e) {
        e.preventDefault();
        search();
    });

    // Ascoltatore per il tasto "Invio" nel campo di input
    $("#search_input").on("keydown", function (e) {
        if (e.keyCode === 13) { // Codice 13 è il tasto "Invio"
            e.preventDefault();
            e.stopPropagation();
            search();
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

                if(arrayResults.length == 0) {
                    alert("No results found");
                    return;
                }
                else if(category == "album") {
                    displayAlbums(arrayResults);
                }
                else {
                    alert("Error: unknown category");
                }

                console.log(arrayResults);

            },
            error: function (xhr, status, error) {
                console.error(error);
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
            window.location.href = '/albumPage?albumId=' + album.id;
        });

        // Aggiungi il div al container
        container.append(albumDiv);
    });
}
