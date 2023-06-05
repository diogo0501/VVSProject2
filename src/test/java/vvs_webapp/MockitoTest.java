package vvs_webapp;

import static org.junit.Assert.*;
import webapp.services.AddressDTO;
import webapp.services.AddressesDTO;
import webapp.services.ApplicationException;
import webapp.services.CustomerService;
import webapp.services.SaleService;
import webapp.services.SalesDTO;
import webapp.services.SaleDTO;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations.Mock;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.*;
import static vvs_webapp.DBSetupUtils.DB_PASSWORD;
import static vvs_webapp.DBSetupUtils.DB_URL;
import static vvs_webapp.DBSetupUtils.DB_USERNAME;
import static vvs_webapp.DBSetupUtils.DELETE_ALL;
import static vvs_webapp.DBSetupUtils.INSERT_CUSTOMER_ADDRESS_DATA;
import static vvs_webapp.DBSetupUtils.startApplicationDatabaseForTesting;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(DatacenterEnum.class)
public class MockitoTest {

    @Mock
    SaleService saleservice;
    final int VAT = 197672337;
    final String ADDRESS = "Rua das Papoilas";
    final int ID = 10;
    //    	private AddressDTO addr = new AddressDTO(ID,VAT,ADDRESS);
    //    	private AddressDTO spy_addr = spy(addr);


    @Test
    public void AddrIsAddrTest() throws ApplicationException {

	final String MESSAGE = "Invalid parameter";
	ApplicationException ae = mock(ApplicationException.class);
	
	Throwable t = new Throwable(MESSAGE);

	when(ae.initCause(null)).thenReturn(t);
	
	assertThrows(ApplicationException.class, () -> {
	    //assertEquals(ae.getCause(),t);
	    throw ae;
	    
	});
	

	//    	   SaleDTO sale = new SaleDTO(1,null,0.0,"O",VAT);
	//    	   List<SaleDTO> ls = new ArrayList<SaleDTO>();
	//    	   ls.add(sale);
	//    	   SalesDTO sales = new SalesDTO(ls);
	//    	   
	//    	   when(saleService.INSTANCE.getSaleByCustomerVat(VAT)).thenReturn(sales);
	//    	   
	//    	   assertEquals(sales.sales.get(0).customerVat,VAT);

	//System.out.println(a.address);



    }

    @Test
    public void addAddressMocktoCustomerTest() {


    }
    //	private Messenger messenger;
    //	
    //	TemplateEngine templateEngine = mock(TemplateEngine.class);
    //	MailServer mailServer = mock(MailServer.class);
    //	Template template = mock(Template.class);
    //	Client client = mock(Client.class);
    //	
    //	private static final String MSG_CONTENT = "TEST MESSAGE";
    //	private static final String SOME_EMAIL = "some@email.com";
    //	
    //	@BeforeEach
    //	public void init() {
    //		messenger = new Messenger(mailServer, templateEngine);
    //	}
    //
    //	@Test
    //	public void dummyObjectTest() {
    //		// template is a dummy object, we don't need it for anything useful
    //		// its methods are not invoked at all, or the result of calling them is of no importance
    //		messenger.sendMessage(client, template);
    //	}
    //	
    //	@Test
    //	public void stubTest() {
    //		// stubs have behavior that can be described via 'when'
    //		when(templateEngine.prepareMessage(template, client)).thenReturn(MSG_CONTENT);
    //		
    //		messenger.sendMessage(client, template);
    //	}
    //	
    //	@Test
    //	public void spyTest() {
    //		// The only indirect output of the SUT is its communication with the send() method of mailServer. 
    //		// This is something we can verify using a test spy/mock
    //		
    //		// notice that client and templateEngine are not dummies, they have behavior
    //		when(client.getEmail()).thenReturn(SOME_EMAIL);
    //		when(templateEngine.prepareMessage(template, client)).thenReturn(MSG_CONTENT);
    //	
    //		messenger.sendMessage(client, template);
    //		
    //		// check whether the SUT is behaving as we expect it to
    //		verify(mailServer).send(SOME_EMAIL, MSG_CONTENT);
    //	}

}
