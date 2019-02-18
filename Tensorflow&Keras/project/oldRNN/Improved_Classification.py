import tensorflow as tf
import numpy
import pandas as pd
tf.set_random_seed(54321)

# import data
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

# parameter
learningrate = 0.1;
batch_size = 1377;
n_hidden_neurons = 10;
n_inputs = 21;
n_classes = 4;

# define placeholder
xs = tf.placeholder(tf.float32, [None, n_inputs])
ys = tf.placeholder(tf.float32, [None, n_classes])

# Define weights, bias based on hidden_neurons
weights = {
    # two hidden layers
    'in': tf.Variable(tf.random_normal([n_inputs, n_hidden_neurons])),
    'out': tf.Variable(tf.random_normal([n_hidden_neurons, n_classes]), name='weights')
}
biases = {
    'in': tf.Variable(tf.constant(0.1, shape=[n_hidden_neurons])),
    'out': tf.Variable(tf.constant(0.1, shape=[n_classes]), name='biases')
}


# tensorflow
def add_layer(inputs, weights, biases, activation_function=None,):
    # add one more layer to the network and return the output
    hidden_in = tf.matmul(inputs, weights['in'])+biases['in'];
    result = tf.matmul(hidden_in, weights['out']) + biases['out']
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


# batch function
epochs_completed = 0
index_in_epoch = 0
num_examples = tv.shape[0]
# for splitting out batches of data
def next_batch(batch_size):
    global tv
    global tlv
    global index_in_epoch
    global epochs_completed

    start = index_in_epoch
    index_in_epoch += batch_size

    # when all trainig data have been already used, it is reorder randomly
    if index_in_epoch > num_examples:
        # finished epoch
        epochs_completed += 1
        # shuffle the data
        perm = numpy.arange(num_examples)
        numpy.random.shuffle(perm)
        tv = tv[perm]
        tlv = tlv[perm]
        # start next epoch
        start = 0
        index_in_epoch = batch_size
        assert batch_size <= num_examples

    end = index_in_epoch
    return tv[start:end], tlv[start:end]


# add output layer
prediction = add_layer(xs, weights, biases, activation_function=tf.nn.softmax)

# compute error #reduction_indices=1, push column ; =0 push row
cross_entropy = tf.reduce_mean(-tf.reduce_sum(ys * tf.log(prediction), reduction_indices=[1]))
# cross_entropy = tf.nn.softmax_cross_entropy_with_logits_v2(logits=prediction, labels=ys)
train_step = tf.train.GradientDescentOptimizer(learningrate).minimize(cross_entropy)

sess = tf.Session()
init = tf.global_variables_initializer()
sess.run(init)

for i in range(1000):
    batch_train, batch_trainlabels=next_batch(batch_size)
    sess.run(train_step, feed_dict={xs: batch_train, ys: batch_trainlabels})
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
