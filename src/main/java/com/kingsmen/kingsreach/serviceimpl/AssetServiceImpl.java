package com.kingsmen.kingsreach.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Asset;
import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.repo.AssetRepo;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.service.AssetService;

@Service
public class AssetServiceImpl implements AssetService {

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private AssetRepo assetRepo;

	@Override
	public Asset grantAssets(Asset asset) {
		String employeeId = asset.getEmployeeId();

		Optional<Employee> optional = employeeRepo.findByEmployeeId(employeeId);
		Employee employee = optional.get();

		asset.setEmployee(employee);

		return assetRepo.save(asset);
	}

}
