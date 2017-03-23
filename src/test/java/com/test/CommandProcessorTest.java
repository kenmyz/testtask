package com.test;

import com.test.errors.ItemNotInMarket;
import com.test.errors.UserIsAlreadyLogged;
import com.test.errors.UserNotLogged;
import com.test.service.MarketService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Created by Ken on 19.03.2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommandProcessorTest {

	public static final String MOCKED_MYINFO = "Mocked myinfo";
	private static final String MOCKED_VIEW_SHOP = "Mocked viewShop"
			;
	@Autowired
	CommandProcessor processor;

	@MockBean
	MarketService service;

	@Before
	public void setup() throws ItemNotInMarket, UserNotLogged {
		given(service.myinfo()).willReturn(new StringBuilder(MOCKED_MYINFO));
		given(service.viewShop()).willReturn(new StringBuilder
				(MOCKED_VIEW_SHOP));
	}

	@Test
	public void testWrongCommand(){
		assert ("The command is empty".equals(processor.processCommand("")
				.toString()));
		String quote = String.valueOf('"');
		assertThat(processor.processCommand("randomcommand")
				.toString(), containsString("Command " + quote +
				"randomcommand" + quote +
				" is " +
				"wrong, " +
				"available commands are " +
				"[buy, login, logout, myinfo, sell, viewshop]"));
	}


	@Test
	public void testLoginCommand() throws UserIsAlreadyLogged {
		assert("OK".equals(processor.processCommand("login login1").toString()));
		verify(service).login("login1");
		assertThat("Wrong parameters count for command login. Got 0 parameters, need 1",
				containsString(processor.processCommand("login").toString()));
	}

	@Test
	public void testLogoutCommand() {
		assert ("OK".equals(processor.processCommand("logout").toString()));
		verify(service).logout();
	}

	@Test
	public void testviewShopCommand() {
		assert (MOCKED_VIEW_SHOP.equals(processor.processCommand("viewshop").toString()));
		verify(service).viewShop();
	}

	@Test
	public void testMyInfoCommand() throws UserNotLogged {
		assert (MOCKED_MYINFO.equals(processor.processCommand("myinfo").toString()));
		verify(service).myinfo();
	}

	@Test
	public void testBuyCommand() throws ItemNotInMarket {
		assert ("OK".equals(processor.processCommand("buy item").toString()));
		verify(service).buy("item");
	}

	@Test
	public void testSellCommand() throws ItemNotInMarket {
		assert ("OK".equals(processor.processCommand("sell item").toString()));
		verify(service).sell("item");
	}
}
