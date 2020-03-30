"""
@author : Hyunwoong
@when : 2019-12-08
@homepage : https://github.com/gusdnd852
"""
import matplotlib.pyplot as plt
import re


def read(name):
    f = open(name, 'r')
    file = f.read()
    file = re.sub('\\[', '', file)
    file = re.sub('\\]', '', file)
    f.close()

    return [float(i) * 100.0 for i in file.split(',')]


val_acc = read('./report/val_acc.txt')
tra_acc = read('./report/acc.txt')

plt.plot(tra_acc, 'r', label='train')
plt.plot(val_acc, 'b', label='validation')

plt.xlabel('epoch')
plt.ylabel('accuracy(%)')
plt.title('training result')
plt.grid(True, which='both', axis='both')
plt.legend(loc='lower right')
plt.show()
