package com.test.item;

import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

/**
 * Created by Ken on 20.03.2017.
 */
public interface MarketItemRepository extends CrudRepository<MarketItem, Long> {

	Optional<MarketItem> getByItemName(String itemName);
}
