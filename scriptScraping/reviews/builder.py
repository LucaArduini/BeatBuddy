import os
import numpy as np
import random
import matplotlib.pyplot as plt
import json
from datetime import datetime, timedelta
import faker
import string
import secrets
import hashlib

fak = faker.Faker(["it_IT", "en_US"])

user_path = os.path.join(os.getcwd(), 'users', "json")

def generate_password(length=8):
    characters = string.ascii_letters + string.digits + string.punctuation
    password = ''.join(secrets.choice(characters) for _ in range(length))
    return password

def read_distribution_params_from_log(log_file_path):
    with open(log_file_path, 'r') as log_file:
        lines = log_file.readlines()
        mean_reviews = float(lines[0].split(":")[1].strip())
        std_dev_reviews = float(lines[1].split(":")[1].strip())
        q25 = float(lines[2].split(":")[1].strip())
        q75 = float(lines[3].split(":")[1].strip())
    return mean_reviews, std_dev_reviews, q25, q75

def import_albums():
    album_path = os.path.join(os.getcwd(), 'albums')
    album_list = []

    for artist in os.listdir(album_path):
        artist_path = os.path.join(album_path, artist)
        for album in os.listdir(artist_path):
            album_detail = {
                'albumName': album.split('.')[0],
                'artist': artist,
            }
            album_list.append(album_detail)
    return album_list

def assign_reviews_to_albums(albums, distribution_params, total_reviews):
    mean_reviews, std_dev_reviews, _, _ = distribution_params

    # Generate random values based on the specified distribution
    reviews_distribution = np.random.exponential(scale=mean_reviews, size=len(albums))
    
    # Ensure that the total number of reviews assigned is not greater than the specified limit
    reviews_distribution = np.minimum(reviews_distribution, total_reviews)
    
    # Ensure that the minimum number of reviews assigned is 1
    reviews_distribution = np.maximum(reviews_distribution, 1)
    
    # Normalize the distribution to sum up to total_reviews
    reviews_distribution = reviews_distribution / np.sum(reviews_distribution) * total_reviews
    
    # Round the values to integers
    reviews_distribution = np.round(reviews_distribution).astype(int)
    
    # Assign reviews to albums
    for i, album in enumerate(albums):
        album['reviews'] = np.random.randint(1, 6, size=reviews_distribution[i])

    return albums

def create_new_user(username):
    #generate a name and surname
    name = fak.first_name().replace("'", '').replace('"', '')
    surname = fak.last_name().replace("'", '').replace('"', '')


    # Example: Generate a random password with default length (12 characters)
    password = generate_password()

    #hash with SHA256
    hashed_password = hashlib.sha256(password.encode()).hexdigest()

    #generate a birth-date between 01/01/1950 and 31/12/2005
    birth_date = fak.date_between(start_date='-70y', end_date='-18y')
    birth_date = birth_date.strftime("%Y-%m-%d")

    #generate a registration-date between 01/01/2023 and 14/01/2024
    registration_date = fak.date_between(start_date='-1y', end_date='+2w')
    registration_date = registration_date.strftime("%Y-%m-%d")

    #print(user, name, surname, password, hashed_password, birth_date, registration_date)

    #create aa json with these fields
    file = {
        "name": name,
        "surname": surname,
        "username": username,
        "password": hashed_password,
        "birth_date": birth_date,
        "registration_date": registration_date,
        "reviewed_albums": []

    }

    tmp_path = user_path + "/" + str(username).replace("/", " ").replace("\\", " ").replace(":", " ").replace("*", " ").replace("?", " ").replace("\"", " ").replace("<", " ").replace(">", " ").replace("|", " ") + ".json"
    
    #save in a file
    try:
        with open(tmp_path, "w") as f:
            json.dump(file, f)
            f.close()
    except:
        print("can't save user", username)
        return False


    with open(os.path.join(user_path, "pass.txt"), "a") as f:
        f.write(username + " " + password + "\n")
        f.close()
    return True





def generate_random_timestamp():
    # Define the start and end dates for the desired range
    start_date_2023 = datetime(2023, 1, 1)
    end_date_2023 = datetime(2023, 12, 31)
    start_date_2024 = datetime(2024, 1, 1)
    end_date_2024 = datetime(2024, 1, 14)

    # Define the probability distribution for 2023 and 2024
    probabilities = [0.6, 0.4]  # Adjust as needed

    # Choose the year based on the probability distribution
    chosen_year = random.choices([start_date_2023.year, start_date_2024.year], weights=probabilities)[0]

    # Define the date range based on the chosen year
    if chosen_year == start_date_2023.year:
        start_date = start_date_2023
        end_date = end_date_2023
    else:
        start_date = start_date_2024
        end_date = end_date_2024

    # Calculate the total number of seconds in the date range
    total_seconds = (end_date - start_date).total_seconds()

    # Generate a random number of seconds offset from the start date
    random_seconds_offset = random.randint(0, int(total_seconds))

    # Calculate the random timestamp within the specified range
    random_timestamp = start_date + timedelta(seconds=random_seconds_offset)

    # Generate random hours, minutes, and seconds within the day
    random_hours = random.randint(0, 23)
    random_minutes = random.randint(0, 59)
    random_seconds = random.randint(0, 59)

    # Add the random time to the timestamp
    random_timestamp = random_timestamp.replace(
        hour=random_hours, minute=random_minutes, second=random_seconds
    )

    return random_timestamp

def get_cover_url(artist, album):
    album_path = os.path.join(os.getcwd(), 'albums', artist, album) + '.json'
    #print(album_path)
    try:
        file = json.load(open(album_path))
        return file['coverURL']
    except:
        return 'https://iili.io/HlHy9Yx.png'


    #print(file)
         

def save_reviews_to_file(albums):
    reviews_file_path = os.path.join(os.getcwd(), 'reviews', 'amazonReviews.json')

    reviews_save_path = os.path.join(os.getcwd(), 'reviews', 'Amazon')

    counter = 19716

    with open(reviews_file_path, 'r') as f:
        lines = f.readlines()
        #remove all the lines without all the fields we'll use 
        lines = [line for line in lines if all(field in line for field in ['reviewerName', 'overall', 'reviewText'])]

    for album in albums:
        # take the number of reviews assigned to this album
        tmp_number = len(album['reviews'])
        # take the name of the album
        tmp_name = album['albumName']
        # take the name of the artist
        tmp_artist = album['artist']

        for i in range(tmp_number):
            
            rnd = random.randint(0, len(lines) - 1)
            chosen_review = lines[rnd]

            # Check if the chosen_review is a non-empty string
            if chosen_review.strip():  # If it's not empty or just whitespace
                try:
                    # Try to load the chosen_review as JSON
                    chosen_review = json.loads(chosen_review)
                except json.JSONDecodeError:
                    # Handle the case where the content is not valid JSON
                    print(f"Error decoding JSON for review: {chosen_review}")
                    continue
            else:
                # Handle the case where the content is an empty string
                print("Empty review content")
                continue

            # Rest of the code remains the same
            document_to_be_saved = {
                "album": tmp_name,
                "artist": tmp_artist,
                "user": chosen_review['reviewerName'],
                "rating": chosen_review['overall'],
                "text": chosen_review['reviewText'],
                "timestamp": generate_random_timestamp().strftime("%Y-%m-%d %H:%M:%S")
            }


            username = chosen_review['reviewerName'].replace("/", "").replace("\\", "").replace(":", "").replace("*", "").replace("?", "").replace("\"", "").replace("<", "").replace(">", "").replace("|", "")
            tmp_path = reviews_save_path + '/' + username + '-' + tmp_name[:200] + '-' + str(counter) + '.json'
            #print(tmp_path)
            counter += 1
                                    #load as json
            app = {
                "album_title": tmp_name,
                "cover_url" : get_cover_url(tmp_artist, tmp_name),
                "artist": tmp_artist,
                "rating": document_to_be_saved["rating"]
            }

            for file in os.listdir(user_path):
                if file.replace(".json", "") == username:
                    
                    break
            else:
                #create new user
                if not create_new_user(username):

                    continue

            #append the review
            file = json.load(open(os.path.join(user_path, username)+'.json'))

            file["reviewed_albums"].append(app)
            #save the file
            with open(os.path.join(user_path, username)+'.json', 'w') as f:
                json.dump(file, f, indent=2)
            

            with open(tmp_path, 'w') as f:
                json.dump(document_to_be_saved, f, indent=2)

            if counter % 1000 == 0:
                print(counter)


# Import albums from the 'albums' directory
albums = import_albums()

print("Number of albums:", len(albums))

# Set the total number of reviews
total_reviews = 150000

# Read distribution parameters from log file
log_file_path = 'distribution_info.log'
distribution_params = read_distribution_params_from_log(log_file_path)

print("Distribution parameters:", distribution_params)

# Assign reviews to albums
albums_with_reviews = assign_reviews_to_albums(albums, distribution_params, total_reviews)

save_reviews_to_file(albums_with_reviews)

# Plot the distribution as a bar plot
unique_review_counts, count_of_each = np.unique([album['reviews'].size for album in albums_with_reviews], return_counts=True)

# Print the number of reviews and the number of albums with that number of reviews
print(unique_review_counts)
print(count_of_each)

with open('log.txt', 'w') as f:
    #write down name, aritst and number of reviews
    for album in albums_with_reviews:
        f.write(f"{album['albumName']} - {album['artist']} - {len(album['reviews'])}\n")


# Plot the distribution as a bar plot
plt.scatter(unique_review_counts, count_of_each)
plt.xlabel("Number of reviews")
plt.ylabel("Frequency")
plt.title("Distribution of number of reviews")
plt.show()
