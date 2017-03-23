package com.test;

import com.test.item.MarketItem;
import com.test.item.MarketItemRepository;
import com.test.user.MarketUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ken on 20.03.2017.
 */
@Component
public class Shop {

	@Autowired
	MarketItemRepository marketItemRepository;

	private Set<MarketItem> items = new HashSet<>();

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

}
