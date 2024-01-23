$(document).ready(function (){
    $(".song-container").click(function (){
        let name = $("#artist_name").text();
        let album = $(this).find("#album_title").text();

        //alert(name + ' ' + album);
        window.location.href = '/albumDetailsFromArtist?albumTitle=' + album + '&albumArtist=' + name;
    });
});