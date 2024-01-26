$(document).ready(function (){
    document.getElementById("update_likes_btn").addEventListener("click", function() {
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
                        alert('No new likes found.');
                        break;
                    case 3:
                        alert('Error while updating new likes.');
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

    document.getElementById("calculate_ranking_btn").addEventListener("click", function() {
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
                    default:
                        alert('Unknown error occurred.');
                }
            })
            .catch((error) => {
                console.error('Error:', error);
                alert('An error occurred during the request.');
            });
    });
})