# -*- coding: utf-8 -*-
from __future__ import print_function

"""
@author : Hyunwoong
@when : 2019-11-27
@homepage : https://github.com/gusdnd852
"""
import tensorflow as tf
from keras.layers import Conv2D
from keras.layers import Dense
from keras.layers import MaxPooling2D
from keras.layers import Activation
from keras.layers import GlobalAveragePooling2D

from keras.layers import Dropout
from keras.layers import BatchNormalization
from keras.layers import Concatenate
from keras.layers import Input

from keras import backend as K
from keras.models import Model

from keras import regularizers

weight_decay = 0.005
axis = 1 if K.image_data_format() == 'channels_first' else -1


def activation(x):
    return tf.nn.relu(x)


def conv2d(x, filters, kernel_size,
           strides=1, padding='same',
           kernel_regularizer=regularizers.l2(weight_decay),
           kernel_initializer='he_normal'):
    x = Conv2D(filters=filters,
               kernel_size=(kernel_size, kernel_size),
               strides=(strides, strides),
               padding=padding,
               kernel_regularizer=kernel_regularizer,
               kernel_initializer=kernel_initializer)(x)

    x = BatchNormalization()(x)
    x = Activation(activation)(x)
    return x


def pool2d(x, kernel_size, strides, padding='same'):
    x = MaxPooling2D(pool_size=kernel_size, strides=strides, padding=padding)(x)
    return x


def intermediate_block(x, filters):
    shortcut = x
    x = conv2d(x, filters=filters, kernel_size=3)
    x = conv2d(x, filters=filters, kernel_size=1)
    x = conv2d(x, filters=filters, kernel_size=1)
    x = Concatenate()([shortcut, x])
    return x


def down_sampling_block(x, filters):
    shortcut = pool2d(x, kernel_size=2, strides=2)
    x = conv2d(x, filters=filters, kernel_size=3)
    x = conv2d(x, filters=filters, kernel_size=1)
    x = conv2d(x, filters=filters, kernel_size=1)
    x = pool2d(x, kernel_size=2, strides=2)
    x = Concatenate()([shortcut, x])
    return x


def stem(x):
    x = conv2d(x, filters=32, kernel_size=7, strides=2)
    x = pool2d(x, kernel_size=3, strides=2)
    return x


def network(input_shape, classes):
    inputs = Input(shape=input_shape)  # [224, 224, 3]

    x = stem(inputs)
    # [56, 56, 32]

    x = intermediate_block(x, filters=64)
    # [56, 56, 64] + [56, 56, 32] = [56, 56, 96]
    x = down_sampling_block(x, filters=96)
    # [28, 28, 96] + [28, 28, 96] = [28, 28, 192]
    x = intermediate_block(x, filters=192)
    # [28, 28, 192] + [28, 28, 192] = [28, 28, 384]
    x = down_sampling_block(x, filters=384)
    # [14, 14, 384] + [14, 14, 384] = [14, 14, 768]

    x = conv2d(x, filters=512, kernel_size=1)
    x = conv2d(x, filters=512, kernel_size=1)
    # [14, 14, 512]
    x = pool2d(x, kernel_size=2, strides=2)
    # [7, 7, 512]

    x = GlobalAveragePooling2D()(x)
    # [1, 1, 512]

    x = Dense(512, activation=activation)(x)
    x = Dropout(0.5)(x)
    x = Dense(classes, activation='softmax')(x)
    # [1, 1, 24]

    return Model(inputs=inputs, outputs=x, name='weakly_dense_net')
