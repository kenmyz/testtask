package com.test;

import com.test.item.MarketItem;
import com.test.item.MarketItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
