import numpy as np
np.set_printoptions(threshold=np.inf)
import tensorflow as tf
np.random.seed(1337)  # for reproducibility
import matplotlib.pyplot as plt
import pandas as pd
from keras.models import load_model

model = load_model('TensorGraph/Model/model11294.h5')
TIME_STEPS=5;
BATCH_SIZE=1;
INPUT_SIZE=7;
OUTPUT_SIZE=1;

# import test data
testpanda =pd.read_csv('DataSet/Stock/1029_1127.csv', usecols=[0,1,2,3,4,5,6], header=None,
               delimiter=",", )
testvalue = testpanda.values
testlabelpanda = pd.read_csv('DataSet/Stock/1029_1127.csv', usecols=[7], header=None, delimiter=",")
testlabelvalue = testlabelpanda.values
# train, test, train_labels, test_labels = train_test_split(datavalue,labelvalue,test_size=0.1)
num_test = testvalue.shape[0]
test = np.float32(testvalue)
testlabels = np.float32(testlabelvalue)

index_in_epoch = 0
def next_batch():
    global testvalue
    global testlabels
    global index_in_epoch
    global epochs_completed
    global BATCH_SIZE

    start = index_in_epoch
    index_in_epoch += BATCH_SIZE*TIME_STEPS

    # when all trainig data have been already used, it is not reorder randomly
    if index_in_epoch > num_test:
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
        assert BATCH_SIZE*TIME_STEPS <= num_test

    end = index_in_epoch
    # print("%f -> %f " % (start,end))
    xs = np.arange(start,start+TIME_STEPS*BATCH_SIZE)
    return testvalue[start:end][np.newaxis,:], testlabels[start:end][np.newaxis,:], xs

n = num_test/(TIME_STEPS*BATCH_SIZE)
totalcost=0
i=0
totalpred=[[0 for i in range(int(TIME_STEPS))] for i in range(int(n))]
totallabel=[[0 for i in range(int(TIME_STEPS))] for i in range(int(n))]
for step in range(int(n)):
    X_batch, Y_batch, xs = next_batch()
    # plt.plot(xs, Y_batch,'b')
    # X_batch = tf.reshape(X_batch, [BATCH_SIZE, TIME_STEPS, INPUT_SIZE])
    # Y_batch = tf.reshape(Y_batch, [BATCH_SIZE, TIME_STEPS, OUTPUT_SIZE])
    # print(np.array2string(test, precision=5, separator=','))
    pred = model.predict(X_batch,BATCH_SIZE)
    totalpred[i]=pred.flatten()
    totallabel[i]=Y_batch.flatten()
    # print(pred)
    cost = model.train_on_batch(X_batch, Y_batch)
    totalcost += cost;
    # print(pred.shape)
    # str = np.array2string(pred, precision=5, separator=',')
    # with open('TensorGraph/logs/temp1123.txt', 'w') as f:
    #     f.write(str)
    # xs = np.arange(num_test)
    # Y_batch=tf.reshape(Y_batch,[-1])
    plt.plot(xs, pred.flatten(),'r')
    plt.plot(xs, Y_batch.flatten(), 'b')
    plt.draw()
    plt.pause(0.1)
    i+=1

#print(totalpred)
print(totalcost)
xs = np.arange(1,int(n)*TIME_STEPS*BATCH_SIZE+1)
print(xs)
plt.plot(xs, np.reshape(totalpred,[-1]),'r')
plt.plot(xs, np.reshape(totallabel,[-1]),'b')
plt.draw()
plt.pause(0.1)