$(document).ready(function (){
    $(".song-container").click(function (){
        let name = $("#artist_name").text();
        let encoded_name = encodeURIComponent(name);
        let album = $(this).find("#album_title").text();
        let encoded_album = encodeURIComponent(album);

        window.location.href = '/albumDetails?title=' + encoded_album + '&artist=' + encoded_name;
    });
});