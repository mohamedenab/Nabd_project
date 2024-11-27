package com.example.nabd.service;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.reportDtos.ReportMedicineAmountDto;

public interface IReportService {
    BasisResponse createReport();
    BasisResponse getReport(int pageNo,int pageSize,String sortBy);
    BasisResponse getMedicine(String filter);

    BasisResponse editeMedicine(Long id,Long newId);
    BasisResponse editeMedicineAmount(Long id, ReportMedicineAmountDto reportMedicineAmountDto);
    BasisResponse deleteMedicine(Long id);
    BasisResponse reportStatistics();
    BasisResponse deleteReport();
}
