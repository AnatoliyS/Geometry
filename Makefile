# Compiling graph generator
GraphGenerator.class: GraphGenerator.java
	javac -g GraphGenerator.java
GraphGenerator: GraphGenerator.class

# Compiling DCEL classes
DCEL_DIR = DCEL/
DCEL_SRC = $(DCEL_DIR)HalfEdge.java $(DCEL_DIR)Vertex.java $(DCEL_DIR)Face.java
DCEL:
	javac -g $(DCEL_SRC) 

UTILS_SRC = Point.java PointComparator.java 
Utils:
	javac -g $(UTILS_SRC) 

# Compiling Voronoi Demonstarion
VoronoiDemo.class: DCEL Utils VoronoiDemo.java VoronoiDiagram.java 
	javac -g VoronoiDemo.java VoronoiDiagram.java 
VoronoiDemo: VoronoiDemo.class
