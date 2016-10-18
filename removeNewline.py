import sys



'''
Author: Rajmani Arya
Date: 18th Oct 2016

example sequence input:

>gi|568815580|ref|NC_000018.10| Homo sapiens chromosome 18, GRCh38.p7 Primary Assembly
>NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN
>NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN
>NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNCGACTCATGACTACGCAATGACGATA

expected output:
CGACTCATGACTACGCAATGACGATA
'''
if len(sys.argv) != 3:
    print 'Usgae: python removeNewline.py <input_file> <output_file>'
    sys.exit(1)

input_file = sys.argv[1]
output_file = sys.argv[2]

print input_file, output_file

with open(input_file, 'r') as f:
    out = open(output_file, 'w')
    for line in f:
        if line[0] == '>' or line[-1] == 'N':
            continue;
        if line[0] == 'N':
            l = len(line)
            for i in range(l):
                if line[i] != 'N': # Real ACGT sequence starts here
                    line = line[i:]
                    break
        out.write(line[:-1])
