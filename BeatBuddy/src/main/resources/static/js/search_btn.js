$(document).ready(function () {
    $("#search_btn").click(function (e) {
        e.preventDefault();

        const searchTerm = $("#search_input").val();
        const category = $("#category_input").val();

        $.ajax({
            url: '/api/search',
            data: { term: searchTerm, category: category },
            method: 'GET',

            success: function (response) {
                // Deserializza il JSON ricevuto
                const arrayResults = jQuery.parseJSON(response);

                if(arrayResults.length == 0) {
                    alert("No results found");
                    return;
                }
                else if(category == "album") {
                    //displayAlbums(arrayResults);
                    ;
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
    });
});

function displayAlbums(albums) {
    const container = $("#someContainer"); // Sostituisci con l'ID del tuo container HTML
    container.empty(); // Pulisci il contenuto precedente

    albums.forEach(function(album) {
        // Crea un div per ogni AlbumDTO
        let albumDiv = $("<div class='album'></div>");
        albumDiv.append($("<h3></h3>").text(album.title));
        albumDiv.append($("<img>").attr("src", album.coverURL));
        albumDiv.append($("<p></p>").text("Artists: " + album.artists.join(", ")));

        // Aggiungi il div al container
        container.append(albumDiv);
    });
}