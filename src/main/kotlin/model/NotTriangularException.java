package model;

public class NotTriangularException extends RuntimeException {

    public NotTriangularException(boolean isNotTriu) {
        super(
                isNotTriu
                        ?
                        "The provided matrix is not an upper triangular matrix"
                        :
                        "The provided matrix is not a lower triangular matrix"
        );
    }

}
