package org.example.embed;


import javax.persistence.*;

@Embeddable
public class Contact {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PHONE_ID",referencedColumnName = "ID")
    private Phone phone;

    public String getPhoneNumber() {
        return phone.getNumber();
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }
}
