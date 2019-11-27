package de.marcoedenhofer.edenbank.persistence.entities;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.util.List;

@Embeddable
public class TanList {
    @ElementCollection
    private List<Tan> tanNumbers;

    public List<Tan> getTanNumbers() {
        return tanNumbers;
    }

    public void setTanNumbers(List<Tan> tanNumbers) {
        this.tanNumbers = tanNumbers;
    }
}
