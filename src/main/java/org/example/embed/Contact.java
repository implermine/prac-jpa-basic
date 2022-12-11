package org.example.embed;


import javax.persistence.Embeddable;

@Embeddable
public class Contact {

    private Phone phone;

    public String getPhoneNumber() {
        return phone.getNumber();
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }
}
