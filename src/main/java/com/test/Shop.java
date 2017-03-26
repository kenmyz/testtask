package com.test;

import com.test.errors.UserIsAlreadyLogged;
import com.test.item.MarketItem;
import com.test.item.MarketItemRepository;
import com.test.user.MarketUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Ken on 20.03.2017.
 */
@Component
public class Shop {

	@Autowired
	MarketItemRepository marketItemRepository;

	private Set<MarketItem> items = new HashSet<>();

	private final Set<MarketUser> loggedUsers = Collections.synchronizedSet(new HashSet<MarketUser>());

	public boolean removeItem(MarketItem marketItem) {
		return items.remove(marketItem);
	}

	public void putItem(MarketItem marketItem) {
		Optional<MarketItem> optionItem = marketItemRepository.getByItemName
				(marketItem.getItemName());
		if( !optionItem.isPresent()) {
			marketItemRepository.save(marketItem);
			items.add(marketItem);
		} else {
			items.add(optionItem.get());
		}
	}

	public void enterUser(MarketUser user) throws UserIsAlreadyLogged {
		if (!loggedUsers.add(user)) {
			throw new UserIsAlreadyLogged();
		}
	}

	public void leftUser(MarketUser userName) {
		loggedUsers.remove(userName);
	}
}
