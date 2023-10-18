import math
import random
import numpy as np
import functools

class HMM:
    def __init__(self, states, symbols):
        self.states = states    # list of states ['A', 'B']
        self.symbols = symbols  # list of symbols ['A', 'B', 'C', 'D', 'E']
        
        self.PI = {}            # PI is (1 x numStates)
        self.A = {}             # A is (numStates x numStates)
        self.B = {}             # B is (numStates x numSymbols)
        
        PI_sum = 0
        A_sum = 0
        B_sum = 0
        
        for i in states:
            initialize_pi = 1.0/len(states) + 0.01 * random.uniform(-1, -1) * (1.0 / len(states))
            PI_sum += initialize_pi
            
            self.PI[i] = initialize_pi
            self.A[i] = {}
            
            for j in states:
                initialize_A = 1.0 / len(states) + 0.01 * random.uniform(-1, 1) * (1.0 / len(states))
                A_sum += initialize_A
                
                self.A[i][j] = initialize_A
            
            for j in states:
                self.A[i][j] /= A_sum
            
            A_sum = 0
            
            self.B[i] = {}
            for k in symbols:
                initialize_B = 1.0 / len(symbols) + 0.01 * random.uniform(-1, 1) * (1.0 / len(states))
                B_sum += initialize_B
            
                self.B[i][k] = initialize_B
            
            for k in symbols:
                self.B[i][k] /= B_sum
            
            B_sum = 0
        
        for i in states:
            self.PI[i] /= PI_sum
    
    def forward(self, observation): 
        T = len(observation)
        alpha = [{} for t in range(T)]
        c = [0 for x in range(T)]
        
        # compute alpha[0][i]
        c[0] = 0
        for i in self.states: 
            alpha[0][i] = self.PI[i] * self.B[i][observation[0]]
            c[0] = c[0] + alpha[0][i]
        
        # scale the alpha[0][i]
        c[0] = 1 / c[0]
        for i in self.states:
            alpha[0][i] = c[0] * alpha[0][i]
        
        # compute alpha[t][i]
        for t in range(1, T):
            c[t] = 0
            for i in self.states:
                alpha[t][i] = 0
                for j in self.states:
                    alpha[t][i] = alpha[t][i] + (alpha[t - 1][j] * self.A[j][i])
                alpha[t][i] = alpha[t][i] * self.B[i][observation[t]]
                c[t] = c[t] + alpha[t][i]
            
            # scale alpha[t][i]
            c[t] = 1 / c[t]
            for i in self.states: 
                alpha[t][i] = c[t] * alpha[t][i]
        
        return (alpha, c)
    
    def backward(self, observation, c): 
        T = len(observation)
        beta = [{} for t in range(T)]
        
        for i in self.states:
            beta[T - 1][i] = c[T - 1]
        
        # beta pass
        for t in range(T - 2, -1, -1):
            for i in self.states:
                beta[t][i] = 0
                for j in self.states:
                    beta[t][i] = beta[t][i] + (self.A[i][j] * self.B[j][observation[t + 1]] * beta[t + 1][j])
                beta[t][i] = c[t] * beta[t][i]
        
        return beta
    
    def gammas(self, observation):
        T = len(observation)
        gamma = [{} for t in range(T)]
        di_gamma = [{} for t in range(T - 1)]
        
        for t in range(T - 1):
            for i in self.states:
                di_gamma[t][i] = {}
        
        alpha, c = self.forward(observation)
        beta = self.backward(observation, c)
        
        for t in range(T - 1):
            denom = 0
            for i in self.states:
                for j in self.states:
                    denom = denom + (alpha[t][i] * self.A[i][j] * self.B[j][observation[t + 1]] * beta[t + 1][j])
            
            for i in self.states:
                gamma[t][i] = 0
                for j in self.states:
                    di_gamma[t][i][j] = (alpha[t][i] * self.A[i][j] * self.B[j][observation[t + 1]] * beta[t + 1][j]) / denom
                    gamma[t][i] = gamma[t][i] + di_gamma[t][i][j]
        
        # special case
        denom = 0
        for i in self.states:
            denom = denom + alpha[T - 1][i]
        
        for i in self.states:
            gamma[T - 1][i] = alpha[T - 1][i] / denom
        
        return (gamma, di_gamma, c)
    
    def reestimate(self, observation):
        T = len(observation)
        gamma, di_gamma, c = self.gammas(observation)
        
        # re-estimate PI
        for i in self.states:
            self.PI[i] = gamma[0][i]
        
        # re-estimate A
        for i in self.states:
            for j in self.states:
                numer = 0
                denom = 0
                
                for t in range(T - 1):
                    numer = numer + di_gamma[t][i][j]
                    denom = denom + gamma[t][i]
                self.A[i][j] = numer / denom
        
        # re-estimate B
        for i in self.states:
            for j in self.symbols:
                numer = 0
                denom = 0
                for t in range(T - 1):
                    if observation[t] == j:
                        numer = numer + gamma[t][i]
                    denom = denom + gamma[t][i]
                self.B[i][j] = numer / denom
        
        return c
    
    def fit(self, maxIterations, observations):
        oldLogProb = float('-inf')
        obs =  observations
        
        for i in range(maxIterations):
            np.random.shuffle(obs)
            
            observation = functools.reduce(lambda x, y: x + y, obs)
            c = self.reestimate(observation)
    
            self.logProb = 0
            
            for scale in c:
                self.logProb = self.logProb + math.log10(scale)
            
            self.logProb = -self.logProb
            
            print("Score: " + str(self.logProb))
            if oldLogProb < self.logProb:
                oldLogProb = self.logProb
            
            else:
                break
            
    def score(self, observation):
        alpha, c = self.forward(observation)
        logProb = 0
        
        for scale in c:
            logProb = logProb + math.log10(c)
        
        return -logProb
    
    def test(self, observations):
        results = []
        for observation in observations:
            result.append(self.score(observation))
            
        return results