import tensorflow as tf
import numpy
import pandas as pd
tf.set_random_seed(54321)

t =pd.read_csv('DataSet/cartrain.csv', usecols=[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20], header=None,
               delimiter=";")
tv = t.values
tl = pd.read_csv('DataSet/cartrain.csv', usecols=[21,22,23,24], header=None, delimiter=";")
tlv = tl.values
te = pd.read_csv('DataSet/cartest.csv', usecols=[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20], header=None,
                 delimiter=";")
tev = te.values
tel = pd.read_csv('DataSet/cartest.csv', usecols=[21,22,23,24], header=None, delimiter=";")
telv = tel.values

train = numpy.float32(tv)
train_labels = numpy.float32(tlv)
test = numpy.float32(tev)
test_labels = numpy.float32(telv)


# tensorflow
def add_layer(inputs, insize, outsize, activation_function=None,):
    # add one more layer to the network and return the output
    weights = tf.Variable(tf.random_normal([insize, outsize]), name='weights')
    biases = tf.Variable(tf.zeros([1, outsize])+1, name='bias')
    result = tf.matmul(inputs, weights) + biases
    if activation_function is not None:
        result = activation_function(result)
    return result


def compute_accuracy(v_xs, v_ys):
    global prediction
    y_pre = sess.run(prediction, feed_dict={xs: v_xs})
    correct_prediction = tf.equal(tf.argmax(y_pre, 1), tf.argmax(v_ys, 1))
    accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))
    result = sess.run(accuracy, feed_dict={xs: v_xs, ys: v_ys})
    return result


# define placeholder
xs = tf.placeholder(tf.float32, [None, 21])
ys = tf.placeholder(tf.float32, [None, 4])

# add output layer
prediction = add_layer(xs, 21, 4, activation_function=tf.nn.softmax)

# compute error #reduction_indices=1, push column ; =0 push row
cross_entropy = tf.reduce_mean(-tf.reduce_sum(ys * tf.log(prediction), reduction_indices=[1]))
# cross_entropy = tf.nn.softmax_cross_entropy_with_logits_v2(logits=prediction, labels=ys)
train_step = tf.train.GradientDescentOptimizer(0.5).minimize(cross_entropy)

sess = tf.Session()
init = tf.global_variables_initializer()
sess.run(init)

for i in range(1000):
    sess.run(train_step, feed_dict={xs: tv, ys: tlv})
    if i % 50 == 0:
        print(compute_accuracy(test, test_labels))

# output log
# writer = tf.summary.FileWriter('./TensorGraph/logs', sess.graph)
# writer.close()

# print variables
# variable_names = [v.name for v in tf.trainable_variables()]
# values = sess.run(variable_names)
# for k, v in zip(variable_names, values):
#    print("Variable: ", k)
#    print("Shape: ", v.shape)
#    print(v)
