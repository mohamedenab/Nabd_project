package com.example.nabd.service;

import com.example.nabd.dtos.BasisResponse;

public interface IReportService {
    BasisResponse createReport();
    BasisResponse getReport();
    BasisResponse deleteReport();
}
