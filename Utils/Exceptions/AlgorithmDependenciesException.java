package Utils.Exceptions;

import java.lang.Exception;

public class AlgorithmDependenciesException extends Exception {
    public AlgorithmDependenciesException(String errorMessage) {
        super(errorMessage);
    }
}