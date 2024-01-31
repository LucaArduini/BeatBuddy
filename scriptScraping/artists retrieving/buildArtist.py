import musicbrainzngs
import time
import requests
import random
import os
import sys
import json

global artists_path
artists_path = ''

def init():
    global artists_path
    musicbrainzngs.set_useragent("MyApp", "0.1", "example@example.it")
    musicbrainzngs.set_rate_limit(1, 1)

    # get the current path
    current_path = os.getcwd()

    # if doesn't exist, create directory "artist"
    artists_path = os.path.join(current_path, 'artists')
    print(artists_path)
    if not os.path.exists(artists_path):
        os.mkdir(artists_path)

def read_retrieved_artists():
    # Read the file once and store the data in a set
    with open('artists retrieving/retrieved.txt', 'r') as f:
        return set(f.read().splitlines())

def get_artist_id(artist_name):
    # get the artist id
    response = musicbrainzngs.search_artists(artist=artist_name, limit=1)

    try:
        artist_id = response['artist-list'][0]['id']
        return artist_id
    except IndexError:
        print(f"Artista '{artist_name}' non trovato.")
        return None

def get_artist_image(artist_id):
    key = 'our_magic_key'
    url = f'http://webservice.fanart.tv/v3/music/{artist_id}?api_key={key}'
    headers = {'Content-Type': 'application/json'}
    response = requests.get(url, headers=headers)

    json_data = response.json()

    if 'artistthumb' in json_data:
        url = random.choice(json_data['artistthumb'])['url']
        return url
    else:
        urls = ['None']
        for line in response.text.splitlines():
            if 'url' in line:
                urls.append(line.split(': "')[1].replace('",', ''))

        return urls[0] if len(urls) == 1 else random.choice(urls[1:])

def main():
    albums_path = os.path.join(os.getcwd(), 'albums')
    print(albums_path)

    retrieved_artists = read_retrieved_artists()

    for filename in os.listdir(albums_path):
        artist_name = filename

        if artist_name in retrieved_artists:
            continue

        # get the artist id
        artist_id = get_artist_id(artist_name)


        # get the artist url image
        artist_image = get_artist_image(artist_id) if artist_id != 'None' else 'None'

        document = {'name': artist_name, 'image': artist_image if artist_image != 'None' else 'https://i.pinimg.com/564x/1d/04/a8/1d04a87b8e6cf2c3829c7af2eccf6813.jpg'}

        albums = []

        artist_album_path = os.path.join(albums_path, artist_name)

        # if it has no files, skip and delete the directory
        if not os.listdir(artist_album_path):
            try:
                os.rmdir(artist_album_path)
            except OSError as e:
                print(f"Error: {e}")
            continue

        for album in os.listdir(artist_album_path):
            if album.endswith('.json'):
                with open(os.path.join(artist_album_path, album), 'r') as f:
                    data = json.load(f)
                    coverUrl = data.get('coverURL', '')
                    albums.append({'title': data['title'], 'coverURL': coverUrl})

        document['albums'] = albums

        # once finished with every album for every artist, save the file named as the artist
        with open(os.path.join(artists_path, artist_name + '.json'), 'w') as f:
            json.dump(document, f, indent=4)

        time.sleep(1)

        # dump in "retrieved.txt" the name of the last artist retrieved
        with open('artists retrieving/retrieved.txt', 'a') as f:
            f.write(artist_name + '\n')
        retrieved_artists.add(artist_name)
        print("Saved", artist_name)

if __name__ == '__main__':
    init()
    main()
