/*$(document).ready(function () {
    $('#view_all_review_btn').click(function (e) {
        e.preventDefault();

        // Procedo con la chiamata AJAX
        $.ajax({
            url: '/api/albumReviews',
            data: DATO_da_inviare,
            dataType: 'json',
            method: 'POST',

            success: function(response) {

            },
            error: function (xhr, status, error) {
                alert("ERROR: " + error);
            }
        });
    });
});*/