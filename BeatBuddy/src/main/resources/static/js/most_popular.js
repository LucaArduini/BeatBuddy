$(document).ready(function (){
    $(".carousel-caption").click(function (e){
        e.preventDefault();
        let id = $(this).attr("id");
        let type;
        if(id == "most_popular_albums")
            type = "albums";
        else if(id == "most_popular_artists")
            type = "artists";
        else
            type = "songs";

        window.location.href = '/mostPopularPage?type=' + type;
    })
})