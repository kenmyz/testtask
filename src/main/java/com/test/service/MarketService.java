package com.test.service;

import com.test.Shop;
import com.test.errors.ItemNotInMarket;
import com.test.errors.UserIsAlreadyLogged;
import com.test.errors.UserNotLogged;
import com.test.item.MarketItemRepository;
import com.test.item.MarketItem;
import com.test.user.MarketUser;
import com.test.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by Ken on 19.03.2017.
 */

@Component
@Scope(scopeName = "prototype")
public class MarketService {

	public static final int INIT_USER_SUM = 100;
	@Autowired
	private Shop shop;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private MarketItemRepository marketItemRepository;

	private MarketUser userName = null;

	public MarketService(Shop shop, UserRepository userRepo, MarketItemRepository marketItemRepository) {
		this.shop = shop;
		this.userRepo = userRepo;
		this.marketItemRepository = marketItemRepository;
	}

	public void login(String userName) throws UserIsAlreadyLogged {
		MarketUser user = userRepo.getByUserName(userName).orElse(new MarketUser(userName, BigDecimal.valueOf
				(INIT_USER_SUM)));
		if(!user.equals(this.userName)) {
			if(this.userName != null){
				shop.leftUser(this.userName);
			}
			shop.enterUser(user);
		}
		this.userName = user;
	}

	public boolean isLogged() {
		return userName != null;
	}

	public MarketUser getUserName() throws UserNotLogged {
		if (!isLogged()){
			throw new UserNotLogged();
		}
		return userName;
	}

	public void logout() throws UserNotLogged {
		if (!isLogged()){
			throw new UserNotLogged();
		} else {
			shop.leftUser(userName);
			userName = null;
		}
	}

	private boolean isUserCanBuy(MarketUser user, MarketItem item){
		//User has enough money to buy this item
		return user.getBalance().compareTo(item.getPrice()) > -1;
	}

	@Transactional
	public boolean buy(String itemName) throws ItemNotInMarket {
		Optional<MarketItem> item = marketItemRepository.getByItemName(itemName);
		if (!item.isPresent()){
			throw new ItemNotInMarket();
		}
		if(isUserCanBuy(userName, item.get())){
			shop.removeItem(item.get());
			userName.addItem(item.get());
			userName.getBalance().subtract(item.get().getPrice());
			return true;
		} else {
			return false;
		}
	}

	@Transactional
	public boolean sell(String itemName) throws ItemNotInMarket {
		Optional<MarketItem> item = marketItemRepository.getByItemName(itemName);
		if (!item.isPresent()){
			throw new ItemNotInMarket();
		}
		if(userName.removeItem(item.get())){
			shop.putItem(item.get());
			userName.removeItem(item.get());
			userName.getBalance().add(item.get().getPrice());
			return true;
		} else {
			return false;
		}
	}

	public StringBuilder myinfo() throws UserNotLogged {
		if(!isLogged()){
			throw new UserNotLogged();
		}
		return new StringBuilder(userName.toString());
	}

	public StringBuilder viewShop() {
		return new StringBuilder(shop.toString());
	}
}
