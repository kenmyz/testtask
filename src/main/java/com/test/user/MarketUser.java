package com.test.user;

import com.test.item.MarketItem;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * Created by Ken on 20.03.2017.
 */
@Entity
public class MarketUser {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Version
	private Integer version;

	private String userName;

	private BigDecimal balance;

	@ManyToMany
	private Set<MarketItem> items = new HashSet<>();

	public MarketUser(String userName, BigDecimal balance) {
		this.userName = userName;
		this.balance = balance;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void addItem(MarketItem item) {
		items.add(item);
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public boolean removeItem(MarketItem marketItem) {
		return items.remove(marketItem);
	}
}
