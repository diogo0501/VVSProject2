package vvs_webapp;

import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;


class MockTest {
    

	public MockTest() {
	    
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
