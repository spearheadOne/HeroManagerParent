package org.abondar.industrial.heromanager.exception;

public class HeroNotFoundException extends RuntimeException {

    public HeroNotFoundException() {
        super("Hero not found by specified ID or parameter");
    }
}
