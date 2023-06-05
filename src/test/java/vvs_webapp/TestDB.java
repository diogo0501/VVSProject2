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

	startApplicationDatabaseForTesting();
	dataSource = DriverManagerDestination.with(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    @BeforeEach
    public void setup() throws SQLException {

	Operation initDBOperations = Operations.sequenceOf(
		DELETE_ALL
		, INSERT_CUSTOMER_ADDRESS_DATA
		);

	DbSetup dbSetup = new DbSetup(dataSource, initDBOperations);

	dbSetupTracker.launchIfNecessary(dbSetup);
	
    }

    @Test 
    public void testAddClientWithExistingVAT() throws ApplicationException {

	//Add client with existent VAT
	assertThrows(ApplicationException.class, () -> {
	    CustomerService.INSTANCE.addCustomer(197672337, "FRANCISCO", 912345678);
	});

    }

    @Test
    public void testUpdateCostumerContact() throws ApplicationException{

	final int UPDATEPHONE = 215555555;
	final int VAT = 197672337;
	CustomerService.INSTANCE.updateCustomerPhone(VAT,UPDATEPHONE);

	//Check updated contact
	CustomerDTO c = CustomerService.INSTANCE.getCustomerByVat(VAT);
	assertEquals(c.phoneNumber,UPDATEPHONE);
    }

    @Test
    public void testEmptyDataBase() throws ApplicationException {

	final Operation DELETE_CUSTOMER = deleteAllFrom("CUSTOMER");

	//Empty Customer table
	DbSetup dbSetupTest = new DbSetup(dataSource, DELETE_CUSTOMER);

	dbSetupTracker.launchIfNecessary(dbSetupTest);

	assertEquals(0,CustomerService.INSTANCE.getAllCustomers().customers.size());
    }

    @Test
    public void testAddSale() throws ApplicationException {

	final int VAT = 168027852;

	//Add Sale
	SaleService.INSTANCE.addSale(VAT);
	assertEquals(NUM_INIT_SALES + 1,SaleService.INSTANCE.getAllSales().sales.size());
    }

    @Test
    public void testUpdateSale() throws ApplicationException {

	final int VAT = 168027852;

	//Add sale 
	SaleService.INSTANCE.addSale(VAT);
	SaleDTO s = SaleService.INSTANCE.getSaleByCustomerVat(VAT).sales.get(0);
	assertTrue(!s.equals(null));

	//update sale
	SaleService.INSTANCE.updateSale(s.id);
	s = SaleService.INSTANCE.getSaleByCustomerVat(VAT).sales.get(0);
	assertEquals("C",s.statusId);
    }

    @Test 
    public void testGetAllSalesFromVAT () throws ApplicationException {

	final int VAT = 168027852;
	final int NUM_SALES = 1;

	//Add set if sales
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

	//Add sale and add delivery for that sale
	for (int i = 0; i < NUM_SALES_DEL ;i++) {
	    SaleService.INSTANCE.addSale(VAT);
	    SaleDTO s = SaleService.INSTANCE.getSaleByCustomerVat(VAT).sales.get(i);
	    SaleService.INSTANCE.addSaleDelivery(s.id,i * 1000);
	}

	assertEquals(NUM_SALES_DEL,SaleService.INSTANCE.getSaleByCustomerVat(VAT).sales.size());
    }


}