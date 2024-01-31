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
            success = session.execute_write(self._create_and_return_node, node_type, node_data)
            return success

    @staticmethod
    def _create_and_return_node(tx, node_type, node_data):
        try:
            query = f"CREATE (n:{node_type} {{username: $data.username}}) RETURN n"
            result = tx.run(query, data=node_data)
            return result.single() is not None
        except Exception as e:
            print(f"Errore durante l'inserimento: {e}")
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
    
user_path = os.path.join(os.getcwd(), "users", "json")

count = 0

for file in os.listdir(user_path):
    with open(os.path.join(user_path, file)) as json_file:
        data = json.load(json_file)

        if "isAdmin" in data:
            continue

        #di tutti i dati, devo inserire solo username
        username = {
            "username": data["username"]
        }
        type = "User"

        _create_and_return_node = db.insert_node(type, username)

        if not _create_and_return_node:
            print(f"Errore durante l'inserimento dell'utente {username}")
            continue

        count += 1
        if count %1000 == 0:
            print(f"{count} users inseriti")

