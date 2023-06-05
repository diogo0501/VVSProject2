package vvs_webapp;

import static org.junit.Assert.*;
import org.junit.*;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import java.net.MalformedURLException;

import java.io.*;
import java.util.*;

public class TestHtmlUnit {

    private static final String APPLICATION_URL = "http://localhost:8080/VVS_webappdemo/";
    //	
    //
    private static WebClient webClient;
    private static HtmlPage page;

    @BeforeClass
    public static void setUpClass() throws Exception {
	webClient = new WebClient(BrowserVersion.getDefault());

	// possible configurations needed to prevent JUnit tests to fail for complex HTML pages
	webClient.setJavaScriptTimeout(15000);
	webClient.getOptions().setJavaScriptEnabled(true);
	webClient.getOptions().setThrowExceptionOnScriptError(false);
	webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
	webClient.getOptions().setCssEnabled(false);
	webClient.setAjaxController(new NicelyResynchronizingAjaxController());

	page = webClient.getPage(APPLICATION_URL);
	assertEquals(200, page.getWebResponse().getStatusCode()); // OK status
    }

    @AfterClass
    public static void takeDownClass() {
	webClient.close();
    }
    //	
    @Test
    public void testAddAddress() throws Exception {

	int num_rows = 0;
	HtmlTable table = null;
	assertEquals("WebAppDemo Menu", page.getTitleText());

	final String VAT,ADDRESS,DOOR,POSTAL,LOCALITY;

	VAT = "197672337";
	ADDRESS = "Rua das Flores";
	DOOR = "5";
	POSTAL = "2735-980";
	LOCALITY = "Lisboa";

	final String pageAsXml = page.asXml();
	assertTrue(pageAsXml.contains("<div class=\"w3-container w3-blue-grey w3-center w3-allerta\" id=\"body\">"));

	final String pageAsText = page.asText();
	assertTrue(pageAsText.contains("WebAppDemo Menu"));

	HtmlPage GetCustInfoPage = webClient.getPage(APPLICATION_URL + "getCustomerByVAT.html");
	final String GetCustInfoPageAsText = GetCustInfoPage.asText();

	assertTrue(GetCustInfoPageAsText.contains("Customer Info"));

	HtmlForm getCustomerForm = GetCustInfoPage.getForms().get(0);

	// place data at form
	HtmlInput vatInput = getCustomerForm.getInputByName("vat");
	vatInput.setValueAttribute(VAT);

	HtmlInput submit = getCustomerForm.getInputByName("submit");

	HtmlPage ClientInfoPage = submit.click();

	final String ClientInfoPageAsText = ClientInfoPage.asText();

	assertTrue(ClientInfoPageAsText.contains("Client Info"));

	try {
	    table = (HtmlTable) ClientInfoPage.getElementsByTagName("table").get(0);
	    if(table.equals(null)) {
		System.out.println("[INFO] Null Object");
	    }
	    else {
		System.out.println(num_rows);
		num_rows = table.getRowCount();
	    }
	}
	catch(IndexOutOfBoundsException e) {
	    num_rows = 1;
	}


	HtmlPage addAddressPage = webClient.getPage(APPLICATION_URL + "addAddressToCustomer.html");

	final String addAddresspageAsText = addAddressPage.asText();

	assertTrue(addAddresspageAsText.contains("Add Address"));

	//place data in address input form
	HtmlForm addAddressForm = addAddressPage.getForms().get(0);

	HtmlInput vatAddInput = addAddressForm.getInputByName("vat");
	vatAddInput.setValueAttribute(VAT);

	HtmlInput addressInput = addAddressForm.getInputByName("address");
	addressInput.setValueAttribute(ADDRESS);

	HtmlInput doorInput = addAddressForm.getInputByName("door");
	doorInput.setValueAttribute(DOOR);

	HtmlInput postCodeInput = addAddressForm.getInputByName("postalCode");
	postCodeInput.setValueAttribute(POSTAL);

	HtmlInput localInput = addAddressForm.getInputByName("locality");
	localInput.setValueAttribute(LOCALITY);

	HtmlInput Addresssubmit = addAddressForm.getInputByValue("Insert");

	HtmlPage nextPage = Addresssubmit.click();

	HtmlPage clientPage = webClient.getPage(APPLICATION_URL + "GetCustomerPageController?vat=" + VAT + "&submit=Get+Customer");

	final String clientPageAsText = clientPage.asText();

	assertTrue(clientPageAsText.contains("Client Info"));

	try {
	    table = (HtmlTable) clientPage.getElementsByTagName("table").get(0);
	    if(table.equals(null)) {
		System.out.println("[INFO] Null Object");
	    }
	    else {
		int new_num_rows = table.getRowCount();
		assertEquals("Num of rows didn't increased",new_num_rows,num_rows + 1);

		//Check if address were added 
		List<String> values = Arrays.asList(ADDRESS,DOOR,POSTAL,LOCALITY);
		HtmlTableRow r = table.getRow(new_num_rows - 1);
		int i = 0;
		for(HtmlTableCell c : r.getCells()) {
		    assertEquals(c.asText(),values.get(i));
		    i++;
		}
	    }
	}
	catch(IndexOutOfBoundsException e) {
	    num_rows = 1;
	    assertTrue(false);
	}

    }
    
    @Test
    public void testInsertSale() throws Exception {
	
	HtmlTable table;
	final String VAT = "197672337";
	String salePageUrl = "addSale.html";
	HtmlPage salePage = webClient.getPage(APPLICATION_URL + salePageUrl);
	
	final String salePageAsText = salePage.asText();
	assertTrue(salePageAsText.contains("New Sale"));
	
	HtmlForm getSaleForm = salePage.getForms().get(0);

	// place data at form
	HtmlInput vatInput = getSaleForm.getInputByName("customerVat");
	vatInput.setValueAttribute(VAT);

	HtmlInput submit = getSaleForm.getInputByValue("Add Sale");

	HtmlPage saleInfoPage = submit.click();
	
	final String saleInfoPageasText = saleInfoPage.asText();
	assertTrue(saleInfoPageasText.contains("Sale Info"));
	
	try {
	    table = (HtmlTable) saleInfoPage.getElementsByTagName("table").get(0);
	    if(table.equals(null)) {
		System.out.println("[INFO] Null Object");
	    }
	    else {
		
		//Check sale status
		HtmlTableCell  c = table.getRow(table.getRowCount() - 1).getCell(3);
		assertEquals(c.asText(),"O");
	    }
	}
	catch(IndexOutOfBoundsException e) {
	    
	}

    }
    
    @Test
    public void testCloseExistingSale() throws Exception {
	
	testInsertSale();
	HtmlTable table;
	final String ID = "1";
	String closeSalePageUrl = "UpdateSaleStatusPageControler";
	HtmlPage closeSalePage = webClient.getPage(APPLICATION_URL + closeSalePageUrl);
	
	final String salePageAsText = closeSalePage.asText();
	assertTrue(salePageAsText.contains("Close Sale"));
	
	HtmlForm getSaleForm = closeSalePage.getForms().get(0);

	// place data at form
	HtmlInput idInput = getSaleForm.getInputByName("id");
	idInput.setValueAttribute(ID);

	HtmlInput submit = getSaleForm.getInputByValue("Close Sale");
	
	HtmlPage updatedClosePage = submit.click();
	
	try {
	    table = (HtmlTable) updatedClosePage.getElementsByTagName("table").get(0);
	    if(table.equals(null)) {
		System.out.println("[INFO] Null Object");
	    }
	    else {
		//Check if sale state is correct
		HtmlTableCell c = table.getRow(2).getCell(3);
		assertEquals(c.asText(),"C");
	    }
	}
	catch(IndexOutOfBoundsException e) {
	    e.printStackTrace();
	}
    }

    @Test
    public void testNewCustSaleDelivery() throws Exception {
	
	final String VAT,DES,PHONE;
	
	VAT = "503183504";
	DES = "FCUL";
	PHONE = "217500000";
	
	HtmlPage addCustomerPage = webClient.getPage(APPLICATION_URL + "addCustomer.html");

	final String addCustPageAsText = addCustomerPage.asText();
	
	assertTrue(addCustPageAsText.contains("Customer Info"));
	
	HtmlForm addCustomerForm = addCustomerPage.getForms().get(0);
	
	//add Customer Form
	HtmlInput vatAddInput = addCustomerForm.getInputByName("vat");
	vatAddInput.setValueAttribute(VAT);

	HtmlInput designationInput = addCustomerForm.getInputByName("designation");
	designationInput.setValueAttribute(DES);

	HtmlInput phoneInput = addCustomerForm.getInputByName("phone");
	phoneInput.setValueAttribute(PHONE);

	HtmlInput AddCustsubmit = addCustomerForm.getInputByValue("Get Customer");

	HtmlPage clientInfoPage = AddCustsubmit.click();
	
	final String clientInfoPageAsTest = clientInfoPage.asText();
	
	assertTrue(clientInfoPageAsTest.contains("Client Info"));
	
	HtmlTable table;
	String salePageUrl = "addSale.html";
	HtmlPage salePage = webClient.getPage(APPLICATION_URL + salePageUrl);
	
	final String salePageAsText = salePage.asText();
	assertTrue(salePageAsText.contains("New Sale"));
	
	HtmlForm getSaleForm = salePage.getForms().get(0);

	//Check sale status
	HtmlInput vatInput = getSaleForm.getInputByName("customerVat");
	vatInput.setValueAttribute(VAT);

	HtmlInput submit = getSaleForm.getInputByValue("Add Sale");

	HtmlPage saleInfoPage = submit.click();
	
	final String saleInfoPageasText = saleInfoPage.asText();
	assertTrue(saleInfoPageasText.contains("Sale Info"));
	
	try {
	    table = (HtmlTable) saleInfoPage.getElementsByTagName("table").get(0);
	    if(table.equals(null)) {
		System.out.println("[INFO] Null Object");
	    }
	    else {
		HtmlTableCell  c = table.getRow(table.getRowCount() - 1).getCell(3);
		assertEquals(c.asText(),"O");
	    }
	}
	catch(IndexOutOfBoundsException e) {
	    
	}
	
	final String SALE_ID,ADDR_ID;
	SALE_ID = "1";
	ADDR_ID = "5"; 
	
	String addSaleDelPageUrl = "AddSaleDeliveryPageController?vat="+VAT;
	HtmlPage addSaleDelPage = webClient.getPage(APPLICATION_URL + addSaleDelPageUrl);
	
	
	//Add Sale Delivery
	final String addSaleDelPageAsText = addSaleDelPage.asText();
	assertTrue(addSaleDelPageAsText.contains("Add SaleDelivery"));
	
	HtmlForm getAddSaleDelForm = addSaleDelPage.getForms().get(0);
	
	HtmlInput addrIdInput = getAddSaleDelForm.getInputByName("addr_id");
	addrIdInput.setValueAttribute(ADDR_ID);
	
	HtmlInput saleIdInput = getAddSaleDelForm.getInputByName("sale_id");
	saleIdInput.setValueAttribute(SALE_ID);

	HtmlInput saleDelsubmit = getAddSaleDelForm.getInputByValue("Insert");

	saleDelsubmit.click();
	
	HtmlPage saleDelInfoPage = webClient.getPage(APPLICATION_URL + "GetSaleDeliveryPageController?vat=" + VAT);
	
	final String saleDelInfoPageAsText = saleDelInfoPage.asText();
	assertTrue(saleDelInfoPageAsText.contains("Sale Info"));
	
	try {
	    table = (HtmlTable) saleDelInfoPage.getElementsByTagName("table").get(0);
	    if(table.equals(null)) {
		System.out.println("[INFO] Null Object");
	    }
	    else {
		
		//Check if everything worked correctly
		HtmlTableCell saleIdCell = table.getRow(table.getRowCount() - 1).getCell(1);
		HtmlTableCell addrIdCell = table.getRow(table.getRowCount() - 1).getCell(2);
		
		assertEquals(saleIdCell.asText(),SALE_ID);
		assertEquals(addrIdCell.asText(),ADDR_ID);
	    }
	}
	catch(IndexOutOfBoundsException e) {
	    
	}
    }


}