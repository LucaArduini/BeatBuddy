$(document).ready(function() {
    let user = $("#username").text();
    // Aggiungi l'evento click all'elemento "followed-users"
    $("#followed_users_btn").click(function() {
        $("#followed_container").empty(); // Svuota il contenuto del div
        // Esegui una richiesta Ajax per ottenere i dati (ad esempio, una lista di nomi)
        $.ajax({
            url: "",  // Sostituisci con l'URL corretto per ottenere i dati
            data: user,
            dataType: 'json',
            method: "POST",
            success: function(result) {
                result = JSON.parse(result);
                if(!result){
                    $("#followed_container").append("No user followed.");
                }else{

                }
            },
            error: function() {
                alert("Errore nella richiesta Ajax.");
            }
        });
    });
    $("#show_follower_btn").click(function(){
        $.ajax()
    });
    $("#liked_albums_btn").click(function (){
        $.ajax()
    });
    $("#liked_songs_btn").click(function (){
        $.ajax()
    });
});
function appendFollowedUser(name) {
    var container = $("#followed_container");
    var newDiv = $("<div>").text(name);
    container.append(newDiv);
}