package org.example.embed;

import javax.persistence.Entity;

@Entity
public class Phone {

    private String number;

    private String modelCode;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }
}
