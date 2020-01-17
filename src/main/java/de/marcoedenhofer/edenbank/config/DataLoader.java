package de.marcoedenhofer.edenbank.config;

import de.marcoedenhofer.edenbank.application.bankaccountservice.IBankAccountService;
import de.marcoedenhofer.edenbank.application.customeraccountservice.ICustomerAccountService;
import de.marcoedenhofer.edenbank.application.customeraccountservice.PostIdentException;
import de.marcoedenhofer.edenbank.persistence.entities.*;
import de.marcoedenhofer.edenbank.persistence.repositories.ICustomerAccountRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class DataLoader implements ApplicationRunner {
    private final ICustomerAccountService registrationService;
    private final IBankAccountService bankAccountService;
    private final ICustomerAccountRepository customerAccountRepository;

    public DataLoader(ICustomerAccountService registrationService,
                      IBankAccountService bankAccountService,
                      ICustomerAccountRepository customerAccountRepository) {
        this.registrationService = registrationService;
        this.bankAccountService = bankAccountService;
        this.customerAccountRepository = customerAccountRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        long count = customerAccountRepository.count();

        // only create data once, if no entry is in database.
        if (count < 2) {
            createEdenbankAccount();
            createDummyCustomerAccount();
        }

    }

    protected void createDummyCustomerAccount() throws ParseException {
        PrivateCustomer customer = new PrivateCustomer();
        PersonalData personalData = new PersonalData();
        Address address = new Address();
        address.setCountry("Deutschland");
        address.setCity("Regensburg");
        address.setPostalNumber(93049);
        address.setStreetName("Prüfeningerstraße");
        address.setHouseNumber("17");
        personalData.setFirstname("Theresa");
        personalData.setLastname("Terstperson");
        personalData.setBirthCountry("Deutschland");
        personalData.setBirthPlace("Regensburg");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        personalData.setBirthdate(format.parse("02.02.1982"));
        personalData.setFormOfAddress("Frau");
        personalData.setPersonalAddress(address);
        customer.setEmail("theresatestperson@testmail.de");
        customer.setPersonalData(personalData);


        CustomerAccount customerAccount = null;
        try {
            customerAccount = registrationService.createPrivateCustomerAccount(customer);
        } catch (PostIdentException e) {
            e.printStackTrace();
        }
        bankAccountService.createCheckingAccountWithFixedBudged(customerAccount,100000);

    }

    private void createEdenbankAccount() throws ParseException {
        BusinessCustomer customer = new BusinessCustomer();
        PersonalData personalData = new PersonalData();
        Address personalAddress = new Address();
        Address businessAddress = new Address();

        personalAddress.setCountry("Deutschland");
        personalAddress.setCity("Regensburg");
        personalAddress.setPostalNumber(93053);
        personalAddress.setStreetName("Bankstraße");
        personalAddress.setHouseNumber("12");

        personalData.setFormOfAddress("Herr");
        personalData.setFirstname("Marco");
        personalData.setLastname("Edenhofer");
        personalData.setBirthCountry("Deutschland");
        personalData.setBirthPlace("Regensburg");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        personalData.setBirthdate(format.parse("03.04.1994"));
        personalData.setPersonalAddress(personalAddress);

        businessAddress.setCountry("Deutschland");
        businessAddress.setCity("Regensburg");
        businessAddress.setPostalNumber(93053);
        businessAddress.setStreetName("Bankstrasse");
        businessAddress.setHouseNumber("10");

        customer.setEmail("marcoedenhofer@edenbank.de");
        customer.setPersonalData(personalData);
        customer.setCompanyName("edenbank GmbH");
        customer.setCompanyAddress(businessAddress);

        CustomerAccount customerAccount = registrationService.createBusinessCustomerAccount(customer);
        bankAccountService.createCheckingAccountWithFixedBudged(customerAccount,2000000000);
    }
}
