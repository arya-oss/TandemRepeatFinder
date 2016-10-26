'''
   Requires CSV file (freq.csv)
   format: period,frequency,length
'''

import numpy as np
import matplotlib.pyplot as plt

data = np.genfromtxt('freq.csv', delimiter=',', dtype=np.int32)

period = [i[0] for i in data[1:]]
freq = [i[1] for i in data[1:]]
length = [i[2] for i in data[1:]]

# print np.max(length)
# print np.max(period)
# print np.max(freq)

plt.scatter(period, freq)
plt.xlabel('Period Length')
plt.ylabel('Frequency')
plt.title('Tandem repeat occurence Period vs. Frequency')
plt.savefig('per_freq.png')

plt.scatter(period, length)
plt.xlabel('Period Length')
plt.ylabel('Average Length')
plt.title('Tandem repeat occurence Period vs. Avg. Length')
plt.savefig('per_len.png')

