from hmm import HMM
import os

symbols = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z']
states = ['a', 'b']

symbols_HMM = symbols[0:21]

winwebsec_training = []
with open('Data/winwebsec/dataset/winwebsec_training.txt', 'r') as file:
    lines = file.read().splitlines()
    for line in lines:
        if line != '':
            winwebsec_training.append(line)

print(len(winwebsec_training))

winwebsec_hmm = HMM(states, symbols_HMM)
winwebsec_hmm.fit(100, winwebsec_training)