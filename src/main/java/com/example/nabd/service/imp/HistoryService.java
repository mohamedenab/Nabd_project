package com.example.nabd.service.imp;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.HistoryDto;
import com.example.nabd.dtos.PatientDto;
import com.example.nabd.entity.History;
import com.example.nabd.entity.Patient;
import com.example.nabd.exception.ResourceNotFoundException;
import com.example.nabd.mapper.BasisResponseMapper;
import com.example.nabd.repository.HistoryRepo;
import com.example.nabd.repository.PatientRepo;
import com.example.nabd.service.IHistoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class HistoryService implements IHistoryService {
    private final PatientRepo patientRepo;
    private final HistoryRepo historyRepo;
    private final ModelMapper modelMapper;
    private final BasisResponseMapper basisResponseMapper = new BasisResponseMapper();

    public HistoryService(PatientRepo patientRepo, HistoryRepo historyRepo, ModelMapper modelMapper) {
        this.patientRepo = patientRepo;
        this.historyRepo = historyRepo;
        this.modelMapper = modelMapper;
    }
    PatientDto mapToDto(Patient patient){
        return modelMapper.map(patient,PatientDto.class);
    }

    @Override
    public BasisResponse addHistory(HistoryDto historyDto, Long patientId) {
        Patient patient = patientRepo.findById(patientId).orElseThrow(
                ()-> new ResourceNotFoundException("Patient" , "id",patientId));
        History history = History.builder().comment(historyDto.getComment())
                .link(historyDto.getLink()).patient(patient).historyType(historyDto.getHistoryType()).build();
        History historySaved= historyRepo.save(history);
        HistoryDto historyDtoToSend = HistoryDto.builder().patientDto(mapToDto(historySaved.getPatient()))
                .id(historySaved.getId()).comment(historySaved.getComment())
                .link(historySaved.getLink()).historyType(historySaved.getHistoryType()).build();
        return basisResponseMapper.createBasisResponse(historyDtoToSend);
    }

    @Override
    public BasisResponse updateHistory(HistoryDto historyDto , Long historyId) {
        History history = historyRepo.findById(historyId).orElseThrow(
                ()-> new ResourceNotFoundException("History" , "id",historyId));
        history.setHistoryType(historyDto.getHistoryType());
        history.setComment(historyDto.getComment());
        history.setLink(historyDto.getLink());
        history.setUpdatedAt(new Date());
        History historySaved = historyRepo.save(history);
        HistoryDto historyDtoToSend = HistoryDto.builder().patientDto(mapToDto(historySaved.getPatient()))
                .id(historySaved.getId()).comment(historySaved.getComment())
                .link(historySaved.getLink()).historyType(historySaved.getHistoryType()).build();
        return basisResponseMapper.createBasisResponse(historyDtoToSend);
    }

    @Override
    public BasisResponse getHistoryById(Long id) {
        History history = historyRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("History" , "id",id));
        HistoryDto historyDtoToSend = HistoryDto.builder().patientDto(mapToDto(history.getPatient()))
                .id(history.getId()).comment(history.getComment())
                .link(history.getLink()).historyType(history.getHistoryType())
                .patientDto(mapToDto(history.getPatient())).build();
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
