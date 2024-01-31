import os
import json
import numpy as np
import matplotlib.pyplot as plt
from scipy.stats import anderson



reviews_path = os.path.join(os.getcwd(), "reviews", "amazonReviews.json")

asins = {}
with open(reviews_path, "r") as f:
    lines = f.read()

    for line in lines.split("\n"):
        if line.strip() == "":
            continue
        line = json.loads(line)

        asin = line["asin"]

        if asin not in asins:
            asins[asin] = 1
        else:
            asins[asin] += 1

# Plot the distribution as a function
reviews = list(asins.values())
unique_reviews, frequencies = zip(*[(r, reviews.count(r)) for r in set(reviews)])
plt.scatter(unique_reviews, frequencies)
plt.xlabel("Number of reviews")
plt.ylabel("Frequency")
plt.title("Distribution of number of reviews")
plt.show()

# Anderson-Darling test for exponential distribution
result = anderson(reviews, dist='expon')

print("Anderson-Darling test statistic:", result.statistic)
print("Critical values at 1%, 5%, and 10% significance:", result.critical_values)
print("p-value:", result.significance_level)

# Extract relevant values for reuse
mean_reviews = np.mean(reviews)
std_dev_reviews = np.std(reviews)
q25 = np.percentile(reviews, 25)
q75 = np.percentile(reviews, 75)
ad_statistic = result.statistic
critical_values = result.critical_values
p_values = result.significance_level

# Save the extracted values to a log file
log_filename = "distribution_info.log"
with open(log_filename, 'w') as log_file:
    log_file.write(f"Mean of reviews: {mean_reviews}\n")
    log_file.write(f"Standard deviation of reviews: {std_dev_reviews}\n")
    log_file.write(f"25th percentile: {q25}\n")
    log_file.write(f"75th percentile: {q75}\n")
    log_file.write(f"Anderson-Darling test statistic: {ad_statistic}\n")
    log_file.write(f"Critical values at 1%, 5%, and 10% significance: {critical_values}\n")
    log_file.write(f"P-values: {p_values}\n")

print(f"Information saved to {log_filename}")



