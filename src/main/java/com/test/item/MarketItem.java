package com.test.item;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Ken on 20.03.2017.
 */

@Entity
public class MarketItem {

	//http://www.oracle.com/technetwork/middleware/ias/id-generation-083058.html
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEM_SEQ")
	@SequenceGenerator(sequenceName = "item_seq", initialValue = 1,
			allocationSize = 1, name = "ITEM_SEQ")
	Long id;

	private String itemName;

	private BigDecimal price;

	protected MarketItem() {}

	public MarketItem(String itemName, BigDecimal price) {
		this.itemName = itemName;
		this.price = price;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		MarketItem that = (MarketItem) o;

		if (!itemName.equals(that.itemName)) return false;
		return price.equals(that.price);
	}

	@Override
	public int hashCode() {
		int result = itemName.hashCode();
		result = 31 * result + price.hashCode();
		return result;
	}
}
