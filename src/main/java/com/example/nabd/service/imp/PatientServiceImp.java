package com.example.nabd.service.imp;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.PatientDto;
import com.example.nabd.entity.Patient;
import com.example.nabd.mapper.BasisResponseMapper;
import com.example.nabd.repository.PatientRepo;
import com.example.nabd.service.IPatientService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PatientServiceImp implements IPatientService {
    private final ModelMapper modelMapper;
    private final PatientRepo patientRepo;
    private final BasisResponseMapper basisResponseMapper = new BasisResponseMapper();

    public PatientServiceImp(ModelMapper modelMapper, PatientRepo patientRepo) {
        this.modelMapper = modelMapper;
        this.patientRepo = patientRepo;
    }

    PatientDto mapToDto(Patient patient){
        return modelMapper.map(patient,PatientDto.class);
    }
    Patient mapToEntity(PatientDto patientDto){
        return modelMapper.map(patientDto,Patient.class);
    }
    @Override
    public BasisResponse createPatient(PatientDto patientDto) {
        Patient patient = mapToEntity(patientDto);
        Patient savedPatient= patientRepo.save(patient);
        return basisResponseMapper.createBasisResponse(mapToDto(savedPatient));
    }

    @Override
    public BasisResponse getPatient(int pageNo, int pageSize, String sortBy, String filterType , String filterValue) {
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);
        Page<Patient> patients = patientRepo.findAll(pageable);
        List<Patient> patientList = patients.getContent();
        if (filterType!=null){
            return basisResponseMapper.createBasisResponseForPatient(
                    getPatientFilter(filterType,filterValue,patientList),pageNo,patients);
        }
        List<PatientDto> patientDtoList = patientList.stream().map(this::mapToDto).toList();
        return basisResponseMapper.createBasisResponseForPatient(patientDtoList,pageNo,patients);
    }
    private List<PatientDto> getPatientFilter( String filterType , String filterValue , List<Patient> patientList){
        switch (filterType) {
            case "name" -> {
                List<Patient> patients = patientList.stream().filter(
                        patient -> patient.getName().equals(filterValue)).toList();
                return patients.stream().map(this::mapToDto).toList();
            }
            case "numberOfFamilyMembers" -> {
                List<Patient> patients = patientList.stream().filter(
                        patient -> patient.getNumberOfFamilyMembers() == Integer.parseInt(filterValue)).toList();
                return patients.stream().map(this::mapToDto).toList();
            }
            case "locations" -> {
                List<Patient> patients = patientList.stream().filter(
                        patient -> patient.getLocations().getLocationName().equals(filterValue)).toList();
                return patients.stream().map(this::mapToDto).toList();
            }
            case "specialization" -> {
                List<Patient> patients = patientList.stream().filter(
                        patient -> patient.getSpecialization().getName().equals(filterValue)).toList();
                return patients.stream().map(this::mapToDto).toList();
            }
            default -> {
                return null;
            }
        }
    }
}
