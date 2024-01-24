$(document).ready(function () {
    $('#logout_btn').click(function (e) {
        e.preventDefault();

        $.ajax({
            url: '/api/logout',
            dataType : 'json',  // indico che la risposta deve essere deserializzata come JSON
            method: 'POST',

            success: function(outcome) {
                if(outcome["outcome_code"] == 0) {
                    //alert("OK BRO, you did it");    //###
                    window.location.href = "/";
                }
                else {
                    alert("Si è verificato un problema durante il logout");
                }
            },
            error: function (xhr, status, error) {
                alert("ERRORE QUI 1");  //###
                alert("Error: " + error);
            }
        });
    });
});
