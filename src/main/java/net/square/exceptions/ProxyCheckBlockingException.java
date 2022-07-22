package net.square.exceptions;

public class ProxyCheckBlockingException extends InterruptedException {

    public ProxyCheckBlockingException(String message) {
        super(message);
    }
}
