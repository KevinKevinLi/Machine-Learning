import numpy as np

from keras.models import load_model
from train import readTrain
from train import augFeatures
from train import buildTrain
from train import splitData
from train import normalize

# Adj Close   mean 1482.116 min 676.53 max 2930.75

# mean = 1482.116
# min = 676.53
# max = 2930.75

mean = 0
min = 0
max = 1

model = load_model("v1.0/m12.h5")

train = readTrain()
train_Aug = augFeatures(train)
train_norm = normalize(train_Aug)
X_train, Y_train = buildTrain(train_norm, 3, 1)
X_train, Y_train, X_val, Y_val = splitData(X_train, Y_train, 0.1)

prediction = model.predict(X_val)

print(Y_val.shape,prediction.shape)

for i in range(len(prediction)):
    print(prediction[i]*(max-min)+mean, Y_val[i]*(max-min)+mean)

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

sum = 0
count3 = 0
for i in range(len(prediction)):
    sum += (Y_val[i] - prediction[i])*(Y_val[i] - prediction[i])
    count3 += 1

print(sum/count3)