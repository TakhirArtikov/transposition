#!/bin/bash

javac Test.java

if [ $? -eq 0 ]; then
    java Test
else
    echo "Compilation failed."
fi