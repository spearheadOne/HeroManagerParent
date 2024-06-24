package org.abondar.industrial.heromanager.exception;

public class PropertyNotFoundException extends RuntimeException {

    public PropertyNotFoundException() {
        super("Property with supplied userId doesn't exist");
    }
}
