import pandas as pd
import numpy as np
from keras.utils import np_utils
from keras.models import Sequential
from keras.layers import Dense, Dropout, Activation, Flatten, LSTM, TimeDistributed, RepeatVector
from keras.callbacks import EarlyStopping, ModelCheckpoint
np.set_printoptions(threshold=np.inf)


def readTrain():
  train = pd.read_csv("980101190212_class.csv")
  return train


def augFeatures(train):
  train["Date"] = pd.to_datetime(train["Date"])
  train["day"] = train["Date"].dt.dayofweek
  return train


def normalize(train):
  train = train.drop(["Date"], axis=1)
  # train = train.drop(["day"])
  train_norm = train.iloc[:,0:6].apply(lambda x: (x - np.min(x)) / (np.max(x) - np.min(x)))
  train_norm = np.c_[train_norm, train["Class"]]
  train_norm = pd.DataFrame(train_norm, columns=["Open","High","Low","Close","Adj Close","Volume","Class"])

  return train_norm


def onehot(train):
  onehot = np_utils.to_categorical(train["day"], num_classes = 5)
  result = np.c_[train, onehot]
  # convert nparray to pandas dataframe
  result = pd.DataFrame(result, columns=["Open","High","Low","Close","Adj Close","Volume","Class","day","one","two","three","four","five"])
  return result


# train days, future days
def buildTrain(train, pastDay, futureDay):
  X_train, Y_train = [], []
  for i in range(train.shape[0]-futureDay-pastDay):
    # 0,1,2,4,5,6 | one hot 8 9 10 11 12
    X_train.append(np.array(train.iloc[i:i+pastDay,[0,1,2,4,5]]))
    Y_train.append(np.array(train.iloc[i+pastDay-1:i+pastDay+futureDay-1]["Class"]))
  return np.array(X_train), np.array(Y_train)


# extract to validate data
def splitData(X,Y,rate):
  X_train = X[:int(X.shape[0]*(1-rate))]
  Y_train = Y[:int(X.shape[0]*(1-rate))]
  X_val = X[int(X.shape[0]*(1-rate)):]
  Y_val = Y[int(X.shape[0]*(1-rate)):]
  return X_train, Y_train, X_val, Y_val


def buildManyToOneModel(shape):
  model = Sequential()
  model.add(LSTM(32, input_length=shape[1], input_dim=shape[2]))
  # output shape: (1, 1)
  model.add(Dense(16, activation='relu'))
  model.add(Dense(1,activation='sigmoid'))
  model.compile(loss="binary_crossentropy", optimizer="rmsprop")
  model.summary()
  return model


if __name__ == "__main__":
  train = readTrain()
  # Augment the features (year, month, date, day)
  train_Aug = augFeatures(train)

  # normalize data
  train_norm = normalize(train_Aug)

  # use one hot code to week of the day
  # train_hot = onehot(train_norm)

  # change the last day and next day
  X_train, Y_train = buildTrain(train_norm, 3, 1)

  # because no return sequence, Y_train and Y_val shape must be 2 dimension
  X_train, Y_train, X_val, Y_val = splitData(X_train, Y_train, 0.1)

  model = buildManyToOneModel(X_train.shape)
  # callback patience(the rounds that not imrove accuracy); basedline
  callback = EarlyStopping(monitor="loss", patience=50, verbose=1, mode="auto")
  model.fit(X_train, Y_train, epochs=1000, batch_size=128, validation_data=(X_val, Y_val), callbacks=[callback])
  #model.fit(X_train, Y_train, epochs=1000, batch_size=128, validation_data=(X_val, Y_val))

  model.save("m1.h5")