$(document).ready(function () {
	// .ready(...) è un metodo di jQuery che fa sì che il codice venga eseguito quando il DOM è pronto.
	// È simile alla window.onload di JavaScript.

	// Ascoltatore per l'evento "keydown" sulla casella di input della password
	$("#password").on("keydown", function (e) {
		// Verifica se il tasto premuto è il tasto "Invio" (codice 13)
		if (e.keyCode === 13) {
			// Se il tasto "Invio" è stato premuto, chiama la funzione di login
			login();
		}
	});

	// Ascoltatore per il click sul bottone "Login"
	$("#btn_login").click(function (e) {
		login();
	});

	// Funzione di login
	function login() {
        const tmp_usrn = $("#username").val();
        const tmp_pwd = $("#password").val();

		$.ajax({
			// Il percorso /api/login è l'URL a cui viene inviata la richiesta.
			url: "/api/login",
			// I dati inviati sono l'username e la password raccolti dai campi di input.
			data: { username: tmp_usrn, password: tmp_pwd },
			// Specifica che il tipo di dati attesi in risposta è json.
			dataType: 'json',
			method: "post",

			// Una volta ricevuta la risposta dal server, questa funzione viene eseguita.
			// La funzione success viene eseguita quando la richiesta ha successo.
			success: function (response) {
			var result = jQuery.parseJSON(response);

			if (result["outcome_code"] == 0) {
				alert("OK FRà");
				// Reindirizza l'utente alla pagina index
				window.location.href = "/";
			} else if (result["outcome_code"] == 1) {
				alert("Incorrect username");
				$("#username").val("");
				$("#password").val("");
			} else if (result["outcome_code"] == 2) {
				alert("Incorrect password");
				$("#username").val("");
				$("#password").val("");
			} else {
				alert("Error: unknown outcome_code");
			}
		},
		// La funzione error viene eseguita quando la richiesta fallisce.
		error: function (xhr, status, error) {
			alert("Error: " + error);
		}
        });
	}
});
