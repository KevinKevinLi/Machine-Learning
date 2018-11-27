"""
To know more or get code samples, please visit my website:
https://morvanzhou.github.io/tutorials/
Or search: 莫烦Python
Thank you for supporting!
"""

# please note, all tutorial code are running under python3.5.
# If you use the version like python2.7, please modify the code accordingly

# 8 - RNN LSTM Regressor example

# to try tensorflow, un-comment following two lines
# import os
# os.environ['KERAS_BACKEND']='tensorflow'
import numpy as np
np.set_printoptions(threshold=np.inf)
import tensorflow as tf
np.random.seed(1337)  # for reproducibility
import matplotlib.pyplot as plt
import pandas as pd
from keras.models import Sequential
from keras.layers import LSTM, TimeDistributed, Dense
from keras.optimizers import Adam
from keras.models import load_model

BATCH_START = 0
TIME_STEPS = 5
BATCH_SIZE = 1
INPUT_SIZE = 7
OUTPUT_SIZE = 1
CELL_SIZE = 10
LR = 0.01

# import data
datapanda =pd.read_csv('DataSet/Stock/1102_origin.csv', usecols=[0,1,2,3,4,5,6], header=None,
               delimiter=",", )
datavalue = datapanda.values
labelpanda = pd.read_csv('DataSet/Stock/1102_origin.csv', usecols=[7], header=None, delimiter=",")
labelvalue = labelpanda.values
# train, test, train_labels, test_labels = train_test_split(datavalue,labelvalue,test_size=0.1)
train = np.float32(datavalue)
train_labels = np.float32(labelvalue)

# batch function
epochs_completed = 0
index_in_epoch = 0
num_examples = datavalue.shape[0]
def next_batch():
    global datavalue
    global train_labels
    global index_in_epoch
    global epochs_completed
    global BATCH_SIZE

    start = index_in_epoch
    index_in_epoch += BATCH_SIZE*TIME_STEPS

    # when all trainig data have been already used, it is not reorder randomly
    if index_in_epoch > num_examples:
        # finished epoch
        epochs_completed += 1
        # shuffle the data randomly
        # perm = np.arange(num_examples)
        # print(perm)
        # numpy.random.shuffle(perm)
        # datavalue = datavalue[perm]
        # train_labels = train_labels[perm]
        # start next epoch
        start = 0
        index_in_epoch = BATCH_SIZE*TIME_STEPS
        assert BATCH_SIZE*TIME_STEPS <= num_examples

    end = index_in_epoch
    # print("%f -> %f " % (start,end))
    xs = np.arange(start,start+TIME_STEPS*BATCH_SIZE)
    return datavalue[start:end], train_labels[start:end], xs


model = Sequential()
# build a LSTM RNN
model.add(LSTM(
    batch_input_shape=(BATCH_SIZE, TIME_STEPS, INPUT_SIZE),       # Or: input_dim=INPUT_SIZE, input_length=TIME_STEPS,
    output_dim=CELL_SIZE,
    return_sequences=True,      # True: output at all steps. False: output as last step.
    stateful=True,              # True: the final state of batch1 is feed into the initial state of batch2
))
# add output layer
model.add(TimeDistributed(Dense(OUTPUT_SIZE)))
adam = Adam(LR)
model.compile(optimizer=adam,
              loss='mse',)

str = ''
print('Training ------------')
for step in range(1000):
    # data shape = (batch_num, steps, inputs/outputs)
    X_batch, Y_batch, xs = next_batch()
    X_batch = tf.reshape(X_batch, [BATCH_SIZE, TIME_STEPS, INPUT_SIZE])
    Y_batch = tf.reshape(Y_batch, [BATCH_SIZE, TIME_STEPS, OUTPUT_SIZE])
    # print(X_batch.shape)
    cost = model.train_on_batch(X_batch, Y_batch)

    # plt.plot(xs[0, :], Y_batch[0].flatten(), 'r', xs[0, :], pred.flatten()[:TIME_STEPS], 'b--')
    # plt.ylim((-1.2, 1.2))
    # Y_batch = tf.reshape(Y_batch, [-1])
    # print(pred)
    if step % 10 == 0:
        print('train cost: ', cost)
        pred = model.predict(X_batch, steps=1)
        # Y_batch = tf.reshape(Y_batch,[-1]);
        y = Y_batch.eval(session=tf.keras.backend.get_session())
        print("step",step)
        print(pred)
        print(y)
        # print(pred.shape)
        # plt.plot(xs, pred.flatten(), 'r')
        # plt.plot(xs, y, 'b')
        # plt.draw()
        # plt.pause(0.1)

model.save('TensorGraph/Model/model1123.h5')


# import test data
testpanda =pd.read_csv('DataSet/Stock/1102_origin_28.csv', usecols=[0,1,2,3,4,5,6], header=None,
               delimiter=",", )
testvalue = testpanda.values
testlabelpanda = pd.read_csv('DataSet/Stock/1102_origin_28.csv', usecols=[7], header=None, delimiter=",")
testlabelvalue = testlabelpanda.values
# train, test, train_labels, test_labels = train_test_split(datavalue,labelvalue,test_size=0.1)
num_test = testvalue.shape[0]
test = np.float32(testvalue)
test = tf.reshape(test,[-1,TIME_STEPS,INPUT_SIZE])
print(np.array2string(test, precision=5, separator=','))
pred = model.predict(testvalue, steps=1)
# print(pred.shape)
str = np.array2string(pred, precision=5, separator=',')
with open('TensorGraph/logs/temp1123.txt', 'w') as f:
    f.write(str)
xs = np.arange(num_test)
plt.plot(xs, pred.flatten())
plt.draw()
plt.pause(0.1)