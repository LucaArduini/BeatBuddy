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
            success = session.execute_write(self._create_and_return_song, node_data)
            return success

    @staticmethod
    def _create_and_return_song(tx, song_data):
        try:
            # Assicurati che la query sia formattata correttamente con i campi specifici per le canzoni
            query = (
                "CREATE (n:Album {"
                "albumName: $albumName, "
                "artistName: $artistName, "
                "coverURL: $coverURL"
                "}) RETURN n"
            )

            result = tx.run(query, albumName=song_data["albumName"], 
                            artistName=song_data["artistName"], coverURL=song_data["coverURL"])

            # Verifica che il risultato non sia vuoto, indicando che il nodo è stato creato
            return result.single() is not None
        except Exception as e:
            print(f"Errore durante l'inserimento della canzone: {e}")
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
db = BeatBuddyDB("neo4j://10.1.1.17:7687", "neo4j", "rootroot", "BeatBuddy")

#check the connection
if db.driver:
    print("Connection Successful")
else:
    print("Connection Failed")

#inserimento degli user
    
album_path = os.path.join(os.getcwd(), "albums")

count = 0

for artist in os.listdir(album_path):
    #open that directory
    for album in os.listdir(os.path.join(album_path, artist)):
        with open(os.path.join(album_path, artist, album)) as json_file:
            data = json.load(json_file)

            #crea una stringa con tutti gli artisti, separati da una virgola e da uno spazio
            artists = ""
            for name in data["artists"]:
                artists += name + ", "


            toBeInserted = {
                    "albumName": data["title"],
                    "artistName": artists[:-2],
                    "coverURL" : data["coverURL"],
                    }
            
            _create_and_return_song = db.insert_node("Album", toBeInserted)

            if not _create_and_return_song:
                print(f"Errore durante l'inserimento dell'album: {data['albumName']}")

            count += 1

            if count % 1000 == 0:
                print(f"Album inseriti: {count}")

            

