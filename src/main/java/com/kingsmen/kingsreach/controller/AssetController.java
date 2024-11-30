package com.kingsmen.kingsreach.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.entity.Asset;
import com.kingsmen.kingsreach.service.AssetService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@CrossOrigin(allowCredentials = "true", origins = "https://hrms.kingsmenrealty.com")
@RestController
public class AssetController {
	
	@Autowired
	private AssetService assetService;

	@PostMapping(value = "/grantAsset")
	private ResponseEntity<ResponseStructure<Asset>> grantedAsset(@RequestBody Asset asset) {
		return assetService.grantAssets(asset);
	}
	
	@GetMapping(value = "/findAllAsset")
	private  ResponseEntity<ResponseStructure<List<Asset>>>  findAllAsset() {
		return assetService.findAllService();
	}
	
}
