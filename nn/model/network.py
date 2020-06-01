# -*- coding: utf-8 -*-
from __future__ import print_function

from keras import backend as K
from keras import regularizers
from keras.layers import Activation, DepthwiseConv2D
from keras.layers import BatchNormalization
from keras.layers import Concatenate
from keras.layers import Conv2D
from keras.layers import Dense
from keras.layers import Dropout
from keras.layers import GlobalAveragePooling2D
from keras.layers import Input
from keras.layers import MaxPooling2D
from keras.models import Model

"""
State of the Art 
Bridge Pyramid Network : 0.9624
"""

weight_decay = 0.005
axis = 1 if K.image_data_format() == 'channels_first' else -1


def convolution(x, filters, kernel_size, strides=(1, 1), bias=True):
    x = Conv2D(filters=filters, kernel_size=kernel_size, strides=strides, padding='same',
               use_bias=bias, kernel_regularizer=regularizers.l2(weight_decay))(x)
    x = BatchNormalization()(x)
    x = Activation('relu')(x)
    return x


def pooling(x, pool_size=(2, 2), strides=(2, 2)):
    x = MaxPooling2D(pool_size=pool_size, strides=strides, padding='same')(x)
    return x


def stem(x):
    x = convolution(x, filters=32, kernel_size=(7, 7), strides=(2, 2))
    x = pooling(x, pool_size=(3, 3))
    return x


def bridge(x, filters):
    x = convolution(x, filters, kernel_size=(1, 1), bias=False)
    x = convolution(x, filters, kernel_size=(1, 1), bias=False)
    return x


def pyramid(x, filters):
    z1 = x
    z2 = convolution(z1, filters=filters // 2, kernel_size=(3, 3))
    z3 = convolution(z2, filters=filters // 2, kernel_size=(3, 3))
    # Pyramid Convolution : Deep Inception

    out = Concatenate()([z1, z2, z3])
    out = bridge(out, filters)
    # Mix information across channel
    return out


def network(input_shape, classes):
    inputs = Input(shape=input_shape)

    x1 = stem(inputs)
    x2 = pyramid(x1, filters=64)
    pool1 = pooling(x2)

    x3 = Concatenate(axis=-1)([x1, x2])
    x4 = pyramid(x3, filters=128)
    pool2 = pooling(x4)

    x5 = Concatenate(axis=-1)([pool1, pool2])
    x6 = pyramid(x5, filters=256)
    pool3 = pooling(x6)

    x7 = Concatenate(axis=-1)([pool2, x6])
    x8 = pyramid(x7, filters=256)
    pool4 = pooling(x8)

    x9 = Concatenate(axis=-1)([pool3, pool4])
    x10 = pyramid(x9, filters=512)
    pool5 = pooling(x10)

    bridge1 = bridge(x7, filters=256)
    pool_bridge1 = pooling(bridge1)

    bridge2 = Concatenate(axis=-1)([pool_bridge1, pool4])
    bridge3 = bridge(bridge2, filters=512)
    pool_bridge2 = pooling(bridge3)

    bridge4 = Concatenate(axis=-1)([pool_bridge2, pool5])
    bridge5 = bridge(bridge4, filters=512)

    out = GlobalAveragePooling2D()(bridge5)
    out = Dense(512, activation='relu')(out)
    out = Dropout(0.5)(out)
    out = Dense(classes, activation='softmax')(out)
    out = Model(inputs, outputs=out, name='my_network')
    return out
