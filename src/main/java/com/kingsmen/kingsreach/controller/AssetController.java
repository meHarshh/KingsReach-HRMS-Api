package com.kingsmen.kingsreach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.entity.Asset;
import com.kingsmen.kingsreach.service.AssetService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@RestController
public class AssetController {
	
	@Autowired
	private AssetService assetService;

	@PostMapping(value = "/grantAsset")
	private ResponseEntity<ResponseStructure<Asset>> grantedAsset(@RequestBody Asset asset) {
		return assetService.grantAssets(asset);
	}
	
}
