import requests
import sys
import time
import json
import os

global access_token, client_id, client_secret

access_token = ''
client_id = ''
client_secret = ''


def is_token_valid(token):
    global access_token
    url = "https://api.spotify.com/v1/me"
    headers = {"Authorization": f"Bearer {token}"}
    response = requests.get(url, headers=headers)

    if response.status_code == 200:
        return True
    elif response.status_code == 401:
        return False
    else:
        print(f"Error: {response.status_code} in tracklist")
        print("headers", response.headers.get("retry-after"))
        return False



def get_credentials(path):
    global client_id, client_secret
    with open(path, "r") as f:
        lines = f.read().splitlines()

    client_id = str(lines[0])
    client_secret = str(lines[1])


def init():
    global access_token

    if len(sys.argv) < 2:
        print("Error: not enough arguments")
        return False
    else:
        get_credentials(sys.argv[1])

    url = "https://accounts.spotify.com/api/token"
    headers = {"Content-Type": "application/x-www-form-urlencoded"}
    data = {
        "grant_type": "client_credentials",
        "client_id": client_id,
        "client_secret": client_secret
    }

    response = requests.post(url, headers=headers, data=data)

    with open("spotipy/token.txt", "w") as f:
        f.write(response.text)

    access_token = response.json()["access_token"]
    print(f"Access token: {access_token}")

    print("Token obtained")

def find10kArtists():
    #function to find first 10k artists on spotify

    print("ok")

    with open("spotipy/lastArtistRetrieved.txt", "r") as f:
        artists = f.readlines()

        #find the last name in the file
        lastArtistName = artists[-1].strip().split(",")[1]
        print(f"Last artist retrieved: {lastArtistName}")

        #search that artist in 10kArtists.txt
        with open("spotipy/10kArtists.txt", "r") as f:
            artists = f.readlines()
            for i in range(0, len(artists)):
                if artists[i].split(",")[2] == lastArtistName:
                    artists = artists[i:]
                    break
        count = int(artists[0].split(",")[0]) if artists[0].split(",")[0] != '' else 0

        print("Restarting from", artists[0].split(",")[2], "at counter", count)


        artistsName, artistIndex = [line.split(",")[2] for line in artists[1:]], [line.split(",")[0] for line in artists[1:]]

        for i, j in zip(artistsName, artistIndex):
            print("Now retrieving " + i)
            #if findAlbum(i) == False:
            #    return
            response = get_several_album(i)
            if response == 429:
                print(f"error {response} on rate limit")
                return
            elif response != None:
                print(f"error {response} on several album")
                continue
                            
                
            print(f"Finished retrieving {i}")
            count += 1

            with open("spotipy/lastArtistRetrieved.txt", "a") as f:
                f.write(f"{j},{i}\n")
            #wait 5 seconds
            time.sleep(1.5)


def get_several_album(artistName):

    os.makedirs(f"albums/{artistName}", exist_ok=True)
    
    query_params = {
        'q': f'artist:{artistName}',
        'type': 'album',
        'album_type': 'album'  # Exclude singles and compilations
    }

    # Spotify API endpoint for searching albums
    url = "https://api.spotify.com/v1/search"

    # Include the query parameters in the GET request
    headers = {
        "Authorization": f"Bearer {access_token}"
    }

    
    response = requests.get(url, headers=headers, params=query_params)

    if response.status_code != 200:
        print(f"Error: {response.status_code} in retrieving artist")
        if response.status_code == 429:
            print("headers", response.headers.get("retry-after"))
        print(response.text)
        #print the body
        print(response.json())
        return response.status_code
    
    time.sleep(1.5)

    #check if the response is ok
    if response.status_code != 200:
        print(f"Error: {response.status_code} in several album")
        
        return False

    #save the json
    with open("albums.json", "w") as f:
        json.dump(response.json(), f, indent=4)

    #get the list of albums
    albums = response.json().get("albums", {}).get("items", [])
    
    #filter only for albums, excluding singles and compilations
    albums = [album for album in albums if album.get("album_type") == "album"]
    #print(f"Found {len(albums)} albums for {artistName}")
    count = 0
    IDs = ''
    ok = True
    for album in albums:
        if count < 20:
            IDs += album['id'] + ','
            count += 1

        else:
            #retrieve the tracklist for the 20 first albums
            #print(f"Retrieving tracklist for {IDs}")

            IDs = IDs[:-1]
             
            if saveAlbums(IDs, artistName) == 429:
                return 429

            IDs = ''
            count = 0
    else:
            IDs = IDs[:-1]
        #print(f"Retrieving tracklist for {IDs}")
            if saveAlbums(IDs, artistName) == 429:
                return 429

def saveAlbums(albumIDS, artistName):
    os.makedirs(f"../album/{artistName}", exist_ok=True)

    url = 'https://api.spotify.com/v1/albums'


    headers = {
        "Authorization": f"Bearer {access_token}"
    }

    params = {'ids': albumIDS}

    response = requests.get(url, headers=headers, params=params)

    if response.status_code != 200:
        print(f"Error: {response.status_code} in saveAlbum")
        if response.status_code == 429:
            print("headers", response.headers.get("retry-after"))
        else:
            print(response.text)
        return response.status_code
  
    #take the body of the response
    body = response.json()

    #save the json in a file
    with open("album.json", "w") as f:
        json.dump(body, f, indent=4)

    artistjson = {"artists": []}

    for album in body['albums']:

        try:
            albumName = album['name']
            if '/' in albumName:
                albumName = albumName.replace('/', '-')

        except:
            #print("EOF, returning")
            continue

        if albumName is None:
            return

        tracklist = []
        
        #if there are more than one artist, add them to the artistjson
        if len(album['artists']) > 1:
            for artist in album['artists']:
                artistjson["artists"].append(artist['name'])

        else:
            artistjson["artists"].append(album['artists'][0]['name'])

        coverURL = album['images'][0]['url'] if 'images' in album and album['images'] else None
            
        #get the tracklist for the album
        for track in getTracklist(album['id']):
            #print(f"{track['name']} - {track['duration_sec']}")
            tracklist.append({"name": track["name"], "duration_ms" : track['duration_sec']})

            
        albumjson = {"title": album['name'], "artists": artistjson, "year": album['release_date'][:4], 'coverURL': coverURL, "tracklist": tracklist}
        #print("name", albumjson, end='\n\n')

        with open(f"../album/{artistName}/{albumName}.json", "a") as f:
                json.dump(albumjson, f, indent=4)

        time.sleep(0.5)


def getTracklist(album_id):

    # Function to retrieve the tracklist for a given album ID
    url = f"https://api.spotify.com/v1/albums/{album_id}/tracks"
    
    # Specify the headers with the Authorization token
    headers = {
        "Authorization": f"Bearer {access_token}"
    }

    # Make the GET request

    response = requests.get(url, headers=headers)

    if response.status_code != 200:
        print(f"Error: {response.status_code} in tracklist")
        print("headers", response.headers.get("retry-after"))
        return False


    # Check if the request was successful (status code 200)
    if response.status_code == 200:
        # Parse the JSON response
        tracklist = response.json()

        # Extract track information
        tracklist_items = tracklist.get("items", [])
        tracks_info = []

        for track in tracklist_items:
            track_name = track.get("name")
            duration_ms = track.get("duration_ms")

            # Convert duration from milliseconds to seconds
            duration_sec = duration_ms if duration_ms is not None else None

            # Append track information to the array
            tracks_info.append({"name": track_name, "duration_sec": duration_sec})

        return tracks_info

    else:
        # Print an error message if the request was not successful
        print(f"Error: {response.status_code} in tracklist")
        print("headers",response.headers["retry-after"])
        return False

# Your existing loop...


if __name__ == "__main__":

    init()
    find10kArtists()
