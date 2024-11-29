package com.kingsmen.kingsreach.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.kingsmen.kingsreach.entity.Asset;
import com.kingsmen.kingsreach.util.ResponseStructure;

public interface AssetService {

	ResponseEntity<ResponseStructure<Asset>> grantAssets(Asset asset);

	ResponseEntity<ResponseStructure<List<Asset>>> findAllService();

}
