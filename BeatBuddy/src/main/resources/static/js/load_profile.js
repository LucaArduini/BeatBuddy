$(document).ready(function() {
    // Aggiungi l'evento click all'elemento "followed-users"
    $("#followed_users_btn").click(function() {
        $("#followed_container").empty(); // Svuota il contenuto del div
        appendFollowedUser("Nome di Esempio 1");
        appendFollowedUser("Nome di Esempio 2");
        appendFollowedUser("Nome di Esempio 3");
        // Esegui una richiesta Ajax per ottenere i dati (ad esempio, una lista di nomi)
        /*$.ajax({
            url: "/getFollowedData",  // Sostituisci con l'URL corretto per ottenere i dati
            method: "GET",
            success: function(data) {
                // Dati ricevuti con successo, crea gli elementi e aggiungili al div "followed_container"
                var container = $("#followed_container");

                // Supponiamo che data sia un array di nomi
                data.forEach(function(name) {
                    // Crea un nuovo elemento div per ogni nome
                    var newDiv = $("<div>").text(name);
                    container.append(newDiv);  // Aggiungi l'elemento al container
                });
            },
            error: function() {
                alert("Errore nella richiesta Ajax.");
            }
        });*/
    });
    $("#show_follower_btn").click(function(){
        let container = $("#follower_number");
        container.empty();
        let newDiv = $("<span>").text("2000");
        container.append(newDiv);
    });
    $("#liked_albums_btn").click(function (){
        let container = $("#liked_albums_container");
        container.empty();
        let newDiv = $("<span>").text("Canzone1");
        container.append(newDiv);
    });
    $("#liked_songs_btn").click(function (){
        let container = $("#liked_songs_container");
        container.empty();
        let newDiv = $("<span>").text("Canzone1");
        container.append(newDiv);
    });
    $("#try").click(function (){
        let container = $("#collapseExample");
        container.empty();
        let newDiv = $("<span>").text("Canzone3");
        container.append(newDiv);
    })
});
function appendFollowedUser(name) {
    var container = $("#followed_container");
    var newDiv = $("<div>").text(name);
    container.append(newDiv);
}