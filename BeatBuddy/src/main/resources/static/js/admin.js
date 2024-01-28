$(document).ready(function (){
    /*document.getElementById("updateLikesButton").addEventListener("click", function() {
        fetch('/api/admin/updateNewLikes', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
                // Include any necessary headers here
            }
        })
            .then(response => response.json())
            .then(data => {
                alert('Response: ' + data.outcome_code);
                switch(data.outcome_code) {
                    case 0:
                        alert('Update successful!');
                        break;
                    case 1:
                        alert('User not found or unauthorized.');
                        break;
                    case 2:
                        alert('Error while updating new likes (for albums).');
                        break;
                    case 3:
                        alert('Error while updating new likes (for songs).');
                        break;
                    case 4:
                        alert('Error while updating average rating.');
                        break;
                    case 10:
                        alert('Database connection error.');
                        break;
                    default:
                        alert('Unknown error occurred.');
                }
            })
            .catch((error) => {
                console.error('Error:', error);
                alert('An error occurred during the request.');
            });
    });

    document.getElementById("calculateRankingsButton").addEventListener("click", function() {
        fetch('/api/admin/calculateRankings', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
                // Include any necessary headers here
            }
        })
            .then(response => response.json())
            .then(data => {
                alert('Response: ' + data.outcome_code);
                switch(data.outcome_code) {
                    case 0:
                        alert('Ranking update successful!');
                        break;
                    case 1:
                        alert('User not found or unauthorized.');
                        break;
                    case 2:
                        alert('No albums found (sorted by rating).');
                        break;
                    case 3:
                        alert('No albums found (sorted by likes).');
                        break;
                    case 4:
                        alert('No songs found (sorted by likes).');
                        break;
                    case 10:
                        alert('Database connection error.');
                        break;
                    case 11:
                        alert('Error while writing to file.');
                        break;
                    case 12:
                        alert('Error while clearing rankings directory.');
                        break;
                    default:
                        alert('Unknown error occurred.');
                }
            })
            .catch((error) => {
                console.error('Error:', error);
                alert('An error occurred during the request.');
            });
    });

    document.getElementById("calculateStatsButton").addEventListener("click", function() {
        fetch('/api/admin/calculateAdminStats', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
                // Include any necessary headers here
            }
        })
            .then(response => response.json())
            .then(data => {
                switch(data.outcome_code) {
                    case 0:
                        document.getElementById("dailyLikesOnAlbums").innerText = "Daily Likes on Albums: " + data.admin_stats.dailyLikesOnAlbums;
                        document.getElementById("dailyLikesOnSongs").innerText = "Daily Likes on Songs: " + data.admin_stats.dailyLikesOnSongs;
                        document.getElementById("dailyReviews").innerText = "Daily Reviews: " + data.admin_stats.dailyReviews;
                        alert('Admin stats updated successfully!');
                        break;
                    case 1:
                        alert('User not found or unauthorized.');
                        break;
                    case 10:
                        alert('Database connection error.');
                        break;
                    case 11:
                        alert('Error while writing to file.');
                        break;
                    default:
                        alert('Unknown error occurred.');
                }
            })
            .catch((error) => {
                console.error('Error:', error);
                alert('An error occurred during the request.');
            });
    });*/
    $("#calculateStatsButton").click(function () {
        $.ajax({
            url: '/api/admin/calculateAdminStats',
            method: 'POST',
            dataType: 'json',
            success: function (data) {
                alert("Response" + data.outcome_code);
                switch (data.outcome_code) {
                    case 0:
                        $("#dailyLikesOnAlbums").text("Daily Likes on Albums: " + data.admin_stats.dailyLikesOnAlbums);
                        $("#dailyLikesOnSongs").text("Daily Likes on Songs: " + data.admin_stats.dailyLikesOnSongs);
                        $("#dailyReviews").text("Daily Reviews: " + data.admin_stats.dailyReviews);
                        alert('Admin stats updated successfully!');
                        break;
                    case 1:
                        alert('User not found or unauthorized.');
                        break;
                    case 10:
                        alert('Database connection error.');
                        break;
                    case 11:
                        alert('Error while writing to file.');
                        break;
                    default:
                        alert('Unknown error occurred.');
                }
            },
            error: function (xhr, status, error) {
                console.error('Error:', error);
                alert('An error occurred during the request.');
            }
        });
    });

    $("#calculateRankingsButton").click(function () {
        $.ajax({
            url: '/api/admin/calculateRankings',
            method: 'POST',
            dataType: 'json',
            success: function (data) {
                alert('Response: ' + data.outcome_code);
                switch (data.outcome_code) {
                    case 0:
                        alert('Ranking update successful!');
                        break;
                    case 1:
                        alert('User not found or unauthorized.');
                        break;
                    case 2:
                        alert('No albums found (sorted by rating).');
                        break;
                    case 3:
                        alert('No albums found (sorted by likes).');
                        break;
                    case 4:
                        alert('No songs found (sorted by likes).');
                        break;
                    case 10:
                        alert('Database connection error.');
                        break;
                    case 11:
                        alert('Error while writing to file.');
                        break;
                    case 12:
                        alert('Error while clearing rankings directory.');
                        break;
                    default:
                        alert('Unknown error occurred.');
                }
            },
            error: function (xhr, status, error) {
                console.error('Error:', error);
                alert('An error occurred during the request.');
            }
        });
    });

    $("#updateLikesButton").click(function () {
        $.ajax({
            url: '/api/admin/updateNewLikes',
            method: 'POST',
            dataType: '/json',
            success: function (data) {
                alert('Response: ' + data.outcome_code);
                switch (data.outcome_code) {
                    case 0:
                        alert('Update successful!');
                        break;
                    case 1:
                        alert('User not found or unauthorized.');
                        break;
                    case 2:
                        alert('Error while updating new likes (for albums).');
                        break;
                    case 3:
                        alert('Error while updating new likes (for songs).');
                        break;
                    case 4:
                        alert('Error while updating average rating.');
                        break;
                    case 10:
                        alert('Database connection error.');
                        break;
                    default:
                        alert('Unknown error occurred.');
                }
            },
            error: function (xhr, status, error) {
                console.error('Error:', error);
                alert('An error occurred during the request.');
            }
        });
    });

    $("#most_popular_albums").click(function (e){
        e.preventDefault();
        window.location.href = '/mostPopularPage?type=albums';
    });

    $("#most_popular_songs").click(function (e){
        e.preventDefault();
        window.location.href = '/mostPopularPage?type=songs';
    });

    $("#most_popular_artists").click(function (e){
        e.preventDefault();
        window.location.href = '/mostPopularPage?type=artists';
    });
})