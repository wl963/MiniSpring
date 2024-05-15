package com.mini.context;

import java.util.EventObject;

public class ApplicationEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */

    public static final long serialVersionUID = 1L;
    public ApplicationEvent(Object source) {
        super(source);
    }
}
