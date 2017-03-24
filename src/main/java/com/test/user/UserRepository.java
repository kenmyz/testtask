package com.test.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Created by Ken on 20.03.2017.
 */
public interface UserRepository extends JpaRepository<MarketUser, Long> {

	Optional<MarketUser> getByUserName(String userName);

}
