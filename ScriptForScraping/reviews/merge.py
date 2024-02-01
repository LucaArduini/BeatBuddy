import os
import json
import random
import re


users_path = os.path.join(os.getcwd(), "users", "data.json")

rev_path = os.path.join(os.getcwd(), "reviews", "json")


with open(users_path, "r") as f:
    lines = f.readlines()
    
    for line in lines:
        index = lines.index(line)

        if "'user'" in line:
            line = line.replace("'user'", '"user"')
            #substitute the old line with the new on

        if "'liked'" in line:
            line = line.replace("'liked'", '"liked"')
            #substitute the old line with the new on

        if "}{" in line:
            line = line.replace("}{", "}\n{")
            #substitute the old line with the new on


        if "'" in line:
            #find the index of all the matches
            indexes = [m.start() for m in re.finditer("'", line)]
            for i in indexes:
                if line[i - 1] == "[":
                    #substitute ' with "

                    line = line[:i] + '"' + line[i + 1:]
                
                elif line[i + 1] == "]":
                    #substitute ' with "

                    line = line[:i] + '"' + line[i + 1:]

                elif line[i+1] == ",":
                    #substitute ' with "

                    line = line[:i] + '"' + line[i+1:]

                elif line[i-1] == " " and (line[i-2] == ":" or line[i-2] == ","):
                    #substitute ' with "

                    line = line[:i] + '"' + line[i+1:]

                else:
                    continue


                

        lines[index] = line
        

with open(users_path, "w") as f:
    f.writelines(lines)

c = 0

chosen = []

for rev in os.listdir(rev_path):
    #open that file
    with open(os.path.join(rev_path, rev), "r") as f:
        #convert to json

        #CONVERT TO DICT

        rev_json = json.load(f)
        #print(rev_json)

        

        try:
            album_name = rev_json["album"]
            artist_name = rev_json["artist"]
            rev_json["rating"] = int(rev_json["rating"])

        except:
            print("no")
            continue

        #take a random line
        while True:
            r = random.randint(0, len(lines) - 1)
            if r not in chosen:
                chosen.append(r)
                break
        
        line = lines[r].strip()



        
        try:
            user_json = json.loads(line)


        except:
            while True:
                r = random.randint(0, len(lines) - 1)
                if r not in chosen:
                    chosen.append(r)
                    break
            line = lines[r].strip()
            user_json = json.loads(line)
            

        username = user_json["user"]

        rev_json["user"] = username

        user_json["liked"].append(album_name)

        line = str(user_json) + "\n"

        if "'user'" in line:
            line = line.replace("'user'", '"user"')
            #substitute the old line with the new on

        if "'liked'" in line:
            line = line.replace("'liked'", '"liked"')
            #substitute the old line with the new on

        if "}{" in line:
            line = line.replace("}{", "}\n{")
            #substitute the old line with the new on


        if "'" in line:
        #find the index of all the matches
            indexes = [m.start() for m in re.finditer("'", line)]
            for i in indexes:
                if line[i - 1] == "[":
                    #substitute ' with "
                    line = line[:i] + '"' + line[i + 1:]
                elif line[i + 1] == "]":
                    #substitute ' with "
                    line = line[:i] + '"' + line[i + 1:]
                elif line[i+1] == ",":
                    #substitute ' with "
                    line = line[:i] + '"' + line[i+1:]
                elif line[i-1] == " " and (line[i-2] == ":" or line[i-2] == ","):
                    #substitute ' with "
                    line = line[:i] + '"' + line[i+1:]        


    #save both files
                    
    new_path = os.path.join(os.getcwd(), "reviews", "Amazon")

    #remove the original file from rev_path

    os.remove(os.path.join(rev_path, rev))
    
    with open(os.path.join(new_path, rev), "w") as f:
        json.dump(rev_json, f, indent=2)

    with open(users_path, "w") as f:
        #substitute the old line with the new one
        lines[r] = line
        f.writelines(lines)


    c+=1

    print(c)