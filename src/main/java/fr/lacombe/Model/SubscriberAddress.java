package fr.lacombe.Model;

import java.io.Serializable;
import java.util.Objects;

public class SubscriberAddress implements Serializable {

    private Country country;
    private int postalCode;
    private String streetName;
    private boolean isAddressActive;
    private String city;

    public SubscriberAddress(Country country, String city, int postalCode, String streetName, boolean isAddressActive) {
        this.city = city;
        this.country = country;
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
                country == that.country &&
                streetName.equals(that.streetName) &&
                city.equals(that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, postalCode, streetName, isAddressActive, city);
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
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
