$(document).ready(function () {
	// .ready(...) è un metodo di jQuery che fa sì che il codice venga eseguito quando il DOM è pronto.
	// È simile alla window.onload di JavaScript.

	// Ascoltatore per il click sul bottone "Login"
	$("#login_btn").click(function (e) {
        e.preventDefault();
        e.stopPropagation();
		login();
	});

    // Ascoltatore per l'evento "keydown" sulla casella di input della password
    $("#password_input").on("keydown", function (e) {
        // Verifica se il tasto premuto è il tasto "Invio" (codice 13)
        if (e.keyCode === 13) {
            // Se il tasto "Invio" è stato premuto, chiama la funzione di login
            e.preventDefault();
            login();
        }
    });

	// Funzione di login
	function login() {
        const tmp_usrn = $("#username_input").val();
        const tmp_pwd = $("#password_input").val();

        //alert("Username: " + tmp_usrn + "\nPassword: " + tmp_pwd);

		$.ajax({
			// Il percorso /api/login è l'URL a cui viene inviata la richiesta.
			url: '/api/login',
			// I dati inviati sono l'username e la password raccolti dai campi di input.
			data: {
                username: tmp_usrn,
                password: tmp_pwd
            },
            dataType : 'json',  // indico che la risposta deve essere deserializzata come JSON
            method: 'POST',

			// Una volta ricevuta la risposta dal server, questa funzione viene eseguita.
			// La funzione success viene eseguita quando la richiesta ha successo.
			success: function (outcome) {
                if (outcome["outcome_code"] == 0) {
                    //alert("OK FRà");
                    // Reindirizza l'utente alla pagina index
                    window.location.href = "/homePage";
                } else if (outcome["outcome_code"] == 1) {
                    alert("Username not found in the database. Please make sure you have typed the username correctly.");
                    $("#username_input").val("");
                    $("#password_input").val("");
                } else if (outcome["outcome_code"] == 2) {
                    alert("Incorrect password");
                    $("#password_input").val("");
                }
                else if (outcome["outcome_code"] == 3) {
                    alert("Unable to connect to the database :-(\nPlease try again later.");
                }
                else {
                    alert("Error: unknown outcome_code");
                }
            },
            // La funzione error viene eseguita quando la richiesta fallisce.
            error: function (xhr, status, error) {
                alert("ERRORE QUI 0");  //###
                alert("Error: " + error);
            }
        });
	}
});
