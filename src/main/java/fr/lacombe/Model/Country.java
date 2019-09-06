package fr.lacombe.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public enum Country implements Serializable {
    @JsonProperty("country")
    FRANCE,
    ITALIA;

    public boolean isFrance(Country country) {
        return country.equals(FRANCE);
    }
}
