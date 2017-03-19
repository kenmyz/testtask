package com.test;

import com.test.errors.NoItemForSelling;
import com.test.errors.UserIsAlreadyLogged;
import com.test.errors.UserNotLogged;
import com.test.service.MarketService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.BDDMockito.given;

/**
 * Created by Ken on 19.03.2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class MarketServiceTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@MockBean
	private Shop shop;

	@Before
	void setup(){
		given(this.shop.tryBuy("Item1", new BigDecimal(100))).willReturn
				(true);
		given(this.shop.tryBuy("Item2", new BigDecimal(100))).willReturn(false);
	}

	@Test
	public void testLogin() throws UserIsAlreadyLogged, UserNotLogged {
		String userName = UUID.randomUUID().toString();
		MarketService serve = getLoggedMarketService(userName);
		assert(serve.isLogged());
		assertEquals(userName, serve.getUserName());
	}

	@Test
	public void testLogoff() throws UserIsAlreadyLogged, UserNotLogged {
		MarketService serve = getLoggedMarketService(UUID.randomUUID().toString());
		serve.logoff();
		assert(!serve.isLogged());
		exception.expect(UserNotLogged.class);
		serve.getUserName();
	}

	private MarketService getLoggedMarketService(String login) throws
			UserIsAlreadyLogged {
		MarketService serve = new MarketService();
		String userName = login;
		serve.login(userName);
		return serve;
	}

	@Test
	public void testBuy(){
		MarketService serve = getLoggedMarketService(UUID.randomUUID()
				.toString());
		assert(serve.buy("Item1"));
		assertFalse(serve.buy("Item2"));
	}

	@Test
	public void testSell(){
		MarketService serve = getLoggedMarketService(UUID.randomUUID()
				.toString());
		String itemName = UUID.randomUUID().toString();
		assert(serve.buy("Item1"));
		assert(serve.sell("Item1"));
		exception.expect(NoItemForSelling.class);
		serve.sell("Item2");
	}

}
