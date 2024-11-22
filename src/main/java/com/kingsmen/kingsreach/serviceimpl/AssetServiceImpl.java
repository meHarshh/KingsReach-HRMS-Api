package com.kingsmen.kingsreach.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Asset;
import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.repo.AssetRepo;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.service.AssetService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@Service
public class AssetServiceImpl implements AssetService {

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private AssetRepo assetRepo;

	@Override
	public ResponseEntity<ResponseStructure<Asset>> grantAssets(Asset asset) {
		String employeeId = asset.getEmployeeId();

		Optional<Employee> optional = employeeRepo.findByEmployeeId(employeeId);
		Employee employee = optional.get();

		asset.setEmployee(employee);

		Asset asset1=assetRepo.save(asset);

		String message=asset.getAssetName() + "granted to " + asset.getAssetId();

		ResponseStructure<Asset> responseStructure=new ResponseStructure<Asset>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage(message);
		responseStructure.setData(asset1);

		return new ResponseEntity<ResponseStructure<Asset>>(responseStructure, HttpStatus.OK);
	}

}
