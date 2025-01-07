package exceptions;

public class NoCorrectCardNumberException extends Exception{
    public NoCorrectCardNumberException(String message) {
        super(message);
    }
}
