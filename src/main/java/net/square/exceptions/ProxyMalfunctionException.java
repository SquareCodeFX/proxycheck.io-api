package net.square.exceptions;

public class ProxyMalfunctionException extends InterruptedException {

    public ProxyMalfunctionException(String message) {
        super(message);
    }
}
