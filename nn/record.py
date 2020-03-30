"""
@author : Hyunwoong
@when : 2019-11-27
@homepage : https://github.com/gusdnd852
"""


def record(hist, name):
    f = open('result/' + name + '.txt', 'w')
    f.write(str(hist.history[name]))
    f.close()
