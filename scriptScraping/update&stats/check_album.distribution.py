from collections import defaultdict
import os
import json
import matplotlib.pyplot as plt

def album_analysis():
    album_path = os.path.join(os.getcwd(), "albums")

    dates = {}
    for artist in os.listdir(album_path):
        for album in os.listdir(os.path.join(album_path, artist)):
            with open(os.path.join(album_path, artist, album), "r") as f:
                albumjson = json.load(f)
                date = albumjson["year"]

                if date in dates:
                    dates[date] += 1
                else:
                    dates[date] = 1


    #study the distribution of the dates

    #print histogram
    #plt.bar(dates.keys(), dates.values(), color='g')
    #plt.xlabel("Year")
    #plt.ylabel("Number of Albums")
    #plt.title("Albums per Year")
    #plt.show()

    # Sort the dictionary based on year to ensure we divide the ranges logically
    sorted_album_data = dict(sorted(dates.items()))

    # We will attempt to distribute the albums as evenly as possible across three servers.
    # We will do this by summing the total number of albums and aiming to assign roughly one-third to each server.
    # We need to be mindful of the distribution to avoid placing too many albums on one server.

    total_albums = sum(sorted_album_data.values())
    target_albums_per_server = total_albums / 3  # target number of albums per server

    # Now we will go through the sorted years and assign them to servers such that
    # each server gets as close to the target number as possible without going over too much.

    servers = {
        'server1': {'years': [], 'album_count': 0},
        'server2': {'years': [], 'album_count': 0},
        'server3': {'years': [], 'album_count': 0}
    }

    current_server = 'server1'

    for year, count in sorted_album_data.items():
        # Check if adding this year to the current server would exceed the target
        # If so, move to the next server
        if servers[current_server]['album_count'] + count > target_albums_per_server and servers[current_server]['album_count'] > 0:
            if current_server == 'server1':
                current_server = 'server2'
            elif current_server == 'server2':
                current_server = 'server3'

        servers[current_server]['years'].append(year)
        servers[current_server]['album_count'] += count


    #print(servers)

    years_sorted = sorted(dates.keys())    
    # Assigning a non-linear weight to album counts that significantly favors newer albums.
    # We'll use an exponential growth function to weight the album counts, reflecting much higher expected traffic for newer albums.

    # Define the base for exponential growth
    base = 1.1

    # Apply the exponential weights to the album counts
    exponential_weights = {year: base**(int(year) - min(map(int, years_sorted))) for year in years_sorted}
    exponential_weighted_album_counts = {
        year: dates[year] * weight for year, weight in exponential_weights.items()
    }

    # Now calculate the total weighted album count and the target weighted album count per server with the exponential weights
    total_exponential_weighted_albums = sum(exponential_weighted_album_counts.values())
    target_exponential_weighted_albums_per_server = total_exponential_weighted_albums / 3

    # We will redistribute the albums with the new exponential weights taken into account.
    servers_exponential_weighted = {
        'server1': {'years': [], 'album_count': 0, 'weighted_album_count': 0},
        'server2': {'years': [], 'album_count': 0, 'weighted_album_count': 0},
        'server3': {'years': [], 'album_count': 0, 'weighted_album_count': 0}
    }

    current_server = 'server1'

    # Go through the sorted years and assign them to servers based on the exponential weighted count
    for year in years_sorted:
        count = dates[year]
        weighted_count = exponential_weighted_album_counts[year]

        # Check if adding this year to the current server would exceed the target.
        # If so, move to the next server.
        if servers_exponential_weighted[current_server]['weighted_album_count'] + weighted_count > target_exponential_weighted_albums_per_server and servers_exponential_weighted[current_server]['album_count'] > 0:
            if current_server == 'server1':
                current_server = 'server2'
            elif current_server == 'server2':
                current_server = 'server3'

        servers_exponential_weighted[current_server]['years'].append(year)
        servers_exponential_weighted[current_server]['album_count'] += count
        servers_exponential_weighted[current_server]['weighted_album_count'] += weighted_count

    # Output the results, focusing on the distribution of the number of albums
        
    {
        server: {
            'years_range': f"{data['years'][0]}-{data['years'][-1]}",
            'number_of_albums': data['album_count'],
            'weighted_album_count': int(data['weighted_album_count'])
        } for server, data in servers_exponential_weighted.items()
    }

    #print the results
    for server, data in servers_exponential_weighted.items():
        print(f"Server {server}: {data['years'][0]}-{data['years'][-1]} with {data['album_count']} albums, Weighted Album Count: {int(data['weighted_album_count'])}")

def users_analysis():

    users = []
    users_path = os.path.join(os.getcwd(), "users", "json")

    # We will attempt to distribute the users as evenly as possible across three servers trough username
    
    for user in os.listdir(users_path):
        with open(os.path.join(users_path, user), "r") as f:
            userjson = json.load(f)
            if userjson["username"] == "" or userjson["username"] == " ":
                continue
            users.append(userjson["username"])

    
    users.sort()

    #divide the users in 3 groups

    users_per_server = len(users) / 3
    server1 = users[:int(users_per_server)]
    server2 = users[int(users_per_server):int(users_per_server*2)]
    server3 = users[int(users_per_server*2):]

    #found the "lowest" and "highest" username in each server
    server1_lowest = server1[0]
    server1_highest = server1[-1]
    server2_lowest = server2[0]
    server2_highest = server2[-1]
    server3_lowest = server3[0]
    server3_highest = server3[-1]

    #print the results
    print("Server 1: " + server1_lowest + " - " + server1_highest, "with " + str(len(server1)) + " users")
    print("Server 2: " + server2_lowest + " - " + server2_highest, "with " + str(len(server2)) + " users")
    print("Server 3: " + server3_lowest + " - " + server3_highest, "with " + str(len(server3)) + " users")


def album_title_analysis():
    album_path = os.path.join(os.getcwd(), "albums")

    titles = defaultdict(int)
    for artist in os.listdir(album_path):
        for album in os.listdir(os.path.join(album_path, artist)):
            with open(os.path.join(album_path, artist, album), "r") as f:
                albumjson = json.load(f)
                title = albumjson["title"][0].upper()  # Taking the first letter of the title

                titles[title] += 1

    # Sort the dictionary based on the first letter of the titles
    sorted_album_data = dict(sorted(titles.items()))

    total_albums = sum(sorted_album_data.values())
    target_albums_per_server = total_albums / 3

    servers = {
        'server1': {'titles': [], 'album_count': 0},
        'server2': {'titles': [], 'album_count': 0},
        'server3': {'titles': [], 'album_count': 0}
    }

    current_server = 'server1'

    for title, count in sorted_album_data.items():
        if servers[current_server]['album_count'] + count > target_albums_per_server and servers[current_server]['album_count'] > 0:
            if current_server == 'server1':
                current_server = 'server2'
            elif current_server == 'server2':
                current_server = 'server3'

        servers[current_server]['titles'].append(title)
        servers[current_server]['album_count'] += count

    # Output the results
    server_ranges = {
        server: f"{data['titles'][0]}-{data['titles'][-1]}" for server, data in servers.items()
    }

    #stampa il numero di album per ogni server
    for server, data in servers.items():
        print(f"Server {server}: {data['titles'][0]}-{data['titles'][-1]} with {data['album_count']} albums")
    return server_ranges

if __name__ == "__main__":
    print(album_title_analysis())
    album_analysis()
    users_analysis()


