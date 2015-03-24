#!/bin/bash
echo "Compile generator..."
make GraphGenerator 

if [ $? -eq 0 ]; then
  echo -e "Run generator...\n"
  java GraphGenerator graph.txt 10 
else
  echo -e "Error in compilation\n"
fi

echo "Compile program..."
make VoronoiDemo

if [ $? -eq 0 ]; then
  echo -e "Run...\n"
  java VoronoiDemo graph.txt
else
  echo -e "Error in compilation\n"
fi
