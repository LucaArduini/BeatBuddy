$(document).ready(function () {
    $('#logout_btn').click(function (e) {
        e.preventDefault();

        $.ajax({
            url: "/api/logout",
            dataType: 'json',
            method: "POST",
            contentType: 'application/json', // Aggiungi questa linea

            success: function(response) {
                var result = jQuery.parseJSON(response);

                if(result["outcome_code"] == 0) {
                    alert("OK BRO, you did it");    //###
                    window.location.href = "/";
                }
                else {
                    alert("Si è verificato un problema durante il logout");
                }
            },
            error: function (xhr, status, error) {
                alert("Error: " + error);
            }
        });
    });
});
