"""
<<<<<<< HEAD
DON'T USE THIS CODE ON WINDOWS.
IF YOU USE WINDOWS, YOU CAN RUN THIS CODE ON GOOGLE COLAB

=======
@author : Hyunwoong
@when : 2019-11-29
@homepage : https://github.com/gusdnd852
"""
import tensorflow as tf

import numpy as np
from PIL import Image

# 1. upload your model file to Colab

# 2. convert to tflite file
converter = tf.lite.TFLiteConverter.from_keras_model_file('last.hdf5')
model = converter.convert()

# 3. save file
file = open('model.tflite', 'wb')
file.write(model)

# 4. test
interpreter = tf.lite.Interpreter(model_path="model.tflite")
interpreter.allocate_tensors()

image = Image.open('class_01_swallow_tail.jpg')
# image = Image.open('class_18_canker.jpg')

np_image = np.array(image, dtype=float)
np_image /= 255.0  # rescale
np_image = np_image.astype('float32')
np_image = np.expand_dims(np_image, axis=0)
input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()
interpreter.set_tensor(input_details[0]['index'], np_image)
interpreter.invoke()
tflite_results = interpreter.get_tensor(output_details[0]['index'])
print(tflite_results.argmax())

# 5. Download your model file to local storage
