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

                // if(arrayResults.length == 0) {
                //     alert("No results found");
                //     return;
                // }
                // else if(category == "album") {
                //     console.log("album");
                // }
                // else {
                //     alert("Error: unknown category");
                // }

                console.log(arrayResults);

            },
            error: function (xhr, status, error) {
                console.error(error);
            }
        });    }
});