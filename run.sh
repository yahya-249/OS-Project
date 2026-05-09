#!/bin/bash
echo "Compiling..."
mkdir -p out
javac -d out src/*.java
if [ $? -ne 0 ]; then
    echo "Compilation FAILED. Make sure Java JDK is installed."
    exit 1
fi
echo "Running..."
java -cp out Main
