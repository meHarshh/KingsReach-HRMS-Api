package com.kingsmen.kingsreach.service;

import org.springframework.http.ResponseEntity;

import com.kingsmen.kingsreach.entity.ResignationDetail;
import com.kingsmen.kingsreach.util.ResponseStructure;

public interface ResignationDetailService {

	ResponseEntity<ResponseStructure<ResignationDetail>> resignationDetail(ResignationDetail resignationDetail);

}
