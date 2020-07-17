package es.ull.pcg.hpc.benchmark.exceptions;

/**
 * Exception raised when trying to merge incompatible {@link es.ull.pcg.hpc.benchmark.Results}.
 */
public class InvalidResultsMergeException extends RuntimeException {
    /**
     * Create a new {@link InvalidResultsMergeException} object, with a reason description.
     * @param message The reason of the exception.
     */
    public InvalidResultsMergeException (String message) {
        super(message);
    }
}
