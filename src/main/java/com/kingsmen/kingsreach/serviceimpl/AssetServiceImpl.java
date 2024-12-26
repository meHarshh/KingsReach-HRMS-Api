package com.kingsmen.kingsreach.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Asset;
import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.entity.Notification;
import com.kingsmen.kingsreach.exceptions.AssetNotFoundException;
import com.kingsmen.kingsreach.repo.AssetRepo;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.repo.NotificationRepo;
import com.kingsmen.kingsreach.service.AssetService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@Service
public class AssetServiceImpl implements AssetService {

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private AssetRepo assetRepo;

	@Autowired
	private NotificationRepo notificationRepo;

	@Override
	public ResponseEntity<ResponseStructure<Asset>> grantAssets(Asset asset) {
		String employeeId = asset.getEmployeeId();

		Optional<Employee> optional = employeeRepo.findByEmployeeId(employeeId);
		if (optional.isPresent()) {
			Employee employee = optional.get();
			asset.setEmployee(employee);
			Asset asset1 = assetRepo.save(asset);

			String message = asset.getAssetName() + " granted to " + asset.getAssetId();

			ResponseStructure<Asset> responseStructure = new ResponseStructure<Asset>();
			responseStructure.setStatusCode(HttpStatus.OK.value());
			responseStructure.setMessage(message);
			responseStructure.setData(asset1);

			//Notification code 
			Notification notify = new Notification();
			notify.setEmployeeId(asset.getEmployeeId());
			notify.setMessage(message);
			notify.setCreatedAt(LocalDateTime.now());
			notificationRepo.save(notify);

			return new ResponseEntity<ResponseStructure<Asset>>(responseStructure, HttpStatus.OK);
		} else {
			throw new AssetNotFoundException("Asset with ID " + asset.getAssetId() + " not found");
		}

	}

	@Override
	public ResponseEntity<ResponseStructure<List<Asset>>> findAllService() {
		List<Asset> asset =assetRepo.findAll();

		ResponseStructure<List<Asset>> responseStructure = new ResponseStructure<List<Asset>>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("Asset Details Fetched successfully.");
		responseStructure.setData(asset);

		return new ResponseEntity<ResponseStructure<List<Asset>>>(responseStructure, HttpStatus.OK);

	}
}
