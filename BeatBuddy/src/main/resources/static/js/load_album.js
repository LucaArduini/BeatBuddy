$(document).ready(function (){
    let title = $('#album_title');
    let cover = $('#album_cover');
    let artists = $('#album_artists_page');
    let release_date = $('#release_date_page');
    let duration = $('#album_duration_page');
    let number_of_likes = $('#number_of_likes_album');
    let average_rating = $('#average_rating');
    let last_reviews = $('#latest_reviews');

    $.ajax({
        url : "",
        method : "get",
        success : function(response){
            const arrayResults = jQuery.parseJSON(response);
            title.text(arrayResults['title']);
            cover.attr("src", arrayResults['coverUrl']);
            artists.text(arrayResults['artists']);
            release_date.text(arrayResults['year']);
            // duration.text(arrayResults['duration']);
            number_of_likes.text(arrayResults['likes']);
            average_rating.text(arrayResults['averageRating']);
            let result_songs = arrayResults['songs'];
            for (s in result_songs){
                let html = '' +
                    '<div class="song-container d-flex justify-content-between align-items-center p-1">\n' +
                    '<div class="d-flex flex-column ps-1 pt-1">\n' +
                    '<p style="font-size: large; font-weight: bold;" id="song_title" class="mt-1 mb-0">' + arrayResults[s].title + '</p>' +
                    '<p style="font-size: medium;" id="song_duration" class="mt-1"><i class="fas fa-clock me-1"></i>' + arrayResults[s].duration + '</p>' +
                    '</div>' +
                    '<div id="like_container" class="d-flex align-items-center">' +
                    '<button id="like_song_btn" class="btn btn-primary" type="button">' +
                    '<i class="fas fa-heart me-1"></i>Like' +
                    '</button>' +
                    '</div>' +
                    '</div>'
                $('#album_songs').append(html)
            }
        }
    })
})