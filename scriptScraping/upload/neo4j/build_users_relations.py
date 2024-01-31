from neo4j import GraphDatabase
import random
from neo4j import GraphDatabase


# Funzione per generare un numero casuale tra min_num e max_num
def generate_random_number(min_num, max_num):
    return random.randint(min_num, max_num)

# Funzione per creare relazioni "LIKES_A" in un'unica transazione
def create_batch_likes_a_relationships(tx, user_id, album_ids):
    query = "UNWIND $pairs AS pair " \
            "MATCH (u:User) WHERE ID(u) = pair.user_id " \
            "MATCH (a:Album) WHERE ID(a) = pair.album_id " \
            "MERGE (u)-[:LIKES_A {timestamp: datetime()}]->(a)"  # Aggiungi la proprietà "timestamp"
    pairs = [{'user_id': user_id, 'album_id': album_id} for album_id in album_ids]
    tx.run(query, pairs=pairs)

# Funzione per creare relazioni "LIKES_S" tra User e Song in un'unica transazione
def create_batch_likes_s_relationships(tx, user_id, song_ids):
    query = "UNWIND $pairs AS pair " \
            "MATCH (u:User) WHERE ID(u) = pair.user_id " \
            "MATCH (s:Song) WHERE ID(s) = pair.song_id " \
            "MERGE (u)-[:LIKES_S {timestamp: datetime()}]->(s)"  # Aggiungi la proprietà "timestamp"
    pairs = [{'user_id': user_id, 'song_id': song_id} for song_id in song_ids]
    tx.run(query, pairs=pairs)


# Funzione per creare relazioni "FOLLOW" in un'unica transazione
def create_batch_follow_relationships(tx, user_id, followees_ids):
    query = "UNWIND $pairs AS pair " \
            "MATCH (u1:User) WHERE ID(u1) = pair.user_id " \
            "MATCH (u2:User) WHERE ID(u2) = pair.followee_id " \
            "MERGE (u1)-[:FOLLOW]->(u2)"
    pairs = [{'user_id': user_id, 'followee_id': followee_id} for followee_id in followees_ids]
    tx.run(query, pairs=pairs)


# Configurazione della connessione a Neo4j
uri = "neo4j://10.1.1.17:7687"  # Modifica con l'URI del tuo database Neo4j
user = "neo4j"
password = "rootroot"  # Modifica con la password del tuo database Neo4j

def main():
    driver = GraphDatabase.driver(uri, auth=(user, password))
    nome_del_database = "BeatBuddy"  # Sostituisci con il nome del tuo database

    with driver.session(database=nome_del_database) as session:
        all_users = session.run("MATCH (user:User) RETURN ID(user) AS user_id").value()
        #all_albums = session.run("MATCH (album:Album) RETURN ID(album) AS album_id").value()
        #all_songs = session.run("MATCH (song:Song) RETURN ID(song) AS song_id").value()


        count = 0
        for user_id in all_users:
            # Crea relazioni "FOLLOW" in batch
            followees = random.sample([uid for uid in all_users if uid != user_id], generate_random_number(1, 30))
            session.write_transaction(create_batch_follow_relationships, user_id, followees)

            ## Crea relazioni "LIKES_A" in batch
            #liked_albums = random.sample(all_albums, generate_random_number(1, 10))
            #session.write_transaction(create_batch_likes_a_relationships, user_id, liked_albums)
#
            ## Crea relazioni "LIKES_S" in batch
            #liked_songs = random.sample(all_songs, generate_random_number(1, 30))
            #session.write_transaction(create_batch_likes_s_relationships, user_id, liked_songs)
            
            count += 1
            if count % 1000 == 0:
                print(f"Aggiornati {count} utenti")

    driver.close()

if __name__ == "__main__":
    main()
