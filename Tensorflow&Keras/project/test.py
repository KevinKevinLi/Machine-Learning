import numpy as np
np.set_printoptions(threshold=np.inf)
import tensorflow as tf
np.random.seed(1337)  # for reproducibility
import matplotlib.pyplot as plt
import pandas as pd
from keras.models import Sequential
from keras.layers import LSTM, TimeDistributed, Dense
from keras.optimizers import Adam

# import test data
testpanda =pd.read_csv('DataSet/Stock/1102_origin_28.csv', usecols=[0,1,2,3,4,5,6], header=None,
               delimiter=",", )
testvalue = testpanda.values
testlabelpanda = pd.read_csv('DataSet/Stock/1102_origin_28.csv', usecols=[7], header=None, delimiter=",")
testlabelvalue = testlabelpanda.values
# train, test, train_labels, test_labels = train_test_split(datavalue,labelvalue,test_size=0.1)
num_test = testvalue.shape[0]
test = np.float32(testvalue)
test = tf.reshape(test,[-1,5,7])
sess = tf.Session()
print(sess.run(test))