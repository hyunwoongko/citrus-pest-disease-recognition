# -*- coding: utf-8 -*-
from __future__ import division
from __future__ import print_function

from keras.callbacks import ReduceLROnPlateau, ModelCheckpoint

"""
@author : Hyunwoong
@when : 2019-11-27
@homepage : https://github.com/gusdnd852
"""

from keras.optimizers import SGD
from keras.preprocessing.image import ImageDataGenerator
from nn.conf import *
from nn.model.network import network
import tensorflow as tf

cfg = tf.ConfigProto()
cfg.gpu_options.allow_growth = True
sess = tf.Session(config=cfg)

model = network(input_shape=input_shape, classes=num_classes)
optimizer = SGD(lr=lr_base, momentum=0.9, nesterov=True)
model.compile(loss='categorical_crossentropy', optimizer=optimizer, metrics=['accuracy'])
model.summary()

train_data = ImageDataGenerator(rescale=1. / 255,
                                rotation_range=15,
                                width_shift_range=0.2,
                                height_shift_range=0.2,
                                horizontal_flip=True,
                                zoom_range=0.2,
                                shear_range=0.2,
                                fill_mode='nearest')

test_data = ImageDataGenerator(rescale=1. / 255)

train_generator = train_data.flow_from_directory(
    train_data_dir,
    target_size=(img_width, img_height),
    batch_size=batch_size,
    class_mode='categorical')

validation_generator = test_data.flow_from_directory(
    validation_data_dir,
    target_size=(img_width, img_height),
    batch_size=batch_size,
    class_mode='categorical')

lr_reduce = ReduceLROnPlateau(monitor='val_loss', factor=0.8, patience=7, mode='auto', min_lr=lr_base * 1e-7)

# store the best performance model according to val_acc

save_dir = os.path.join(os.getcwd(), 'ckpoint')
if not os.path.isdir(save_dir): os.makedirs(save_dir)
save_name = 'model_{epoch:02d}-{val_acc:.4f}.hdf5'
filepath = os.path.join(save_dir, save_name)
checkpoint = ModelCheckpoint(filepath=filepath,
                             monitor='val_acc',
                             verbose=1,
                             save_best_only=True,
                             mode='max')
callbacks = [lr_reduce, checkpoint]

hist = model.fit_generator(
    train_generator,
    steps_per_epoch=nb_train_samples // batch_size,
    epochs=epochs,
    validation_data=validation_generator,
    validation_steps=nb_validation_samples // batch_size,
    callbacks=callbacks)

# print acc and stored into acc.txt
f = open('./report/acc.txt', 'w')
f.write(str(hist.history['acc']))
f.close()
# print val_acc and stored into val_acc.txt
f = open('./report/val_acc.txt', 'w')
f.write(str(hist.history['val_acc']))
f.close()
# print val_loss and stored into val_loss.txt
f = open('./report/val_loss.txt', 'w')
f.write(str(hist.history['val_loss']))
f.close()
