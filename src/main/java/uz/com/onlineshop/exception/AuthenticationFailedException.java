package uz.com.onlineshop.exception;

public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException(String message) {super(message);
    }
}
