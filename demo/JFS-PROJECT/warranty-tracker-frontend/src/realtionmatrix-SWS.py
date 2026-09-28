np.random.seed(42) # For reproducibility

n_samples = 2000 # Increased sample size for more robust results

product_categories = ['Electronics', 'Appliances', 'Automotive', 'Home Goods', 'Wearables']
warranty_durations = [12, 24, 36, 48, 60] # in months

sample_data = {
    'Product_Category': np.random.choice(product_categories, n_samples),
    'Product_Age': np.random.randint(1, 72, n_samples), # Product age in months (1-6 years)
    'Warranty_Duration': np.random.choice(warranty_durations, n_samples),
    'Purchase_Price': np.random.randint(500, 5000, n_samples), # Price of the product
    'Customer_Satisfaction': np.random.randint(1, 11, n_samples), # Scale of 1 to 10
    'Previous_Renewal_History': np.random.randint(0, 4, n_samples), # Number of previous renewals
    'Service_Calls_Last_Year': np.random.randint(0, 6, n_samples), # Number of service calls
}

df = pd.DataFrame(sample_data)

# Introduce 'Warranty_Renewed' (target variable) with some logic
def generate_renewal_status(row):
    # Factors influencing renewal:
    # - Higher satisfaction -> more likely to renew
    # - Newer product -> more likely to renew (Product_Age inversely related)
    # - Previous renewals -> more likely to renew
    # - Lower service calls -> more likely to renew
    # - Higher purchase price -> more likely to renew (investment protection)

    renewal_prob = 0.3 # Base probability

    if row['Customer_Satisfaction'] > 7: renewal_prob += 0.2
    elif row['Customer_Satisfaction'] < 4: renewal_prob -= 0.15

    if row['Product_Age'] < 24: renewal_prob += 0.1
    elif row['Product_Age'] > 48: renewal_prob -= 0.1

    renewal_prob += row['Previous_Renewal_History'] * 0.05
    renewal_prob -= row['Service_Calls_Last_Year'] * 0.03

    if row['Purchase_Price'] > 3000: renewal_prob += 0.05

    # Ensure probability is within [0, 1]
    renewal_prob = max(0.05, min(0.95, renewal_prob))

    return 1 if np.random.rand() < renewal_prob else 0

df['Warranty_Renewed'] = df.apply(generate_renewal_status, axis=1)

# Introduce some missing values for demonstration
missing_cols = ['Customer_Satisfaction', 'Service_Calls_Last_Year', 'Product_Age']
for col in missing_cols:
    missing_indices = np.random.choice(df.index, int(n_samples * 0.02), replace=False)
    df.loc[missing_indices, col] = np.nan

data = df.copy()
print("Generated Dataset Head:")
display(data.head())
print("\nGenerated Dataset Shape:", data.shape)
print("\nStatistical Summary of Generated Data:")
display(data.describe())


# ==========================================
# SMART WARRANTY RENEWAL PREDICTION SYSTEM
# ==========================================

# Import Required Libraries

import pandas as pd
import numpy as np

import matplotlib.pyplot as plt
import seaborn as sns

from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelEncoder
from sklearn.metrics import accuracy_score
from sklearn.metrics import confusion_matrix
from sklearn.metrics import classification_report

from sklearn.linear_model import LogisticRegression
from sklearn.tree import DecisionTreeClassifier
from sklearn.neighbors import KNeighborsClassifier
from sklearn.ensemble import RandomForestClassifier

# ==========================================
# LOAD DATASET (Using Sample Data)
# ==========================================

# Generate sample data for demonstration
np.random.seed(42) # for reproducibility

n_samples = 1000
sample_data = {
    'Product_Category': np.random.choice(['Electronics', 'Appliances', 'Automotive', 'Home Goods'], n_samples),
    'Product_Age': np.random.randint(1, 60, n_samples), # in months
    'Warranty_Duration': np.random.choice([12, 24, 36], n_samples),
    'Product_Cost': np.random.randint(10000, 100000, n_samples),
    'Customer_Satisfaction': np.random.randint(1, 11, n_samples),
    'Previous_Renewal_History': np.random.randint(0, 3, n_samples), # number of previous renewals
    'Warranty_Renewed': np.random.choice([0, 1], n_samples, p=[0.7, 0.3]) # 70% not renewed, 30% renewed
}

data = pd.DataFrame(sample_data)

# Introduce some missing values in Customer_Satisfaction for demonstration of fillna
missing_indices = np.random.choice(data.index, int(n_samples * 0.05), replace=False)
data.loc[missing_indices, 'Customer_Satisfaction'] = np.nan

print("Dataset Shape:", data.shape)

print("\nFirst Five Records")
print(data.head())

# ==========================================
# DATA PREPROCESSING
# ==========================================

print("\nMissing Values")
print(data.isnull().sum())

# Remove duplicate records

data.drop_duplicates(inplace=True)

# Fill missing numerical values

data["Customer_Satisfaction"] = data[
    "Customer_Satisfaction"
].fillna(
    data["Customer_Satisfaction"].mean()
)

# ==========================================
# FEATURE ENCODING
# ==========================================

encoder = LabelEncoder()

data["Product_Category"] = encoder.fit_transform(
    data["Product_Category"]
)

# ==========================================
# EXPLORATORY DATA ANALYSIS
# ==========================================

plt.figure(figsize=(8,5))

sns.countplot(
    x="Warranty_Renewed",
    data=data
)

plt.title(
    "Warranty Renewal Distribution"
)

plt.show()

# Correlation Heatmap

plt.figure(figsize=(10,6))

sns.heatmap(
    data.corr(),
    annot=True,
    cmap="Blues"
)

plt.title(
    "Feature Correlation Matrix"
)

plt.show()

# ==========================================
# FEATURE SELECTION
# ==========================================

X = data[
    [
        "Product_Category",
        "Product_Age",
        "Warranty_Duration",
        "Product_Cost",
        "Customer_Satisfaction",
        "Previous_Renewal_History"
    ]
]

y = data["Warranty_Renewed"]

# ==========================================
# TRAIN TEST SPLIT
# ==========================================

X_train, X_test, y_train, y_test = train_test_split(
    X,
    y,
    test_size=0.20,
    random_state=42
)

print("\nTraining Samples:", len(X_train))
print("Testing Samples:", len(X_test))

# ==========================================
# MODEL 1
# LOGISTIC REGRESSION
# ==========================================

lr_model = LogisticRegression()

lr_model.fit(
    X_train,
    y_train
)

lr_pred = lr_model.predict(X_test)

lr_accuracy = accuracy_score(
    y_test,
    lr_pred
)

print(
    "\nLogistic Regression Accuracy:",
    lr_accuracy
)

# ==========================================
# MODEL 2
# DECISION TREE
# ==========================================

dt_model = DecisionTreeClassifier()

dt_model.fit(
    X_train,
    y_train
)

dt_pred = dt_model.predict(X_test)

dt_accuracy = accuracy_score(
    y_test,
    dt_pred
)

print(
    "Decision Tree Accuracy:",
    dt_accuracy
)

# ==========================================
# MODEL 3
# KNN
# ==========================================

knn_model = KNeighborsClassifier(
    n_neighbors=5
)

knn_model.fit(
    X_train,
    y_train
)

knn_pred = knn_model.predict(
    X_test
)

knn_accuracy = accuracy_score(
    y_test,
    knn_pred
)

print(
    "KNN Accuracy:",
    knn_accuracy
)

# ==========================================
# MODEL 4
# RANDOM FOREST
# ==========================================

rf_model = RandomForestClassifier(
    n_estimators=100,
    random_state=42
)

rf_model.fit(
    X_train,
    y_train
)

rf_pred = rf_model.predict(
    X_test
)

rf_accuracy = accuracy_score(
    y_test,
    rf_pred
)

print(
    "Random Forest Accuracy:",
    rf_accuracy
)

# ==========================================
# MODEL COMPARISON
# ==========================================

results = pd.DataFrame(
{
    "Algorithm":
    [
        "Logistic Regression",
        "Decision Tree",
        "KNN",
        "Random Forest"
    ],

    "Accuracy":
    [
        lr_accuracy,
        dt_accuracy,
        knn_accuracy,
        rf_accuracy
    ]
}
)

print("\nModel Comparison")
print(results)

# ==========================================
# CONFUSION MATRIX
# ==========================================

cm = confusion_matrix(
    y_test,
    rf_pred
)

print("\nConfusion Matrix")
print(cm)

sns.heatmap(
    cm,
    annot=True,
    fmt="d"
)

plt.title(
    "Random Forest Confusion Matrix"
)

plt.show()

# ==========================================
# CLASSIFICATION REPORT
# ==========================================

print(
    "\nClassification Report"
)

print(
    classification_report(
        y_test,
        rf_pred
    )
)

# ==========================================
# SAMPLE PREDICTION
# ==========================================

sample_customer = np.array(
[
    [
        1,      # Product Category (encoded value)
        2,      # Product Age
        24,     # Warranty Duration
        35000,  # Product Cost
        8,      # Satisfaction
        1       # Previous Renewal
    ]
]
)

prediction = rf_model.predict(
    sample_customer
)

print("\nPrediction Result")

if prediction[0] == 1:
    print("Likely to Renew")
else:
    print("Not Likely to Renew")
