#!/bin/bash
echo "Compile generator..."
make GraphGenerator 

if [ $? -eq 0 ]; then
  echo -e "Run generator...\n"
  java GraphGenerator graph.txt 50 
else
  echo -e "Error in compilation\n"
fi

echo "Compile program..."
make Demo 

if [ $? -eq 0 ]; then
  echo -e "Run...\n"
  java Demo graph.txt
else
  echo -e "Error in compilation\n"
fi
