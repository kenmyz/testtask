package com.test.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Created by Ken on 20.03.2017.
 */
public interface UserRepository extends JpaRepository<MarketUser, Long> {

	Optional<MarketUser> getByUserName(String userName);

}
