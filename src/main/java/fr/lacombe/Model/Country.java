package fr.lacombe.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static fr.lacombe.Model.CountryEnum.FRANCE;

public class Country {


    private CountryEnum countryEnum;

    @JsonCreator
    public Country(@JsonProperty("country") CountryEnum countryEnum) {
        this.countryEnum = countryEnum;
    }

    public boolean isFrance() {
        return countryEnum.equals(FRANCE);
    }

}
