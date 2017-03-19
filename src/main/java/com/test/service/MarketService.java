package com.test.service;

import com.test.Shop;
import com.test.errors.UserIsAlreadyLogged;
import com.test.errors.UserNotLogged;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by Ken on 19.03.2017.
 */

@Service
public class MarketService {

	@Autowired
	Shop shop;

	@Autowired
	UserRepository userRepo;

	Set<String> loggedUsers = new ConcurrentSkipListSet<>();

	private MarketUser userName = null;

	public void login(String userName) throws UserIsAlreadyLogged {
		if (!loggedUsers.add(userName)) {
			throw new UserIsAlreadyLogged();
		}
		this.userName = userRepo.getUserByName(userName);
	}

	public boolean isLogged() {
		return userName != null;
	}

	public String getUserName() throws UserNotLogged {
		if (!isLogged()){
			throw new UserNotLogged();
		}
		return userName;
	}

	public void logoff() {
		userName = null;
	}

	public boolean buy(String item1) {
		if(shop.tryBuy(item1, userName.getBalance())){
			user.addItem(item1);
			BigDecimal itemPrice = 0;
			user.decreaseBalance(itemPrice);
		}
	}
}
