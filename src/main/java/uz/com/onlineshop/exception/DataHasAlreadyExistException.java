package uz.com.onlineshop.exception;

public class DataHasAlreadyExistException extends RuntimeException {
    public DataHasAlreadyExistException(String message) {
        super(message);
    }
}
