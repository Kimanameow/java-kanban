package server;

import java.io.IOException;

public class RequestMethodException extends IOException {
    public RequestMethodException(String message) {
        super(message);
    }
}
