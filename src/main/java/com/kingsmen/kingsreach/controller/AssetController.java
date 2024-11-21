package com.kingsmen.kingsreach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.entity.Asset;
import com.kingsmen.kingsreach.service.AssetService;

@RestController
public class AssetController {
	
	@Autowired
	private AssetService assetService;

	@PostMapping(value = "/grantAsset")
	private Asset grantedAsset(@PathVariable Asset asset) {
		return assetService.grantAssets(asset);
	}
	
}
