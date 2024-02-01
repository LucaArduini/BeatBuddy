from neo4j import GraphDatabase
import json
import numpy as np
import os

class BeatBuddyDB:
    def __init__(self, uri, user, password, database):
        self.driver = GraphDatabase.driver(uri, auth=(user, password))
        self.database = database

    def close(self):
        self.driver.close()

    def insert_node(self, node_type, node_data):
        with self.driver.session(database=self.database) as session:
            if node_type == "Song":
                success = session.write_transaction(self._create_and_return_song, node_data)
            else:

                success = session.execute_write(self._create_and_return_album, node_data)
            return success

    @staticmethod
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
        
    @staticmethod
    def _create_and_return_album(tx, song_data):
        try:
            # Assicurati che la query sia formattata correttamente con i campi specifici per le canzoni
            query = (
                "CREATE (s:Album {"
                "name: $name, "
                "artists: $artistName, "
                "coverUrl: $coverUrl"
                "}) RETURN s"
            )

            result = tx.run(query, name=song_data["name"], 
                            artistName=song_data["artistName"], coverUrl=song_data["coverUrl"])

            # Verifica che il risultato non sia vuoto, indicando che il nodo è stato creato
            return result.single() is not None
        except Exception as e:
            print(f"Errore durante l'inserimento dell'album: {e}")
            return False




    @staticmethod
    def _create_relationships(tx, node_type, relationship_type, lambda_param, max_relationships):
        query = (
            f"MATCH (a:{node_type}), (b:{node_type}) "
            "WHERE ID(a) < ID(b) AND rand() < {probability} "
            f"CREATE (a)-[:{relationship_type}]->(b)"
        )
        probability = 1 - np.exp(-lambda_param)
        tx.run(query, probability=probability, max_rel=max_relationships)

# Connessione al database
db = BeatBuddyDB("bolt://localhost:7687", "neo4j", "BeatBuddy", "beatbuddy")

#check the connection
if db.driver:
    print("Connection Successful")
else:
    print("Connection Failed")

#inserimento degli user
    
album_path = os.path.join(os.getcwd(), "albums")

count = 0
countAlbum = 0

#query for how many documents are in the collection
type = "Song"

query = f"MATCH (n:{type}) RETURN count(n)"

with db.driver.session(database=db.database) as session:
    result = session.run(query)
    count = result.single()[0]
    print(f"Canzoni già presenti: {count}")



for artist in os.listdir(album_path):
    #open that directory
    for album in os.listdir(os.path.join(album_path, artist)):
        with open(os.path.join(album_path, artist, album)) as json_file:
            data = json.load(json_file)

            #check if data["songs"] is a bool
            if isinstance(data["songs"], bool):
                continue
            
            for song in data["songs"]:
                toBeInserted = {
                    "albumName": data["title"],
                    "artistName": data["artists"][0],
                    "coverUrl" : data["coverURL"],
                    "songName": song["name"]}

                #print(tmp)

                _create_and_return_song = db.insert_node("Song", toBeInserted)

                if not _create_and_return_song:
                    print(f"Errore durante l'inserimento della canzone {toBeInserted['songName']}")
                    
                    continue
                
                count += 1

                if count % 10000 == 0:
                    print(f"Canzoni inserite: {count}")

            #inserimento dell'album 
            doc = {
                "name": data["title"],
                "artistName": data["artists"][0],
                "coverUrl" : data["coverURL"]}
            
            _create_and_return_album = db.insert_node("Album", doc)

            if not _create_and_return_album:
                print(f"Errore durante l'inserimento dell'album {doc['name']}")
                
                continue

            countAlbum += 1

            if countAlbum % 1000 == 0:
                print(f"Album inseriti: {countAlbum}")
            

            