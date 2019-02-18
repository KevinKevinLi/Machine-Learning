import numpy as np

from keras.models import load_model
from train_improve import readTrain
from train_improve import augFeatures
from train_improve import buildTrain
from train_improve import splitData
from train_improve import normalize
from train_improve import onehot

# Adj Close   mean 1482.116 min 676.53 max 2930.75

mean = 1482.116
min = 676.53
max = 2930.75

model = load_model("m1.h5")

train = readTrain()
# Augment the features (year, month, date, day)
train_Aug = augFeatures(train)

# normalize data
train_norm = normalize(train_Aug)

# use one hot code to week of the day
train_hot = onehot(train_norm)

# change the last day and next day
X_train, Y_train = buildTrain(train_hot, 3, 1)

# X_train, Y_train = shuffle(X_train, Y_train)
# because no return sequence, Y_train and Y_val shape must be 2 dimension
X_train, Y_train, X_val, Y_val = splitData(X_train, Y_train, 0.1)

prediction = model.predict(X_val)

print(X_train)

print(Y_train*(max-min)+min)

for i in range(len(prediction)):
    print(prediction[i]*(max-min)+min, Y_val[i]*(max-min)+min)

count1 = 0
for i in range(len(prediction)-1):
    if np.sign(prediction[i+1]-Y_val[i])==np.sign(Y_val[i+1]-Y_val[i]):
        count1 += 1

print("compare to true value, correct number is ",count1,"/",len(prediction))

count2 = 0
for i in range(len(prediction)-1):
    if np.sign(prediction[i+1]-prediction[i])==np.sign(Y_val[i+1]-Y_val[i]):
        count2 += 1

print("compare to predict value, correct number is ",count2,"/",len(prediction))