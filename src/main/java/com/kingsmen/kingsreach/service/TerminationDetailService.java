package com.kingsmen.kingsreach.service;

import org.springframework.http.ResponseEntity;

import com.kingsmen.kingsreach.entity.TerminationDetail;
import com.kingsmen.kingsreach.util.ResponseStructure;

public interface TerminationDetailService {

	ResponseEntity<ResponseStructure<TerminationDetail>> terminationDetail(TerminationDetail detail);

	ResponseEntity<ResponseStructure<TerminationDetail>> editTermination(TerminationDetail terminationDetail);

}
