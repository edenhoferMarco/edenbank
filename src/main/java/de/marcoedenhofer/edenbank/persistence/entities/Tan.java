package de.marcoedenhofer.edenbank.persistence.entities;

import javax.persistence.Embeddable;

@Embeddable
public class Tan {
    private int tanNumber;
    private int value;
    private boolean isUsed;
}
