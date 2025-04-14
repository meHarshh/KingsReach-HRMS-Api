package com.kingsmen.kingsreach.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.Asset;

public interface AssetRepo extends JpaRepository<Asset, Integer>{

	Asset findByAssetId(String assetId);

	List<Asset> findByEmployeeId(String employeeId);

	
}
