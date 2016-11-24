#!/usr/bin/env python

arr = [0] * 11

with open('out_p_2.txt') as f:
    for line in f:
        tokens = line.split(",")
        if len(tokens[3]) <= 11:
            arr[len(tokens[3])-1] += 1

print arr
