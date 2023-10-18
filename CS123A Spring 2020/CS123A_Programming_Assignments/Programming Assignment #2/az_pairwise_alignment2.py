# programming assingment 2
from Bio import pairwise2
from Bio.pairwise2 import format_alignment
from Bio.SubsMat import MatrixInfo as matlist

blosum = matlist.blosum62

# opens seq1
with open('seq1.txt', 'r') as file1:
    data1 = file1.read().replace('\n', '')

# opens seq2
with open('seq2.txt', 'r') as file2:
    data2 = file2.read().replace('\n', '')

test1 = pairwise2.align.globalds(data1, data2, blosum,-4, -4, penalize_extend_when_opening = True, penalize_end_gaps = (False, False))
print(format_alignment(*test1[0]))

test2 = pairwise2.align.globalds(data1, data2, blosum, -4, -4, penalize_extend_when_opening = True, penalize_end_gaps = (True, True))
print(format_alignment(*test2[0]))

test3 = pairwise2.align.globalds(data1, data2, blosum, -4, -4, penalize_extend_when_opening = True, penalize_end_gaps = (True, False))
print(format_alignment(*test3[0]))

test4 = pairwise2.align.globalds(data1, data2, blosum, -4, -4, penalize_extend_when_opening = True, penalize_end_gaps = (False, True))
print(format_alignment(*test4[0]))

test5 = pairwise2.align.globalds(data1, data2, blosum,-4, -4, penalize_extend_when_opening = False, penalize_end_gaps = (False, False))
print(format_alignment(*test5[0]))

test6 = pairwise2.align.globalds(data1, data2, blosum, -4, -4, penalize_extend_when_opening = False, penalize_end_gaps = (True, True))
print(format_alignment(*test6[0]))

test7 = pairwise2.align.globalds(data1, data2, blosum, -4, -4, penalize_extend_when_opening = False, penalize_end_gaps = (True, False))
print(format_alignment(*test7[0]))

test8 = pairwise2.align.globalds(data1, data2, blosum, -4, -4, penalize_extend_when_opening = False, penalize_end_gaps = (False, True))
print(format_alignment(*test8[0]))
