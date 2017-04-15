# compile

java *.java


# set the error tolerance in Application.java file.
# currently it is set to 2. error = 2.

# Example (Find Approx Repeats using direct approx )

# large file
$ java Application <data.fasta> <errors>

# example 2 :
# Repeat of length 18 with 2 errors:
0  CGGGCTGC- 7
8  CGGCCTGCA 16
17 C 17

$ java Application data_sample_approx2.fasta 2

[start: 1, end: 4, total length: 16, motif length:4]
[start: 10, end: 13, total length: 8, motif length:4]
[start: 5, end: 8, total length: 16, motif length:4]
[start: 0, end: 7, total length: 18, motif length:8]
[start: 9, end: 12, total length: 10, motif length:4]
[start: 2, end: 5, total length: 15, motif length:4]
[start: 6, end: 9, total length: 15, motif length:4]
[start: 1, end: 8, total length: 17, motif length:8]
[start: 2, end: 5, total length: 16, motif length:4]
[start: 6, end: 9, total length: 16, motif length:4]
[start: 1, end: 8, total length: 18, motif length:8]
[start: 10, end: 13, total length: 10, motif length:4]
[start: 3, end: 6, total length: 15, motif length:4]
[start: 7, end: 10, total length: 15, motif length:4]
[start: 0, end: 3, total length: 9, motif length:4]
[start: 3, end: 6, total length: 16, motif length:4]
[start: 7, end: 10, total length: 16, motif length:4]
[start: 4, end: 7, total length: 15, motif length:4]
[start: 8, end: 11, total length: 15, motif length:4]
[start: 0, end: 3, total length: 16, motif length:4]
[start: 1, end: 4, total length: 9, motif length:4]
[start: 4, end: 7, total length: 16, motif length:4]
[start: 8, end: 11, total length: 10, motif length:4]
[start: 5, end: 8, total length: 15, motif length:4]
[start: 0, end: 7, total length: 17, motif length:8]
[start: 9, end: 12, total length: 15, motif length:4]

#--------

# example 3

# Repeat of length 14 with 2 errors:

0 CA-CAGG 5
6 CACCGGG 13
14 C 14

$ java Application data_sample_approx4.fasta 2
[start: 2, end: 5, total length: 10, motif length:4]
[start: 1, end: 6, total length: 13, motif length:6]
[start: 0, end: 5, total length: 14, motif length:6]
[start: 1, end: 6, total length: 14, motif length:6]
[start: 0, end: 3, total length: 10, motif length:4]
[start: 1, end: 4, total length: 10, motif length:4]
[start: 0, end: 5, total length: 13, motif length:6]


#-------

# example 4

<!-- set error = 4 in Applicaiton.java -->
Repeat of length 21 with 4 errors.
Copy 1: CTC--GAG
copy 2: CTCCTGAC
copy 3: CTCGTGA


$ javac Application.java
$ java Application data_sample_approx1.fasta 4
[start: 5, end: 10, total length: 16, motif length:6]
[start: 0, end: 5, total length: 18, motif length:6]
[start: 7, end: 12, total length: 16, motif length:6]
[start: 0, end: 7, total length: 21, motif length:8]
[start: 2, end: 7, total length: 18, motif length:6]
[start: 2, end: 9, total length: 21, motif length:8]
[start: 4, end: 9, total length: 18, motif length:6]
[start: 4, end: 11, total length: 21, motif length:8]
[start: 6, end: 11, total length: 16, motif length:6]
[start: 1, end: 6, total length: 18, motif length:6]
[start: 8, end: 13, total length: 16, motif length:6]
[start: 1, end: 8, total length: 21, motif length:8]
[start: 3, end: 8, total length: 18, motif length:6]
[start: 3, end: 10, total length: 21, motif length:8]
[start: 5, end: 10, total length: 18, motif length:6]

#----------
