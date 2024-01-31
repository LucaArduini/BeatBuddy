from pymongo import MongoClient
import json
import os

# Connessione a MongoDB
client = MongoClient("mongodb://10.1.1.18:27017,10.1.1.17:27017,10.1.1.19:27017/?replicaSet=BB&w=1&readPreference=nearest&retryWrites=true")
db = client["BeatBuddy"]  # Sostituisci con il nome del tuo database

#test della connessione al db
if db:
    print("Connection Successful")
else:
    print("Connection Failed")
    exit()

# Funzione per caricare i dati degli album
def carica_dati_album():
    percorso_album = "albums/"
    for artist_name in os.listdir(percorso_album):
        artist_path = os.path.join(percorso_album, artist_name)
        if os.path.isdir(artist_path):
            for album_file in os.listdir(artist_path):
                with open(os.path.join(artist_path, album_file), 'r') as file:
                    album_data = json.load(file)
                    db.albums.insert_one(album_data)


# Funzione per creare un mapping tra il nome dell'album e l'ObjectId
def estrai_id_album():
    mapping_id_album = {}
    for album in db.albums.find({}, {"_id": 1, "title": 1, "artists": 1}):
        key = f"{album['title'].lower()}"
        mapping_id_album[key] = album["_id"]
    return mapping_id_album

# Funzione per caricare e aggiornare i dati delle recensioni
def carica_dati_recensioni(mapping_id_album, percorso_recensioni):
    count = 0
    for review_file in os.listdir(percorso_recensioni):
        with open(os.path.join(percorso_recensioni, review_file), 'r') as f:
            data = json.load(f)
            albumName = data["album"].lower()
            f.close()

        key = f"{albumName}"

        if key in mapping_id_album:
            data["albumID"] = mapping_id_album[key]
            data.pop('album', None)
            data.pop('artist', None)
            data["rating"] = int(data["rating"])
            data["username"] = data["user"]
            data.pop('user', None)
            data["date"] = data["timestamp"]
            data.pop('timestamp', None)
            #carica il file json nella collection reviews
            db.reviews.insert_one(data)

        else:
            count += 1

    print(f"Album non trovati: {count}")           

def inserisci_utenti():
    user = os.path.join(os.getcwd(), "users", "json")

    user_collection = db["users"]

    for file in os.listdir(user):
        #carica il file json su mongo

        with open(os.path.join(user, file), "r") as f:
            data = json.load(f)

            #chiudi il file
            f.close()

        #inserisci il file json su mongo
        user_collection.insert_one(data)

    print("Inseriti tutti gli utenti")


def inserisci_artisti():
    artist = os.path.join(os.getcwd(), "artists")

    artist_collection = db["artists"]

    for file in os.listdir(artist):
        #carica il file json su mongo
        try:
            artist_collection.insert_one(json.load(open(os.path.join(artist, file), "r")))
        except:
            pass
    print("Inseriti tutti gli artisti")

def aggiorna_album_review():
    collectionReviews = db.reviews
    collectionAlbums = db.albums

    count = 0

    #recupera tutti gli album e analizzali uno alla volta
    for album in collectionAlbums.find():
        #controlla lunghezza dell'array last_reviews

        #recupera le 5 recensioni più recenti
        reviews = collectionReviews.find({"albumID": album["_id"]}).sort("timestamp", -1).limit(5) 

        #trasforma l'oggetto in una lista
        reviews = list(reviews)


        if len(reviews) == 0:
            #check if the field reviews exists
            if "reviews" in album:
                #delete the field reviews
                collectionAlbums.update_one({"_id": album["_id"]}, {"$unset": {"reviews": ""}})
            continue

        rev = []
        for review in reviews:
            #take only the username, rating and text
            rev.append({"username": review["username"], "rating": review["rating"], "text": review["text"]})
            print("add", review["username"], review["rating"], review["text"])



        #aggiorna l'album inserendo le 5 recensioni più recenti, ed eliminando il campo reviews
        collectionAlbums.update_one({"_id": album["_id"]}, {"$set": {"last_reviews": list(rev)}, "$unset": {"reviews": ""}})

        print("ID album", album["_id"])

        if count % 100 == 0:
            print(f"Aggiornati {count} album")
    
    print("Aggiornati tutti gli album")


# Funzione principale
def main():

    inserisci_utenti()

    carica_dati_album()
    print("Album caricati")

    inserisci_artisti()

    mapping_id_album = estrai_id_album()
    print("Mapping creato")

    carica_dati_recensioni(mapping_id_album, percorso_recensioni="reviews/Amazon/")
    print("Recensioni caricate Amazon")

    carica_dati_recensioni(mapping_id_album, percorso_recensioni="reviews/json/")
    print("Recensioni caricate")
    
    aggiorna_album_review()


if __name__ == "__main__":
    main()


