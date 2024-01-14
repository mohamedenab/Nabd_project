package com.example.nabd.service.imp;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.HistoryDto;
import com.example.nabd.entity.History;
import com.example.nabd.entity.Patient;
import com.example.nabd.exception.ResourceNotFoundException;
import com.example.nabd.mapper.BasisResponseMapper;
import com.example.nabd.mapper.PatientMapper;
import com.example.nabd.repository.HistoryRepo;
import com.example.nabd.repository.PatientRepo;
import com.example.nabd.service.IHistoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

@Service
public class HistoryService implements IHistoryService {
    private final PatientRepo patientRepo;
    private final HistoryRepo historyRepo;
    private final PatientMapper patientMapper;
    private final BasisResponseMapper basisResponseMapper = new BasisResponseMapper();

    public HistoryService(PatientRepo patientRepo, HistoryRepo historyRepo, PatientMapper patientMapper) {
        this.patientRepo = patientRepo;
        this.historyRepo = historyRepo;
        this.patientMapper = patientMapper;
    }

    @Override
    public BasisResponse addHistory(HistoryDto historyDto, Long patientId, int year, int month) {
        LocalDate newDate = LocalDate.of(year,month,1);
        Patient patient = patientRepo.findById(patientId).orElseThrow(
                ()-> new ResourceNotFoundException("Patient" , "id",patientId));
        History history = History.builder().comment(historyDto.getComment()).startDate(newDate).updatedAt(newDate)
                .link(historyDto.getLink()).patientH(patient).historyType(historyDto.getHistoryType()).build();
        History historySaved= historyRepo.save(history);
        HistoryDto historyDtoToSend = HistoryDto.builder().patientDto(patientMapper.EntityToDto(historySaved.getPatientH()))
                .id(historySaved.getId()).comment(historySaved.getComment())
                .link(historySaved.getLink()).historyType(historySaved.getHistoryType()).build();
        return basisResponseMapper.createBasisResponse(historyDtoToSend);
    }

    @Override
    public BasisResponse updateHistory(HistoryDto historyDto , Long historyId) {
        LocalDate localDate= LocalDate.now();
        History history = historyRepo.findById(historyId).orElseThrow(
                ()-> new ResourceNotFoundException("History" , "id",historyId));
        history.setHistoryType(historyDto.getHistoryType());
        history.setComment(historyDto.getComment());
        history.setLink(historyDto.getLink());
        history.setUpdatedAt(localDate);
        History historySaved = historyRepo.save(history);
        HistoryDto historyDtoToSend = HistoryDto.builder().patientDto(patientMapper.EntityToDto(historySaved.getPatientH()))
                .id(historySaved.getId()).comment(historySaved.getComment())
                .link(historySaved.getLink()).historyType(historySaved.getHistoryType()).build();
        return basisResponseMapper.createBasisResponse(historyDtoToSend);
    }

    @Override
    public BasisResponse getHistoryById(Long id) {
        History history = historyRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("History" , "id",id));
        HistoryDto historyDtoToSend = HistoryDto.builder().patientDto(patientMapper.EntityToDto(history.getPatientH()))
                .id(history.getId()).comment(history.getComment())
                .link(history.getLink()).historyType(history.getHistoryType())
                .patientDto(patientMapper.EntityToDto(history.getPatientH())).startAt(history.getStartDate().toString())
                .updatedAt(history.getUpdatedAt().toString()).build();
        return basisResponseMapper.createBasisResponse(historyDtoToSend);
    }

    @Override
    public BasisResponse deleteHistory(Long historyId) {
        History history = historyRepo.findById(historyId).orElseThrow(
                ()-> new ResourceNotFoundException("History" , "id",historyId));
        historyRepo.delete(history);
        return basisResponseMapper.createBasisResponse("History deleted successfully");
    }
}
