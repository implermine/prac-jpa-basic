package org.example.embed;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@Embeddable
@Setter
@ToString
@NoArgsConstructor
public class Address{
    private String city;
    private String street;
    private String zipCode;

    public Address(String city, String street, String zipCode) {
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Address)) return false;
        final Address other = (Address) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$city = this.getCity();
        final Object other$city = other.getCity();
        if (!Objects.equals(this$city, other$city)) return false;
        final Object this$street = this.getStreet();
        final Object other$street = other.getStreet();
        if (!Objects.equals(this$street, other$street)) return false;
        final Object this$zipCode = this.getZipCode();
        final Object other$zipCode = other.getZipCode();
        if (!Objects.equals(this$zipCode, other$zipCode)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Address;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $city = this.getCity();
        result = result * PRIME + ($city == null ? 43 : $city.hashCode());
        final Object $street = this.getStreet();
        result = result * PRIME + ($street == null ? 43 : $street.hashCode());
        final Object $zipCode = this.getZipCode();
        result = result * PRIME + ($zipCode == null ? 43 : $zipCode.hashCode());
        return result;
    }
}