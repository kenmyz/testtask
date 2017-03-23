package com.test;

import com.test.errors.ItemNotInMarket;
import com.test.errors.UserIsAlreadyLogged;
import com.test.errors.UserNotLogged;
import com.test.item.MarketItem;
import com.test.service.MarketService;
import com.test.user.MarketUser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * Created by Ken on 19.03.2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
public class MarketServiceTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Autowired
	private MarketService serve;

	@Autowired
	private Shop shop;

	MarketUser user = new MarketUser("TestUser", BigDecimal.valueOf(100));
	MarketItem goodItem = new MarketItem("Item1", BigDecimal.valueOf(1));
	MarketItem badItem = new MarketItem("Item2", BigDecimal.valueOf(1));

	@Before
	public void setup(){
		shop.putItem(new MarketItem("Item1", BigDecimal.valueOf(1)));
	}

	@Test
	public void testLogin() throws UserIsAlreadyLogged, UserNotLogged {
		String userName = UUID.randomUUID().toString();
		MarketService serve = getLoggedMarketService(userName);
		assert(serve.isLogged());
		assertEquals(userName, serve.getUserName().getUserName());
	}

	@Test
	public void testLogoff() throws UserIsAlreadyLogged, UserNotLogged {
		MarketService serve = getLoggedMarketService(UUID.randomUUID().toString());
		serve.logout();
		assert(!serve.isLogged());
		exception.expect(UserNotLogged.class);
		serve.getUserName();
	}

	private MarketService getLoggedMarketService(String login) throws
			UserIsAlreadyLogged {
		String userName = login;
		serve.login(userName);
		return serve;
	}

	@Test
	public void testBuy() throws UserIsAlreadyLogged, ItemNotInMarket {
		MarketService serve = getLoggedMarketService(UUID.randomUUID()
				.toString());
		assert(serve.buy("Item1"));
		exception.expect(ItemNotInMarket.class);
		serve.buy("Item2");
	}

	@Test
	public void testSell() throws UserIsAlreadyLogged, ItemNotInMarket {
		MarketService serve = getLoggedMarketService(UUID.randomUUID()
				.toString());
		String itemName = UUID.randomUUID().toString();
		assert(serve.buy("Item1"));
		assert(serve.sell("Item1"));
		exception.expect(ItemNotInMarket.class);
		serve.sell("Item2");
	}

}
