package com.example.jwtProject.Repository;

import com.example.jwtProject.Entity.TradersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TraderRepo extends JpaRepository<TradersEntity, Long> {
    public Optional<TradersEntity> findByEmailId(String emailId);

    Optional<TradersEntity> findByTraderId(Long traderId);
}
