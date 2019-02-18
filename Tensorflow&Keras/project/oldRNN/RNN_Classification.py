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
te = pd.read_csv('DataSet/cartrain.csv', usecols=[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20], header=None,
                 delimiter=";")
tev = te.values
tel = pd.read_csv('DataSet/cartrain.csv', usecols=[21,22,23,24], header=None, delimiter=";")
telv = tel.values

train = numpy.float32(tv)
train_labels = numpy.float32(tlv)
test = numpy.float32(tev)
test_labels = numpy.float32(telv)

# parameter
learningrate = 0.1;
batch_size = 100;
n_hidden_neurons = 10;
n_inputs = 21;
n_classes = 4;
n_steps = 1;
train_iters = 10000;

# define placeholder
xs = tf.placeholder(tf.float32, [None, n_steps, n_inputs])
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
def RNN(inputs, weights, biases):
    # add one more layer to the network and return the output
    inputs = tf.reshape(inputs, [-1, n_inputs])
    hidden_in = tf.matmul(inputs, weights['in'])+biases['in']
    hidden_in = tf.reshape(hidden_in, [-1, n_steps, n_hidden_neurons])
    # neurons
    lstm_neuron = tf.nn.rnn_cell.LSTMCell(n_hidden_neurons, forget_bias=1.0, state_is_tuple=True,name='basic_lstm_cell')
    # lstm is divided into two parts (c_state, m_state) by state is tuple
    _init_state = lstm_neuron.zero_state(batch_size, dtype=tf.float32)
    outputs, states = tf.nn.dynamic_rnn(lstm_neuron, hidden_in, initial_state=_init_state, time_major=False)
    results = tf.matmul(states[1], weights['out']) + biases['out']
    return results


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
        # numpy.random.shuffle(perm)
        tv = tv[perm]
        tlv = tlv[perm]
        # start next epoch
        start = 0
        index_in_epoch = batch_size
        assert batch_size <= num_examples

    end = index_in_epoch
    return tv[start:end], tlv[start:end]


# add output layer
prediction = RNN(xs, weights, biases)

# compute error #reduction_indices=1, push column ; =0 push row
cross_entropy = tf.reduce_mean(-tf.reduce_sum(ys * tf.log(prediction), reduction_indices=[1]))
# cross_entropy = tf.nn.softmax_cross_entropy_with_logits_v2(logits=prediction, labels=ys)
train_step = tf.train.AdamOptimizer(learningrate).minimize(cross_entropy)


init = tf.global_variables_initializer()
with tf.Session() as sess:
    sess.run(init)
    step = 0
    while step * batch_size < train_iters:
       batch_train, batch_trainlabels=next_batch(batch_size)
       batch_train = batch_train.reshape([batch_size, n_steps, n_inputs])
       sess.run(train_step, feed_dict={xs: batch_train,
                                       ys: batch_trainlabels})
       if step % 10 == 0:
           # test = test.reshape([-1, n_steps, n_inputs])
           test, test_labels = next_batch(batch_size)
           test = test.reshape([batch_size, n_steps, n_inputs])
           print(compute_accuracy(test, test_labels))
       step += 1


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
