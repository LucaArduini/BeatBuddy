$(document).ready(function () {
    $('#signup_btn').click(function (e) {
        e.preventDefault();

        // Raccolgo i dati del form
        const name = $('#name_input').val();
        const surname = $('#surname_input').val();
        const username = $('#username_input').val();
        const password = $('#password_input').val();
        const repeatPassword = $('#repeat_password_input').val();
        const birthday = $('#birthday_input').val();
        const email = $('#email_input').val();

        // Controllo se tutti i campi sono stati riempiti
        if (!name || !surname || !username || !password ||
            !repeatPassword || !birthday || !email) {
            alert("Please fill in all fields.");
            return;
        }

        // Controllo se le password coincidono
        if (password !== repeatPassword) {
            alert("Passwords do not match. Please re-enter them.");
            return;
        }

        // Controllo se la email è valida
        if (!isValidEmail(email)) {
            alert("Please enter a valid email address.");
            return;
        }

        // Dati da inviare al server
        const formData = {
            name: name,
            surname: surname,
            username: username,
            password: password,
            birthday: birthday,
            email: email
        };

        // Procedo con la chiamata AJAX
        $.ajax({
            url: '/api/signup',
            data: formData,
            dataType: 'json',
            method: 'POST',

            success: function(response) {
                switch(response.outcome_code) {
                    case 0:
                        alert("Registration successful!\nYou can now proceed with the login.");
                        window.location.href = "/login";
                        break;
                    case 1:
                        alert("Username already in use. Please choose another username.");
                        break;
                    case 2:
                        alert("Email already in use. Please use a different email.");
                        break;
                    case 3:
                        alert("Error in MongoDB connection.");
                        break;
                    case 4:
                        alert("Other error in MongoDB.");
                        break;
                    case 5:
                        alert("Error during user registration in MongoDB.");
                        break;
                    case 6:
                        alert("Error during user registration in Neo4j.");
                        break;
                    case 7:
                        alert("Error in Neo4j connection.");
                        break;
                    default:
                        alert("Unknown error.");
                }
            },
            error: function (xhr, status, error) {
                alert("ERROR: " + error);
            }
        });
    });
});

// Funzione per verificare il formato dell'email
function isValidEmail(email) {
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailRegex.test(email);
}
