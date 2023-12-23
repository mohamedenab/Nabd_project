package com.example.nabd.service;

import com.example.nabd.dtos.BasisResponse;

public interface IReportService {
    BasisResponse createReport();
    BasisResponse getReport(int pageNo,int pageSize,String sortBy);
    BasisResponse deleteMedicine(Long id);
    BasisResponse deleteReport();
}
