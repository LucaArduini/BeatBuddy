$(document).ready(function () {
    $("#login_btn").click(function (e) {
        e.preventDefault();
        e.stopPropagation();
		login();
	});

    $("#password_input").on("keydown", function (e) {
        // Check if the pressed key is the "Enter" key (code 13)
        if (e.keyCode === 13) {
            e.preventDefault();
            login();
        }
    });

	function login() {
        const tmp_usrn = $("#username_input").val();
        const tmp_pwd = $("#password_input").val();

		$.ajax({
			url: '/api/login',
			data: {
                username: tmp_usrn,
                password: tmp_pwd
            },
            dataType : 'json',
            method: 'POST',
			success: function (outcome) {
                if (outcome["outcome_code"] == 0) {
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
            error: function (xhr, status, error) {
                console.log("Error: " + error);
            }
        });
	}
});
