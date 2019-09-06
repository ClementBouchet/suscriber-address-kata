package fr.lacombe.Model;

import java.io.Serializable;

public class SubscriberId implements Serializable {
    private String id;

    public SubscriberId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
