import os
import requests
import sys
import time
import json
from pymongo import MongoClient
from neo4j import GraphDatabase



global access_token, client_id, client_secret


#connect to mongodb
client = MongoClient("mongodb://10.1.1.18:27017,10.1.1.17:27017,10.1.1.19:27017/?replicaSet=BB")
db = client["BeatBuddy"]

access_token = ''
client_id = ''
client_secret = ''
duplicated = []

uri = "neo4j://10.1.1.17:7687"  # Modifica con l'URI del tuo database Neo4j
user = "neo4j"
password = "rootroot"  # Modifica con la password del tuo database Neo4j

driver = GraphDatabase.driver(uri, auth=(user, password))
nome_del_database = "BeatBuddy"  


def remove_node(node_type, node_data):
    with driver.session(database=nome_del_database) as session:

        if node_type == "Song":
            success = session.write_transaction(_delete_song, node_data)
        else:
            success = session.execute_write(_delete_album, node_data)
        return success
    

def _delete_song(tx, song_data):
    try:
        # Assicurati che la query sia formattata correttamente con i campi specifici per le canzoni
        query = (
            "MATCH (s:Song {"
            "songName: $songName, "
            "albumName: $albumName, "
            "artistName: $artistName, "
            "coverUrl: $coverUrl"
            "}) DELETE s"
        )

        result = tx.run(query, songName=song_data["songName"], albumName=song_data["albumName"], 
                        artistName=song_data["artistName"], coverUrl=song_data["coverUrl"])

        # Verifica che il risultato non sia vuoto, indicando che il nodo è stato creato
        return result.single() is not None
    except Exception as e:
        print(f"Errore durante la rimozione della canzone: {e}")
        return False
    

def _delete_album(tx, song_data):
    try:
        # Assicurati che la query sia formattata correttamente con i campi specifici per le canzoni
        query = (
            "MATCH (s:Album {"
            "albumName: $albumName, "
            "artistName: $artistName, "
            "coverURL: $coverUrl"
            "}) DELETE s"
        )
        print(query)
        result = tx.run(query, albumName=song_data["albumName"], 
                        artistName=song_data["artistName"], coverUrl=song_data["coverUrl"])
        print(result)
        # Verifica che il risultato non sia vuoto, indicando che il nodo è stato creato
        return result.single() is not None
    except Exception as e:
        print(f"Errore durante la rimozione dell'album: {e}")
        #stampa l'errore
        print(e)


        return False


def insert_node(node_type, node_data):
    with driver.session(database=nome_del_database) as session:

        if node_type == "Song":
            success = session.write_transaction(_create_and_return_song, node_data)
        else:
            success = session.execute_write(_create_and_return_album, node_data)
        return success
    

def _create_and_return_song(tx, song_data):
    try:
        # Assicurati che la query sia formattata correttamente con i campi specifici per le canzoni
        query = (
            "CREATE (s:Song {"
            "songName: $songName, "
            "albumName: $albumName, "
            "artistName: $artistName, "
            "coverUrl: $coverUrl"
            "}) RETURN s"
        )

        result = tx.run(query, songName=song_data["songName"], albumName=song_data["albumName"], 
                        artistName=song_data["artistName"], coverUrl=song_data["coverUrl"])

        # Verifica che il risultato non sia vuoto, indicando che il nodo è stato creato
        return result.single() is not None
    except Exception as e:
        print(f"Errore durante l'inserimento della canzone: {e}")
        return False
    

def _create_and_return_album(tx, song_data):
    try:
        # Assicurati che la query sia formattata correttamente con i campi specifici per le canzoni
        query = (
            "CREATE (s:Album {"
            "albumName: $albumName, "
            "artistName: $artistName, "
            "coverURL: $coverUrl"
            "}) RETURN s"
        )
        print(query)
        result = tx.run(query, albumName=song_data["albumName"], 
                        artistName=song_data["artistName"], coverUrl=song_data["coverUrl"])
        print(result)
        # Verifica che il risultato non sia vuoto, indicando che il nodo è stato creato
        return result.single() is not None
    except Exception as e:
        print(f"Errore durante l'inserimento dell'album: {e}")
        #stampa l'errore
        print(e)


        return False
    

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

    #get the list of albums
    albums = response.json().get("albums", {}).get("items", [])
    
    
    #filter only for albums, excluding singles and compilations
    albums = [album for album in albums if album.get("album_type") == "album"]
    #print(f"Found {len(albums)} albums for {artistName}")

    collezione_artists =  db.artists
    documento_artista = collezione_artists.find_one({"name": artistName})

    if documento_artista:
        # Verifica se l'array "albums" esiste nel documento e ottieni la sua lunghezza
        if "albums" in documento_artista:
            lunghezza_albums = len(documento_artista["albums"])

        else:
            return False

    else:
        print(f"Artista '{artistName}' non trovato nella collezione 'artists'.")
        return False


    if lunghezza_albums != len(albums):
        
        albumsMongo = list(documento_artista['albums'])

        #ordina per title
        albumsMongo.sort(key=lambda x: x['title'])
        albumsMongo = [album['title'] for album in albumsMongo]


        albumSpotify = [album['name'] for album in albums]

        for album in albumSpotify:
            if album not in albumsMongo:

                
                #print all the albumID from albums
                
                ID = albums[albumSpotify.index(album)]['id']
                

                #query to SPotify to get details about the album
                url = f"https://api.spotify.com/v1/albums/{ID}"

                response = requests.get(url, headers=headers)

                if response.status_code != 200:
                    print(f"Error: {response.status_code} in retrieving artist")
                    if response.status_code == 429:
                        print("headers", response.headers.get("retry-after"))
                    print(response.text)
                    #print the body
                    print(response.json())
                    exit()
                else:

                    artists = str()
                    for artist in response.json()['artists']:
                        artists += artist['name'] + ", "
                    artists = artists[:-2]

                    toBeInserted = {
                    "albumName": response.json()['name'],
                    "artistName": artists,
                    "coverUrl" : response.json()['images'][0]['url']}
                    
                
                    if insert_node("Album", toBeInserted) == False:
                        print(f"Errore durante l'inserimento dell'album {toBeInserted['albumName']}")
                        continue

                    #print the track list of the album
                    

                    toBeInserted = {
                        "title": response.json()['name'],
                        "artists":[artist['name'] for artist in response.json()['artists']],
                        "coverURL": response.json()['images'][0]['url'],
                        "year": response.json()['release_date'][:4],
                        "songs": [{"name": track['name'], "duration_ms": track['duration_ms'], "likes":0} for track in response.json()['tracks']['items']],
                        "likes": 0,
                        "averageRating": 0,
                        "last_reviews": []
                    }       


                    #upload the album to mongo
                    collezione_albums = db.albums
                    print(toBeInserted)
                    result = collezione_albums.insert_one(toBeInserted)

                    if not result.acknowledged:
                        print(f"Errore durante l'inserimento dell'album {toBeInserted['title']}")
                        #fai rollback dell'album su neo4j
                        remove_node("Album", toBeInserted)
                        continue

                    #aggiungi l'album all'artista, mettendo nell'array albums title e coverURL
                    collezione_artists.update_one({"name": artistName}, {"$push": {"albums": {"title": response.json()['name'], "coverURL": response.json()['images'][0]['url']}}})

                    if not result.acknowledged:
                        print(f"Errore durante l'inserimento dell'album {toBeInserted['title']}")
                        #fai rollback dell'album su neo4j
                        remove_node("Album", toBeInserted)
                        continue
                    
                    inserted = []
                    #add the songs on neo4j
                    for song in response.json()['tracks']['items']:
                        toBeInserted = {
                            "albumName": response.json()['name'],
                            "artistName": artistName,
                            "coverUrl" : response.json()['images'][0]['url'],
                            "songName": song["name"]}
                        
                        if insert_node("Song", toBeInserted) == False:
                            print(f"Errore durante l'inserimento della canzone {toBeInserted['songName']}")
                            
                            continue
                        else:
                            #rollback for all the inserted songs
                            for song in inserted:
                                remove_node("Song", song)
                            #rollback for the album from neo4j
                            remove_node("Album", toBeInserted)

                            #rollback for the album from mongo
                            collezione_albums.delete_one({"title": response.json()['name']})
                            #rollback for the album from the artist
                            collezione_artists.update_one({"name": artistName}, {"$pull": {"albums": {"title": response.json()['name']}}})
                            continue
                        
                


    #add the album to the artist
    

                

init()
#find10kArtists()