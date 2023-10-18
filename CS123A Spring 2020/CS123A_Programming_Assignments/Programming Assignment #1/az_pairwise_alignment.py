# programming assingment 1
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

# with localdx and globaldx, they both had a score of 1810
local_alignment = pairwise2.align.localdx(data1, data2, blosum)
print(format_alignment(*local_alignment[0]))

print()

global_alignment = pairwise2.align.globaldx(data1, data2, blosum)
print(format_alignment(*global_alignment[0]))


local_alignment = pairwise2.align.localds(data1, data2, blosum, -4, -4)
print(format_alignment(*local_alignment[0]))

print()

global_alignment = pairwise2.align.globalds(data1, data2, blosum, -4, -4)
print(format_alignment(*global_alignment[0]))

#for a in pairwise2.align.globalds("HEAGAWGHEE", "PAWHEAE", blosum, -4, -4):
#    print(format_alignment(*a))
