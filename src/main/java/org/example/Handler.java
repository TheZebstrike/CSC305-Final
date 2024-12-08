package org.example;

public interface Handler {
    String handle(SvgShape shape);
    void setNext(Handler next);
}

