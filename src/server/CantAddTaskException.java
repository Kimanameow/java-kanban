package server;

public class CantAddTaskException extends RuntimeException {
    public CantAddTaskException(String message) {
        super(message);
    }
}
