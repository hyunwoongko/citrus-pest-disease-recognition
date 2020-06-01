"""
@author : Hyunwoong
@when : 2019-11-27
@homepage : https://github.com/gusdnd852
"""
import os

os.environ['CUDA_VISIBLE_DEVICES'] = '0'
train_data_dir = os.path.join(os.getcwd(), 'data/train')
validation_data_dir = os.path.join(os.getcwd(), 'data/validation')

batch_size = 16
epochs = 500
lr_base = 0.001

num_classes = 24
nb_train_samples = 10402
nb_validation_samples = 2159
img_height, img_width = 224, 224
input_shape = (img_width, img_height, 3)
