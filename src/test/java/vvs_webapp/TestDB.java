package vvs_webapp;
import java.sql.SQLException;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;

import static vvs_webapp.DBSetupUtils.*;

import webapp.persistence.SaleStatus;
import webapp.services.*;

public class TestDB {

    private static Destination dataSource;

    // the tracker is static because JUnit uses a separate Test instance for every test method.
    private static DbSetupTracker dbSetupTracker = new DbSetupTracker();

    @BeforeAll
    public static void setupClass() {
	//    	System.out.println("setup Class()... ");

	startApplicationDatabaseForTesting();
	dataSource = DriverManagerDestination.with(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    @BeforeEach
    public void setup() throws SQLException {
	//		System.out.print("setup()... ");

	Operation initDBOperations = Operations.sequenceOf(
		DELETE_ALL
		, INSERT_CUSTOMER_ADDRESS_DATA
		);

	DbSetup dbSetup = new DbSetup(dataSource, initDBOperations);

	// Use the tracker to launch the DBSetup. This will speed-up tests 
	// that do not change the DB. Otherwise, just use dbSetup.launch();
	dbSetupTracker.launchIfNecessary(dbSetup);
	//		dbSetup.launch();
    }

    @Test 
    public void testAddClientWithExistingVAT() throws ApplicationException {

	assertThrows(ApplicationException.class, () -> {
	    CustomerService.INSTANCE.addCustomer(197672337, "FRANCISCO", 912345678);
	});

    }

    @Test
    public void testUpdateCostumerContact() throws ApplicationException{

	final int UPDATEPHONE = 215555555;
	final int VAT = 197672337;
	CustomerService.INSTANCE.updateCustomerPhone(VAT,UPDATEPHONE);

	CustomerDTO c = CustomerService.INSTANCE.getCustomerByVat(VAT);
	assertEquals(c.phoneNumber,UPDATEPHONE);
    }

    @Test
    public void testEmptyDataBase() throws ApplicationException {

	final Operation DELETE_CUSTOMER = deleteAllFrom("CUSTOMER");

	// Idk if this is the right way to do this. or even if Im allowed to do this
	DbSetup dbSetupTest = new DbSetup(dataSource, DELETE_CUSTOMER);

	// Use the tracker to launch the DBSetup. This will speed-up tests 
	// that do not change the DB. Otherwise, just use dbSetup.launch();
	dbSetupTracker.launchIfNecessary(dbSetupTest);

	assertEquals(0,CustomerService.INSTANCE.getAllCustomers().customers.size());
    }

    @Test
    public void testAddSale() throws ApplicationException {

	final int VAT = 168027852;

	SaleService.INSTANCE.addSale(VAT);

	assertEquals(NUM_INIT_SALES + 1,SaleService.INSTANCE.getAllSales().sales.size());
    }

    @Test
    public void testUpdateSale() throws ApplicationException {

	final int VAT = 168027852;

	SaleService.INSTANCE.addSale(VAT);

	SaleDTO s = SaleService.INSTANCE.getSaleByCustomerVat(VAT).sales.get(0);

	assertTrue(!s.equals(null));

	SaleService.INSTANCE.updateSale(s.id);

	s = SaleService.INSTANCE.getSaleByCustomerVat(VAT).sales.get(0);

	assertEquals("C",s.statusId);
    }

    @Test 
    public void testGetAllSalesFromVAT () throws ApplicationException {

	final int VAT = 168027852;
	final int NUM_SALES = 1;

	for (int i = 0; i < NUM_SALES ;i++) {
	    SaleService.INSTANCE.addSale(VAT);
	}

	assertEquals(NUM_SALES,SaleService.INSTANCE.getSaleByCustomerVat(VAT).sales.size());
    }

    @Test
    public void testAddInexistentSaleDelivery () throws ApplicationException {

	assertThrows(ApplicationException.class, () -> {
	    SaleService.INSTANCE.addSaleDelivery(15, 1000);
	});}

    @Test 
    public void testGetAllSalesDeliveriesFromVAT () throws ApplicationException {

	final int VAT = 168027852;
	final int NUM_SALES_DEL = 5;

	for (int i = 0; i < NUM_SALES_DEL ;i++) {
	    SaleService.INSTANCE.addSale(VAT);
	    SaleDTO s = SaleService.INSTANCE.getSaleByCustomerVat(VAT).sales.get(i);
	    SaleService.INSTANCE.addSaleDelivery(s.id,i * 1000);
	}

	assertEquals(NUM_SALES_DEL,SaleService.INSTANCE.getSaleByCustomerVat(VAT).sales.size());
    }


    //    //updateCustomerPhone
    //    @Test
    //    public void queryCustomerNumberTest() throws ApplicationException {
    //	//		System.out.println("queryCustomerNumberTest()... ");
    //
    //	// read-only test: unnecessary to re-launch setup after test has been run
    //	dbSetupTracker.skipNextLaunch();
    //
    //	int expected = NUM_INIT_CUSTOMERS;
    //	int actual   = CustomerService.INSTANCE.getAllCustomers().customers.size();
    //
    //	assertEquals(expected, actual);
    //    }
    //
    //    @Test
    //    public void addCustomerSizeTest() throws ApplicationException {
    //	//		System.out.println("addCustomerSizeTest()... ");
    //
    //	CustomerService.INSTANCE.addCustomer(503183504, "FCUL", 217500000);
    //	int size = CustomerService.INSTANCE.getAllCustomers().customers.size();
    //
    //	assertEquals(NUM_INIT_CUSTOMERS+1, size);
    //    }
    //
    //    private boolean hasClient(int vat) throws ApplicationException {	
    //	CustomersDTO customersDTO = CustomerService.INSTANCE.getAllCustomers();
    //
    //	for(CustomerDTO customer : customersDTO.customers)
    //	    if (customer.vat == vat)
    //		return true;			
    //	return false;
    //    }
    //
    //    @Test
    //    public void addCustomerTest() throws ApplicationException {
    //	//		System.out.println("addCustomerTest()... ");
    //
    //	assumeFalse(hasClient(503183504));
    //	CustomerService.INSTANCE.addCustomer(503183504, "FCUL", 217500000);
    //	assertTrue(hasClient(503183504));
    //    }

}
