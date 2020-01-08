package de.marcoedenhofer.edenbank.config;

import de.marcoedenhofer.edenbank.application.bankaccountservice.IBankAccountService;
import de.marcoedenhofer.edenbank.application.registrationservice.IRegistrationService;
import de.marcoedenhofer.edenbank.persistence.entities.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class DataLoader implements ApplicationRunner {
    private final IRegistrationService registrationService;
    private final IBankAccountService bankAccountService;

    public DataLoader(IRegistrationService registrationService,
                      IBankAccountService bankAccountService) {
        this.registrationService = registrationService;
        this.bankAccountService = bankAccountService;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        createDummyData();
    }

    protected void createDummyData() throws ParseException {
        PrivateCustomer customer = new PrivateCustomer();
        PersonalData personalData = new PersonalData();
        Address address = new Address();
        address.setCountry("Deutschland");
        address.setCity("Bad Abbach");
        address.setPostalNumber(93077);
        address.setStreetName("Maria-Weigert-Str.");
        address.setHouseNumber("17");
        personalData.setFirstname("Marco");
        personalData.setLastname("Edenhofer");
        personalData.setBirthCountry("Deutschland");
        personalData.setBirthPlace("Regensburg");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        personalData.setBirthdate(format.parse("03.04.1994"));
        personalData.setFormOfAddress("Herr");
        personalData.setPersonalAddress(address);
        customer.setEmail("marcoedenhofer@edenbank.de");
        customer.setPersonalData(personalData);

        CustomerAccount customerAccount = registrationService.createPrivateCustomerAccount(customer);
        bankAccountService.createCheckingAccountWithFixedBudged(customerAccount,100000);

    }
}
