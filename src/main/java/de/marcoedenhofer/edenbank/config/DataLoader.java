package de.marcoedenhofer.edenbank.config;

import de.marcoedenhofer.edenbank.application.bankaccountservice.IBankAccountService;
import de.marcoedenhofer.edenbank.application.customeraccountservice.ICustomerAccountService;
import de.marcoedenhofer.edenbank.application.customeraccountservice.PostIdentException;
import de.marcoedenhofer.edenbank.persistence.entities.*;
import de.marcoedenhofer.edenbank.persistence.repositories.ICustomerAccountRepository;
import de.marcoedenhofer.edenbank.persistence.repositories.ICustomerRepository;
import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class DataLoader implements ApplicationRunner {
    private final ICustomerAccountService registrationService;
    private final IBankAccountService bankAccountService;
    private final ICustomerAccountRepository customerAccountRepository;
    private final BCryptPasswordEncoder encoder;
    private final ICustomerRepository customerRepository;

    public DataLoader(ICustomerAccountService registrationService,
                      IBankAccountService bankAccountService,
                      ICustomerAccountRepository customerAccountRepository, BCryptPasswordEncoder encoder, ICustomerRepository customerRepository) {
        this.registrationService = registrationService;
        this.bankAccountService = bankAccountService;
        this.customerAccountRepository = customerAccountRepository;
        this.encoder = encoder;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        long count = customerAccountRepository.count();

        // only create data once, if no entry is in database.
        if (count < 5) {
            createEdenbankAccount();
            createLieferdienstAccount();
            createBigBazarAccount();
            createDummyCustomerAccount1();
            createDummyCustomerAccount2();
        }

    }

    protected void createDummyCustomerAccount1() throws ParseException {
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

        customer.setIdentified(true);
        customer = customerRepository.save(customer);
        CustomerAccount account = buildCustomerAccount(0.0, customer);
        CustomerAccount customerAccount = customerAccountRepository.save(account);
        bankAccountService.createCheckingAccountWithFixedBudged(customerAccount,1000000);
    }

    protected void createDummyCustomerAccount2() throws ParseException {
        PrivateCustomer customer = new PrivateCustomer();
        PersonalData personalData = new PersonalData();
        Address address = new Address();
        address.setCountry("Deutschland");
        address.setCity("Regensburg");
        address.setPostalNumber(93049);
        address.setStreetName("Prüfeningerstraße");
        address.setHouseNumber("17");
        personalData.setFirstname("Theodor");
        personalData.setLastname("Terstperson");
        personalData.setBirthCountry("Deutschland");
        personalData.setBirthPlace("Regensburg");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        personalData.setBirthdate(format.parse("02.02.1986"));
        personalData.setFormOfAddress("Herr");
        personalData.setPersonalAddress(address);
        customer.setEmail("theodortestperson@testmail.de");
        customer.setPersonalData(personalData);

        customer.setIdentified(true);
        customer = customerRepository.save(customer);
        CustomerAccount account = buildCustomerAccount(0.0, customer);
        CustomerAccount customerAccount =  customerAccountRepository.save(account);
        bankAccountService.createCheckingAccountWithFixedBudged(customerAccount,1000000);

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
        bankAccountService.createCheckingAccountWithFixedBudged(customerAccount,20000000000L);
    }

    private void createBigBazarAccount() throws ParseException {
        BusinessCustomer customer = new BusinessCustomer();
        PersonalData personalData = new PersonalData();
        Address personalAddress = new Address();
        Address businessAddress = new Address();

        personalAddress.setCountry("Deutschland");
        personalAddress.setCity("Regensburg");
        personalAddress.setPostalNumber(93053);
        personalAddress.setStreetName("Bazarstraße");
        personalAddress.setHouseNumber("3");

        personalData.setFormOfAddress("Herr");
        personalData.setFirstname("Fatih");
        personalData.setLastname("Arslan");
        personalData.setBirthCountry("Deutschland");
        personalData.setBirthPlace("Regensburg");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        personalData.setBirthdate(format.parse("20.02.1997"));
        personalData.setPersonalAddress(personalAddress);

        businessAddress.setCountry("Deutschland");
        businessAddress.setCity("Schwandorf");
        businessAddress.setPostalNumber(92421);
        businessAddress.setStreetName("In der Hood");
        businessAddress.setHouseNumber("90");

        customer.setEmail("fatiharslan@big-bazar.de");
        customer.setPersonalData(personalData);
        customer.setCompanyName("BigBazar GmbH");
        customer.setCompanyAddress(businessAddress);

        CustomerAccount customerAccount = registrationService.createBusinessCustomerAccount(customer);
        bankAccountService.createCheckingAccountWithFixedBudged(customerAccount,100000000);
    }

    private void createLieferdienstAccount() throws ParseException {
        BusinessCustomer customer = new BusinessCustomer();
        PersonalData personalData = new PersonalData();
        Address personalAddress = new Address();
        Address businessAddress = new Address();

        personalAddress.setCountry("Deutschland");
        personalAddress.setCity("Regensburg");
        personalAddress.setPostalNumber(93053);
        personalAddress.setStreetName("Lieferdienststraße");
        personalAddress.setHouseNumber("31");

        personalData.setFormOfAddress("Herr");
        personalData.setFirstname("Elyes");
        personalData.setLastname("Nasri");
        personalData.setBirthCountry("Deutschland");
        personalData.setBirthPlace("Regensburg");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        personalData.setBirthdate(format.parse("29.12.1990"));
        personalData.setPersonalAddress(personalAddress);

        businessAddress.setCountry("Deutschland");
        businessAddress.setCity("Regensburg");
        businessAddress.setPostalNumber(93053);
        businessAddress.setStreetName("Lieferdienststraße");
        businessAddress.setHouseNumber("32");

        customer.setEmail("elyesnasri@lieferdienst.de");
        customer.setPersonalData(personalData);
        customer.setCompanyName("Lieferdienst GmbH");
        customer.setCompanyAddress(businessAddress);

        CustomerAccount customerAccount = registrationService.createBusinessCustomerAccount(customer);
        bankAccountService.createCheckingAccountWithFixedBudged(customerAccount,100000000);
    }

    private CustomerAccount buildCustomerAccount(double managementFee, Customer customer) {
        CustomerAccount account = new CustomerAccount();
        account.setArchived(false);
        // for now use firstname as password
        String password = customer.getPersonalData().getFirstname();
        account.setPassword(encoder.encode(password));
        account.setManagementFee(managementFee);
        account.setCustomerDetails(customer);

        return account;
    }
}
