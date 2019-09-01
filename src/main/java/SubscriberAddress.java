import java.util.Objects;

public class SubscriberAddress {

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
}
