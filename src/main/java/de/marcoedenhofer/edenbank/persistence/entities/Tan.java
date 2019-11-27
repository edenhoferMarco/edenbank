package de.marcoedenhofer.edenbank.persistence.entities;

import javax.persistence.Embeddable;

@Embeddable
public class Tan {
    private int tanNumber;
    private int value;
    private boolean isUsed;

    public int getTanNumber() {
        return tanNumber;
    }

    public void setTanNumber(int tanNumber) {
        this.tanNumber = tanNumber;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }
}
