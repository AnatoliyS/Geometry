# Compiling graph generator
GraphGenerator.class: GraphGenerator.java
	javac -g GraphGenerator.java
GraphGenerator: GraphGenerator.class

# Compiling DCEL classes
DCEL_DIR = DCEL/
DCEL_SRC = $(DCEL_DIR)HalfEdge.java $(DCEL_DIR)Vertex.java $(DCEL_DIR)Face.java
DCEL_CLASSES = $(DCEL_SRC:.java=.class)
$(DCEL_CLASSES): $(DCEL_SRC)
	javac -g $(DCEL_SRC) 
DCEL: $(DCEL_CLASSES)

UTILS_DIR = Utils/
UTILS_EXCEPTIONS_DIR= Utils/Exceptions/
UTILS_SRC = $(UTILS_DIR)Point.java \
					 	$(UTILS_DIR)PointComparator.java \
					 	$(UTILS_DIR)Line.java \
						$(UTILS_DIR)Constants.java \
						$(UTILS_DIR)Pair.java \
						$(UTILS_DIR)Debug.java \
						$(UTILS_DIR)Vector.java \
						$(UTILS_DIR)Geometry.java \
						$(UTILS_DIR)Segment.java \
						$(UTILS_EXCEPTIONS_DIR)NoIntersectionException.java \
						$(UTILS_EXCEPTIONS_DIR)NoDataException.java \
						$(UTILS_EXCEPTIONS_DIR)AlgorithmDependenciesException.java \
						$(UTILS_EXCEPTIONS_DIR)UnknownAlgorithmException.java


UTILS_CLASSES = $(UTILS_SRC:.java=.class)
$(UTILS_CLASSES): $(UTILS_SRC)
	javac -g $(UTILS_SRC) 
UTILS: $(UTILS_CLASSES)

# Compiling ConvexHull logic
CONHULL_SRC = ConvexHull.java ConvexHullAlgo.java

# Compiling algorithms
ALGS_SRC = AlgorithmName.java Algorithm.java AlgorithmsContainer.java \
	AlgorithmsContainerBuilder.java $(CONHULL_SRC)

# Compiling Divide and Conquer tree
DAC: $(ALGS_SRC) DACNode.java DACTree.java
	javac -g $(ALGS_SRC)

# Compiling Voronoi Demonstarion
VoronoiDemo.class: DCEL UTILS DAC VoronoiDemo.java VoronoiDiagram.java VoronoiBuilder.java 
	javac -g VoronoiDemo.java VoronoiDiagram.java VoronoiBuilder.java
VoronoiDemo: VoronoiDemo.class

clean:
	$(RM) *.class
