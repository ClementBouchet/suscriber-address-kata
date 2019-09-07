package fr.lacombe.Model;

import java.io.Serializable;
import java.util.Objects;

public class SubscriberAddress implements Serializable {

    private CountryEnum countryEnum;
    private int postalCode;
    private String streetName;
    private boolean isAddressActive;
    private String city;

    public SubscriberAddress(CountryEnum countryEnum, String city, int postalCode, String streetName, boolean isAddressActive) {
        this.city = city;
        this.countryEnum = countryEnum;
        this.postalCode = postalCode;
        this.streetName = streetName;
        this.isAddressActive = isAddressActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriberAddress that = (SubscriberAddress) o;
        return postalCode == that.postalCode &&
                isAddressActive == that.isAddressActive &&
                countryEnum == that.countryEnum &&
                streetName.equals(that.streetName) &&
                city.equals(that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryEnum, postalCode, streetName, isAddressActive, city);
    }

    public CountryEnum getCountryEnum() {
        return countryEnum;
    }

    public void setCountryEnum(CountryEnum countryEnum) {
        this.countryEnum = countryEnum;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public boolean isAddressActive() {
        return isAddressActive;
    }

    public void setAddressActive(boolean addressActive) {
        isAddressActive = addressActive;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
