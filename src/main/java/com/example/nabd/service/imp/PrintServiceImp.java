package com.example.nabd.service.imp;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.printDtos.LocationPrintDto;
import com.example.nabd.dtos.printDtos.MedicinePrintDto;
import com.example.nabd.dtos.printDtos.PatientPrintDto;
import com.example.nabd.entity.Locations;

import com.example.nabd.entity.Patient;
import com.example.nabd.exception.ResourceNotFoundException;
import com.example.nabd.mapper.BasisResponseMapper;
import com.example.nabd.repository.LocationsRepo;
import com.example.nabd.repository.PatientRepo;
import com.example.nabd.service.IPrintService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrintServiceImp implements IPrintService {
    private final LocationsRepo locationsRepo;
    private final PatientRepo patientRepo;
    private final BasisResponseMapper basisResponseMapper = new BasisResponseMapper();


    public PrintServiceImp(LocationsRepo locationsRepo, PatientRepo patientRepo) {
        this.locationsRepo = locationsRepo;
        this.patientRepo = patientRepo;

    }

    @Override
    public BasisResponse printAll() {
        List<Locations> locations = locationsRepo.findAll();
        List<LocationPrintDto> locationPrintDtos=new ArrayList<>();
        for (Locations locations1 : locations){
            String locationName = locations1.getLocationName();
            List<PatientPrintDto> patientPrintDtos =getPatientFromLocation(locations1);
            LocationPrintDto locationPrintDto = LocationPrintDto.builder().locationName(locationName)
                    .patientPrintDtos(patientPrintDtos).build();
            locationPrintDtos.add(locationPrintDto);
        }
        return basisResponseMapper.createBasisResponse(locationPrintDtos);
    }

    @Override
    public BasisResponse printLocation(Long id) {
        Locations locations = locationsRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Location","id",id));
        List<PatientPrintDto> patientPrintDtos =getPatientFromLocation(locations);
        LocationPrintDto locationPrintDto = LocationPrintDto.builder().locationName(locations.getLocationName())
                .patientPrintDtos(patientPrintDtos).build();
        return basisResponseMapper.createBasisResponse(locationPrintDto);
    }

    @Override
    public BasisResponse printPatient(Long id) {
        Patient patient = patientRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Patient","id",id));
        List<MedicinePrintDto> medicinePrintDtos = getMedicineFromPatient(patient);
        PatientPrintDto patientPrintDto = PatientPrintDto.builder().medicinePrintDtos(medicinePrintDtos).name(patient.getName())
                .phoneNumber(patient.getMobileNumbers()).build();
        return basisResponseMapper.createBasisResponse(patientPrintDto);
    }
    private List<PatientPrintDto> getPatientFromLocation(Locations locations){
        List<PatientPrintDto> patientPrintDtos = new ArrayList<>();
        for (Patient patient : locations.getPatient()){
            List<MedicinePrintDto> medicinePrintDtos = getMedicineFromPatient(patient);
            PatientPrintDto patientPrintDto = PatientPrintDto.builder().medicinePrintDtos(medicinePrintDtos).name(patient.getName())
                    .phoneNumber(patient.getMobileNumbers()).build();
            patientPrintDtos.add(patientPrintDto);
        }
        return patientPrintDtos;
    }
    private List<MedicinePrintDto> getMedicineFromPatient(Patient patient){
        return patient.getPatientMedicines().stream().map(
                patientMedicine -> MedicinePrintDto.builder().medicineName(patientMedicine.getMedicine().getNameInEng())
                        .numberPastille(patientMedicine.getNumberPastille()).numberBox(patientMedicine.getNumberBox())
                        .repetition(patientMedicine.getRepetition()).build()).toList();
    }
}
