"""
@author : Hyunwoong
@when : 2019-12-23
@homepage : https://github.com/gusdnd852
"""

import tensorflow as tf

interpreter = tf.lite.Interpreter(model_path="model.tflite")
interpreter.allocate_tensors()

# Print input shape and type
print(interpreter.get_input_details()[0]['shape'])  # Example: [1 224 224 3]
print(interpreter.get_input_details()[0]['dtype'])  # Example: <class 'numpy.float32'>

# Print output shape and type
print(interpreter.get_output_details()[0]['shape'])  # Example: [1 24]
print(interpreter.get_output_details()[0]['dtype'])  # Example: <class 'numpy.float32'>
