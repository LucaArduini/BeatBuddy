/*function scrollToTop() {
  document.body.scrollTop = 0;
  document.documentElement.scrollTop = 0;
}

$(document).ready(function (){
    $('#album_info').click(function (e){
        window.location.href = "/homePage";
    })
})

window.onscroll = function() { scrollFunction() };

function scrollFunction() {
  if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
      document.getElementById("back-to-top-btn").style.display = "block";
  } else {
      document.getElementById("back-to-top-btn").style.display = "none";
  }
}
function searchButton(event) {
  let dropdown = document.getElementById("category_input")
  let category = dropdown.options[dropdown.selectedIndex].value
  if(category == "song")
      searchSong(event)
  else if(category == "album")
      searchAlbum()
  else if(category == "artist")
      searchArtist(event)
}
function searchSong(event) {
  event.preventDefault()
  let songs = [
      {
          title: "Ciao Bella",
          artists: "Sfera Ebbasta, ANNA",
          url: "https://images.genius.com/19dd0c7303164797828221f0d4896353.1000x1000x1.jpg"
      },
      {
          title: "Ciao Ciao",
          artists: "La rappresentante di lista",
          url: "https://www.radioparma.it/wp-content/uploads/2022/02/rappresentante-di-lista_cover_ciaociao-2-1024x1024.jpg"
      },
      {
          title: "Ciao Baby",
          artists: "Gemitaiz",
          url: "https://i1.sndcdn.com/artworks-uIshI6IXddx0-0-t500x500.jpg"
      }
  ];
  // Get the search query
  let query = document.getElementById('search_input').value.toLowerCase();
  let searchResultDiv = document.getElementById('search-result');

  // Clear previous results
  searchResultDiv.innerHTML = '';

  songs.forEach(element => {
    let nameLower = element.title.toLowerCase();
    if (nameLower.includes(query)) {
        let resCon = document.createElement("div");
        resCon.className = "result-container col-md-2 d-flex flex-column align-items-center justify-content-center";
        resCon.innerHTML = `
            <div class="result-content w-75 pt-3">
            <img src="${element.url}" class="img-fluid rounded-circle">
            <h6 class="mt-3">${element.title}</h6>
            <p style="font-size: small;">${element.artists}</p>
            </div>
        `;
        searchResultDiv.appendChild(resCon);
    }
  });

  if(searchResultDiv.innerHTML === '')
    searchResultDiv.innerHTML = '<p>Nessun risultato trovato</p>'
}
function searchArtist(event) {
  event.preventDefault()
  let artists = [
      {
          name: "Sfera Ebbasta",
          profilePicUrl: "https://www.azalea.it/wp-content/uploads/2022/12/123986267_3911923168837741_2131548758059792753_n-e1671194204392.jpg"
      },
      {
          name: "Massimo Pericolo",
          profilePicUrl: "https://www.google.it/url?sa=i&url=https%3A%2F%2Fopen.spotify.com%2Fartist%2F1El4YQA8oCXX7ynFSxRTFq&psig=AOvVaw1nBPySrbbgm-LCssHBRSGM&ust=1705399434464000&source=images&cd=vfe&opi=89978449&ved=0CBIQjRxqFwoTCJiDppGS34MDFQAAAAAdAAAAABAD"
      },
      {
          name: "Charlie Puth",
          profilePicUrl: "https://pyxis.nymag.com/v1/imgs/968/02e/c1da8272108887dfd7c3d090524576ebfe-charlie-puth.1x.rsquare.w1400.jpg"
      },
      {
        name: "Sfera Ebbasta",
        profilePicUrl: "https://www.azalea.it/wp-content/uploads/2022/12/123986267_3911923168837741_2131548758059792753_n-e1671194204392.jpg"
      },
      {
        name: "Sfera Ebbasta",
        profilePicUrl: "https://www.azalea.it/wp-content/uploads/2022/12/123986267_3911923168837741_2131548758059792753_n-e1671194204392.jpg"
      },
      {
        name: "Sfera Ebbasta",
        profilePicUrl: "https://www.azalea.it/wp-content/uploads/2022/12/123986267_3911923168837741_2131548758059792753_n-e1671194204392.jpg"
      },
  ];
  // Get the search query
  let query = document.getElementById('search_input').value.toLowerCase();
  let searchResultDiv = document.getElementById('search-result');

  // Clear previous results
  searchResultDiv.innerHTML = '';

  artists.forEach(element => {
    let nameLower = element.name.toLowerCase();
    if (nameLower.includes(query)) {
        let resCon = document.createElement("div");
        resCon.className = "result-container col-md-2 d-flex flex-column align-items-center justify-content-center";
        resCon.innerHTML = `
            <div class="result-content w-75 pt-3">
            <img src="${element.profilePicUrl}" class="img-fluid rounded-circle">
            <h6 class="mt-3">${element.name}</h6>
            </div>
        `;
        searchResultDiv.appendChild(resCon);
    }
  });

  if(searchResultDiv.innerHTML === '')
    searchResultDiv.innerHTML = '<p>Nessun risultato trovato</p>'
}*/