package exceptions;

public class UserDoNotContainThatCard extends Exception{
    public UserDoNotContainThatCard(String message) {
        super(message);
    }
}
