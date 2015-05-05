# Compiling graph generator
GraphGenerator.class: GraphGenerator.java
	javac -g GraphGenerator.java
GraphGenerator: GraphGenerator.class

# Compiling DCEL classes
DCEL_DIR = DCEL/
DCEL_SRC = $(DCEL_DIR)HalfEdge.java $(DCEL_DIR)Vertex.java $(DCEL_DIR)Face.java $(DCEL_DIR)HalfEdgeBuilder.java \
					 $(DCEL_DIR)DCEL.java
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
						$(UTILS_DIR)IntersectionType.java \
						$(UTILS_DIR)IntersectionResult.java \
						$(UTILS_DIR)Geometry.java \
						$(UTILS_DIR)Segment.java \
						$(UTILS_DIR)DrawHelper.java \
						$(UTILS_EXCEPTIONS_DIR)NoIntersectionException.java \
						$(UTILS_EXCEPTIONS_DIR)NoDataException.java \
						$(UTILS_EXCEPTIONS_DIR)AlgorithmDependenciesException.java \
						$(UTILS_EXCEPTIONS_DIR)AlgorithmRuntimeException.java \
						$(UTILS_EXCEPTIONS_DIR)UnknownAlgorithmException.java \
						$(UTILS_EXCEPTIONS_DIR)HalfEdgeIsNotValidException.java \
						$(UTILS_EXCEPTIONS_DIR)FaceTraversingException.java \
						$(UTILS_EXCEPTIONS_DIR)VoronoiBuildingException.java \


UTILS_CLASSES = $(UTILS_SRC:.java=.class)
$(UTILS_CLASSES): $(UTILS_SRC)
	javac -g $(UTILS_SRC) 
UTILS: $(UTILS_CLASSES)

# Compiling ConvexHull logic
CONHULL_SRC = ConvexHull.java ConvexHullAlgo.java

# Compiling MinimumAreaPolygon logic
MIN_AREA_POL_SCR = MinimumAreaPolygon.java MinimumAreaPolygonAlgo.java

# Compiling Voronoi logic
VORONOI_SRC = VoronoiDiagram.java

# Compiling algorithms
ALGS_SRC = AlgorithmName.java Algorithm.java AlgorithmsContainer.java VisualData.java \
	$(CONHULL_SRC) $(MIN_AREA_POL_SCR) $(VORONOI_SRC)

# Compiling Divide and Conquer tree
DAC_SRC = $(ALGS_SRC) DACNode.java DACTree.java

DAC_CLASSES = $(DAC_SRC:.java=.class)
$(DAC_CLASSES): $(DAC_SRC)
	javac -g $(DAC_SRC) 
DAC: $(DAC_CLASSES)

# Compiling Demonstarion
Demo.class: DCEL UTILS DAC Demo.java   
	javac -g Demo.java
Demo: Demo.class

clean:
	$(RM) *.class
