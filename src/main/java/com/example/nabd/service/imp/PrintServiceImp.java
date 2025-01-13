package com.example.nabd.service.imp;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.printDtos.LocationPrintDto;
import com.example.nabd.dtos.printDtos.MedicinePrintDto;
import com.example.nabd.dtos.printDtos.PatientPrintDto;
import com.example.nabd.entity.Locations;

import com.example.nabd.entity.Patient;
import com.example.nabd.entity.Patient_Medicine;
import com.example.nabd.exception.ResourceNotFoundException;
import com.example.nabd.mapper.BasisResponseMapper;
import com.example.nabd.repository.LocationsRepo;
import com.example.nabd.repository.PatientRepo;
import com.example.nabd.service.IPrintService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
            List<PatientPrintDto> patientPrintDtos1 = new ArrayList<>();
            for (PatientPrintDto p : patientPrintDtos){
                if (!p.getMedicinePrintDtos().isEmpty()){
                    patientPrintDtos1.add(p);
                }
            }
            LocationPrintDto locationPrintDto = LocationPrintDto.builder().locationName(locationName)
                    .patientPrintDtos(patientPrintDtos1).build();
            locationPrintDtos.add(locationPrintDto);
        }
        return basisResponseMapper.createBasisResponse(locationPrintDtos);
    }

    @Override
    public BasisResponse printLocation(Long id) {
        Locations locations = locationsRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Location","id",id));
        List<PatientPrintDto> patientPrintDtos =getPatientFromLocation(locations);
        List<PatientPrintDto> patientPrintDtos1 = new ArrayList<>();
        for (PatientPrintDto p : patientPrintDtos){
            if (!p.getMedicinePrintDtos().isEmpty()){
                patientPrintDtos1.add(p);
            }
        }
        LocationPrintDto locationPrintDto = LocationPrintDto.builder().locationName(locations.getLocationName())
                .patientPrintDtos(patientPrintDtos1).build();
        return basisResponseMapper.createBasisResponse(locationPrintDto);
    }

    @Override
    public BasisResponse printPatient(Long id) {
        Patient patient = patientRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Patient","id",id));
        List<MedicinePrintDto> medicinePrintDtos = getMedicineFromPatient(patient);
        PatientPrintDto patientPrintDto = PatientPrintDto.builder().medicinePrintDtos(medicinePrintDtos).name(patient.getName())
                .phoneNumber(patient.getMobileNumbers()).dateOfBeginningOfDecision(patient.getDateOfBeginningOfDecision()).build();
        return basisResponseMapper.createBasisResponse(patientPrintDto);
    }
    private List<PatientPrintDto> getPatientFromLocation(Locations locations){
        List<PatientPrintDto> patientPrintDtos = new ArrayList<>();
        for (Patient patient : locations.getPatient()){
            List<MedicinePrintDto> medicinePrintDtos = getMedicineFromPatient(patient);
            PatientPrintDto patientPrintDto = PatientPrintDto.builder().medicinePrintDtos(medicinePrintDtos).name(patient.getName())
                    .phoneNumber(patient.getMobileNumbers()).dateOfBeginningOfDecision(patient.getDateOfBeginningOfDecision())
                    .build();
            patientPrintDtos.add(patientPrintDto);
        }
        return patientPrintDtos;
    }
    private List<MedicinePrintDto> getMedicineFromPatient(Patient patient){
        List<Patient_Medicine> patientPrintDtos = new ArrayList<>();
        LocalDate localDate = LocalDate.now();
        for (Patient_Medicine p : patient.getPatientMedicines()){
            if (p.getStartIn().getYear()<=localDate.getYear()
                    &&p.getMonth().contains(localDate.getMonth().getValue())
                    &&p.getStartIn().isBefore(localDate)
                    &&p.getPatient().isActive()&& p.isActive()){
                patientPrintDtos.add(p);
            }
        }
        return patientPrintDtos.stream().map(
                patientMedicine -> MedicinePrintDto.builder().medicineName(patientMedicine.getMedicine().getNameInEng())
                        .numberPastille(patientMedicine.getNumberPastille()).numberBox(patientMedicine.getNumberBox())
                        .repetition(patientMedicine.getRepetition()).notes(patientMedicine.getNotes()).build()).toList();
    }
}
