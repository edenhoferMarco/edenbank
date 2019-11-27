package de.marcoedenhofer.edenbank.persistence.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class CustomerAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long customerAccountId;
    private String password;
    private double managementFee;
    private boolean isArchived;
    private TanList tanList;
    @OneToMany
    private List<Loan> loans;
}
