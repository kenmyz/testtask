package com.test;

import com.test.errors.ItemNotInMarket;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ken on 20.03.2017.
 */
public class Shop {

	private Map<String, BigDecimal> items= new ConcurrentHashMap<>();

	public boolean tryBuy(String item1, BigDecimal sumForBying) {
		synchronized (this){
			if(items.get(item1).doubleValue() > sumForBying.doubleValue()) {
				return false;
			} else {
				items.remove(item1);
				return true;
			}
		}
	}
}
