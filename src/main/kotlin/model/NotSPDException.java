package model;

public class NotSPDException extends RuntimeException {
    public NotSPDException() {
        super("This matrix is not Singular, Positive Definite or both.");
    }
    public NotSPDException(String message) {
        super(message);
    }
}
