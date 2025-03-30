package com.theboringproject.portfolio_service.repository;

import com.theboringproject.portfolio_service.model.dao.Assets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Assets, Long> {
    List<Assets> findByAssetNameContainingIgnoreCase(String query);
}
