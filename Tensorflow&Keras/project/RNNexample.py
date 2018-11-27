import tensorflow as tf
from tensorflow.examples.tutorials.mnist import input_data
tf.set_random_seed(5432)

# Just disables the warning, doesn't enable AVX/FMA
import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

# data import
mnist = input_data.read_data_sets("MNIST_data", one_hot=True)
# print(mnist.train.images.shape)

# parameters
learningrate = 0.001
iterations = 100000
batch_size = 128
training_iters = 100000
# display_step=10

n_inputs = 28  # the image is 28*28
n_steps = 28
n_hidden_neurons = 128
n_classes = 10  # 0-9

# tf graph input
x = tf.placeholder(tf.float32, [None, n_steps, n_inputs])
y = tf.placeholder(tf.float32, [None, n_classes])

# Define weights and bias
weights = {
    # two hidden layers
    # (28, 128)
    'in': tf.Variable(tf.random_normal([n_inputs, n_hidden_neurons])),
    # (128, 10)
    'out': tf.Variable(tf.random_normal([n_hidden_neurons, n_classes]))
}
biases = {
    # (128,)
    'in': tf.Variable(tf.constant(0.1, shape=[n_hidden_neurons, ])),
    # (10, )
    'out': tf.Variable(tf.constant(0.1, shape=[n_classes, ]))
}

def RNN(X, weights, biases):
    # hiddenlayer from input to neurons
    # X (128 batch, 28 steps, 28 inputs)
    # ==> (128*28, 28inputs)
    # -1 is inferred to some number
    X = tf.reshape(X, [-1, n_inputs])
    # X_in ==> (128batch*28steps,128hidden)
    X_in = tf.matmul(X, weights['in'])+biases['in']
    # X_in ==> (128batch, 28steps, 128hidden)
    X_in = tf.reshape(X_in, [-1, n_steps, n_hidden_neurons])

    # neurons
    lstm_neuron = tf.nn.rnn_cell.BasicLSTMCell(n_hidden_neurons, forget_bias=1.0, state_is_tuple=True)
    # lstm is divided into two parts (c_state, m_state) by state is tuple
    _init_state = lstm_neuron.zero_state(batch_size, dtype=tf.float32)

    outputs, states = tf.nn.dynamic_rnn(lstm_neuron, X_in, initial_state=_init_state, time_major=False)
    # time_major: whether first line is time step

    # hidden layer for output as the final results
    # method 1 states[1]=m_state
    results = tf.matmul(states[1], weights['out'])+biases['out']

    # method 2 outputs[-1] is the last output for the model(0-9)
    # unpack to list [(batch, outpurs)..]* steps
    # outputs = tf.unpack(tf.transpose(outputs, [1, 0, 2]))
    # results = tf.matmul(outputs[-1], weights['out']) + biases['out']
    return results


pred = RNN(x, weights, biases)
cost = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(logits=pred, labels=y))
train_op = tf.train.AdamOptimizer(learningrate).minimize(cost)

correct_pred = tf.equal(tf.argmax(pred, 1), tf.argmax(y, 1))
accuracy = tf.reduce_mean(tf.cast(correct_pred, tf.float32))

init = tf.global_variables_initializer()
with tf.Session() as sess:
    sess.run(init)
    step = 0
    while step * batch_size < training_iters:
        batch_xs, batch_ys = mnist.train.next_batch(batch_size)
        batch_xs = batch_xs.reshape([batch_size, n_steps, n_inputs])
        sess.run([train_op], feed_dict={
            x: batch_xs,
            y: batch_ys,
        })
        if step % 20 == 0:
            print(sess.run(accuracy, feed_dict={
                x: batch_xs,
                y: batch_ys,
            }))
        step += 1
