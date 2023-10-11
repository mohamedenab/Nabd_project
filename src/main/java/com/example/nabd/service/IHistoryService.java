package com.example.nabd.service;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.HistoryDto;

public interface IHistoryService {
    BasisResponse addHistory(HistoryDto historyDto ,Long patientId);
    BasisResponse updateHistory(HistoryDto historyDto ,Long HistoryId);
    BasisResponse getHistoryById(Long id);
    BasisResponse deleteHistory(Long historyId);
}
