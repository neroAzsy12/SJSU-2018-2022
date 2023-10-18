import re
import math

# works
class SimpleSubstitution:
    def __init__(self, key):
        self.plainAlphabet = 'abcdefghijklmnopqrstuvwxyz'
        self.cipherAlphabet = key.lower()

        self.pc = {}    # plain alpha to cipher alpha
        self.cp = {}    # cipher alpha to plain alpha

        for i in range(len(self.plainAlphabet)):
            self.pc[self.plainAlphabet[i]] = self.cipherAlphabet[i]
            self.cp[self.cipherAlphabet[i]] = self.plainAlphabet[i] 

    def encrypt(self, text):
        text = text.lower()
        plainText = re.sub(r"[^a-z]+", "", text)

        cipherText = ''
        for i in plainText:
            cipherText += self.pc[i]
        
        return cipherText

    def decrypt(self, text):
        text = text.lower()
        cipherText = re.sub(r"[^a-z]+", "", text)

        plainText = ''
        for i in cipherText:
            plainText += self.cp[i]
        
        return plainText

# works
class ColumnarTransposition:
    def __init__(self, key, padding):
        self.key = key.lower()
        self.key = re.sub(r"[^a-z]+", "", self.key)
        self.padding = padding.lower()

    def encrypt(self, text):
        text = text.lower()
        plainText = re.sub(r"[^a-z]+", "", text)

        text_len = len(plainText)
        text_lst = list(plainText)

        # calc the column of the matrix
        column = len(self.key)

        # calc the max row of the matrix
        row = int(math.ceil(text_len / column))

        # add the padding char in empty cells of the matrix
        fill_padding = int((row * column) - text_len)
        text_lst.extend(self.padding * fill_padding)

        # create Matrix and insert message and padding chars by row
        matrix = [text_lst[i : i + column] for i in range(0, len(text_lst), column)]

        # read matrix by column using key
        cipherText = ''
        keyPointer = 0
        key_lst = sorted(list(self.key))
        
        tmp_key = self.key

        #for _ in range(column):
        #    current_index = self.key.index(key_lst[keyPointer])
        #    cipherText += ''.join([row[current_index] for row in matrix])
        #    keyPointer += 1

        for _ in range(column):
            current_index = tmp_key.index(key_lst[keyPointer])
            cipherText += ''.join([row[current_index] for row in matrix])
            keyPointer += 1

            tmp_key = tmp_key[:current_index] + '1' + tmp_key[current_index + 1:]

        return cipherText
    
    def decrypt(self, text):
        text = text.lower()
        cipherText = re.sub(r"[^a-z]+", "", text)

        cipherText_len = len(cipherText)
        cipherText_lst = list(cipherText)

        # calc the column of the matrix
        column = len(self.key)

        # calc the max row of the matrix
        row = int(math.ceil(cipherText_len / column))

        matrix = []
        for i in range(row):
            matrix += [[''] * column]

        keyPointer = 0
        cipherTextPointer = 0
        key_lst = sorted(list(self.key))

        tmp_key = self.key

        #print(tmp_key)

        for i in range(column):
            #current_index = self.key.index(key_lst[keyPointer])
            current_index = tmp_key.index(key_lst[keyPointer])

            for j in range(row):
                matrix[j][current_index] = cipherText_lst[cipherTextPointer]
                cipherTextPointer += 1
            
            keyPointer += 1
            tmp_key = tmp_key[:current_index] + '-' + tmp_key[current_index + 1:]
            #tmp_key = tmp_key[:current_index] + str(keyPointer) + tmp_key[current_index + 1:]

        #print(tmp_key)

        plainText = ''

        for i in range(row):
            plainText += ''.join(matrix[i])

        return plainText

# works
class Vigenere:
    def __init__(self, key):
        self.key = key.lower()
        self.key = re.sub(r"[^a-z]+", "", self.key)
    
    def encrypt(self, text):
        text = text.lower()
        plainText = re.sub(r"[^a-z]+", "", text)

        cipherText = ''
        key_len = len(self.key)

        for i in range(len(plainText)):
            p_char = ord(plainText[i]) - 97
            k_char = ord(self.key[i % key_len]) - 97

            shift = (p_char + k_char) % 26

            cipherText += chr(97 + shift)
        
        return cipherText

    def decrypt(self, text):
        text = text.lower()
        cipherText = re.sub(r"[^a-z]+", "", text)

        plainText = ''
        key_len = len(self.key)

        for i in range(len(cipherText)):
            c_char = ord(cipherText[i]) - 97
            k_char = ord(self.key[i % key_len]) - 97

            shift = (c_char - k_char + 26) % 26
            
            plainText += chr(97 + shift)
        
        return plainText

class PlayFair:
    def __init__(self, key):
        self.key = key.lower()
        self.key = re.sub(r"[^a-z]+", "", self.key)
        self.matrix = self.generateMatrix(self.key)

    def generateMatrix(self, key):
        result = []
        for letter in key:
            if letter not in result:
                if letter == 'j':
                    result.append('i')
                else:
                    result.append(letter)
        
        
        for i in range(97, 123):
            if i == 106:
                continue 
            elif chr(i) not in result:
                result.append(chr(i))

        index = 0
        matrix = []

        for i in range(0, 5):
            matrix.append([])
            for j in range(0, 5):
                matrix[i].append(result[index])
                index += 1
        
        return matrix

    def locate_index(self, letter):
        for i, x in enumerate(self.matrix):
            if letter in x:
                return [i, x.index(letter)]


    def encrypt(self, text):
        text = text.lower().replace('j', 'i')
        plainText = re.sub(r"[^a-z]+", "", text)

        for i in range(0, len(plainText) + 1, 2):
            if i < len(plainText) - 1:
                if plainText[i] == plainText[i + 1]:
                    plainText = plainText[:i + 1] + 'x' + plainText[i + 1:]

        if len(plainText) % 2 != 0:
            plainText += 'x'

        index = 0
        cipherText = ''

        while(index < len(plainText)):
            first = self.locate_index(plainText[index])
            second = self.locate_index(plainText[index + 1])

            if first[0] == second[0]:
                cipherText += self.matrix[first[0]][(first[1] + 1) % 5]
                cipherText += self.matrix[second[0]][(second[1] + 1) % 5]
            
            elif first[1] == second[1]:
                cipherText += self.matrix[(first[0] + 1) % 5][first[1]]
                cipherText += self.matrix[(second[0] + 1) % 5][second[1]]
            
            else:
                cipherText += self.matrix[first[0]][second[1]]
                cipherText += self.matrix[second[0]][first[1]]
            index += 2

        return cipherText

    def decrypt(self, text):
        text = text.lower().replace('j', 'i')
        cipherText = re.sub(r"[^a-z]+", "", text)

        index = 0
        plainText = ''

        while(index < len(cipherText)):
            first = self.locate_index(cipherText[index])
            second = self.locate_index(cipherText[index + 1])

            if first[0] == second[0]:
                plainText += self.matrix[first[0]][(first[1] - 1) % 5]
                plainText += self.matrix[second[0]][(second[1] - 1) % 5]
            
            elif first[1] == second[1]:
                plainText += self.matrix[(first[0] - 1) % 5][first[1]]
                plainText += self.matrix[(second[0] - 1) % 5][second[1]]
            
            else:
                plainText += self.matrix[first[0]][second[1]]
                plainText += self.matrix[second[0]][first[1]]
            index += 2

        return plainText

class FourSquare:
    def __init__(self, key1, key2):
        self.alphabet = 'abcdefghiklmnopqrstuvwxyz'

        self.key1 = key1.lower()
        self.key1 = re.sub(r"[^a-z]+", "", self.key1)

        self.key2 = key2.lower()
        self.key2 = re.sub(r"[^a-z]+", "", self.key2)

        self.alpha_table1 = [self.alphabet[:5], self.alphabet[5:10], self.alphabet[10:15], self.alphabet[15:20], self.alphabet[20:25]]
        self.alpha_table2 = [self.alphabet[:5], self.alphabet[5:10], self.alphabet[10:15], self.alphabet[15:20], self.alphabet[20:25]]

        self.key_table1 = self.generateMatrix(self.key1)
        self.key_table2 = self.generateMatrix(self.key2)
    
    def generateMatrix(self, key):
        result = []
        for letter in key:
            if letter not in result:
                if letter == 'j':
                    result.append('i')
                else:
                    result.append(letter)
        
        
        for i in range(97, 123):
            if i == 106:
                continue 
            elif chr(i) not in result:
                result.append(chr(i))

        index = 0
        matrix = []

        for i in range(0, 5):
            matrix.append([])
            for j in range(0, 5):
                matrix[i].append(result[index])
                index += 1
        
        return matrix
    
    def locate_index(self, letter, matrix):
        for i, x in enumerate(matrix):
            if letter in x:
                return [i, x.index(letter)]
    
    def encrypt(self, text):
        text = text.lower().replace('j', 'i')
        plainText = re.sub(r"[^a-z]+", "", text)

        if len(plainText) % 2 != 0:
            plainText += 'x'
        
        index = 0
        cipherText = ''

        while(index < len(plainText)):
            first = self.locate_index(plainText[index], self.alpha_table1)
            second = self.locate_index(plainText[index + 1], self.alpha_table2)

            cipherText += self.key_table1[first[0]][second[1]]
            cipherText += self.key_table2[second[0]][first[1]]

            index += 2
        
        return cipherText

    def decrypt(self, text):
        text = text.lower().replace('j', 'i')
        cipherText = re.sub(r"[^a-z]+", "", text)
        
        index = 0
        plainText = ''

        while index < len(cipherText):
            first = self.locate_index(cipherText[index], self.key_table1)
            second = self.locate_index(cipherText[index + 1], self.key_table2)

            plainText += self.alpha_table1[first[0]][second[1]]
            plainText += self.alpha_table2[second[0]][first[1]]

            index += 2
        
        return plainText