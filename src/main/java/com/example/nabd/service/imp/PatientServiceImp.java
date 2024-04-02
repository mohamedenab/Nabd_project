package com.example.nabd.service.imp;

import com.example.nabd.dtos.*;
import com.example.nabd.dtos.medicineDtos.AddMedicineDto;
import com.example.nabd.dtos.patientDtos.PatientDto;
import com.example.nabd.dtos.patientDtos.PatientMedicineDto;
import com.example.nabd.entity.*;
import com.example.nabd.exception.ResourceNotFoundException;
import com.example.nabd.mapper.BasisResponseMapper;
import com.example.nabd.mapper.PatientMapper;
import com.example.nabd.repository.*;
import com.example.nabd.service.IPatientService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class PatientServiceImp implements IPatientService {
    private final ModelMapper modelMapper;
    private final PatientRepo patientRepo;
    private final MedicineRepo medicineRepo;
    private final SpecializationRepo specializationRepo;
    private final Patient_MedicineRepo patientMedicineRepo;
    private final PatientMapper patientMapper;
    private final HistoryRepo historyRepo;
    private final BasisResponseMapper basisResponseMapper = new BasisResponseMapper();

    public PatientServiceImp(ModelMapper modelMapper, PatientRepo patientRepo, MedicineRepo medicineRepo,
                             SpecializationRepo specializationRepo, Patient_MedicineRepo patientMedicineRepo, PatientMapper patientMapper, HistoryRepo historyRepo) {
        this.modelMapper = modelMapper;
        this.patientRepo = patientRepo;
        this.medicineRepo = medicineRepo;
        this.specializationRepo = specializationRepo;
        this.patientMedicineRepo = patientMedicineRepo;
        this.patientMapper = patientMapper;
        this.historyRepo = historyRepo;
    }

    @Override
    public BasisResponse createPatient(PatientDto patientDto) {
        Patient patient = patientMapper.DtoToEntity(patientDto);
        patient.setActive(true);
        Patient savedPatient= patientRepo.save(patient);
        return basisResponseMapper.createBasisResponse(patientMapper.EntityToDto(savedPatient));
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
        List<PatientDto> patientDtoList = patientList.stream().map(patientMapper::EntityToDto).toList();
        return basisResponseMapper.createBasisResponseForPatient(patientDtoList,pageNo,patients);
    }

    @Override
    public BasisResponse getPatientById(Long id) {
        Patient patient = patientRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Patient" , "id",id));
        PatientDto patientDto= patientMapper.EntityToDto(patient);
        return basisResponseMapper.createBasisResponse(patientDto);
    }

    @Override
    public BasisResponse getPatientMedicine(Long id) {
        Patient patient = patientRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Patient" , "id",id));
        LocalDate date = LocalDate.now();
        List<PatientMedicineDto> patientMedicinesDtos = new ArrayList<>();
        for (Patient_Medicine patientMedicine:
                patient.getPatientMedicines()) {
            if (patientMedicine.getMonth().contains(date.getMonth().getValue())&&patientMedicine.getStartIn().isBefore(date)){
                Specialization specialization = specializationRepo.findById(patientMedicine.getSpecialization())
                        .orElseThrow(()-> new ResourceNotFoundException("Specialization" , "id",id));
                PatientMedicineDto patientMedicineDto = PatientMedicineDto.builder().startIn(patientMedicine.getStartIn().toString())
                        .Repetition(patientMedicine.getRepetition()).note(patientMedicine.getNotes())
                        .numberPastille(patientMedicine.getNumberPastille()).numberBox(patientMedicine.getNumberBox())
                        .medicineName(patientMedicine.getMedicine().getNameInEng()).specializationName(specialization.getName())
                        .id(patientMedicine.getId()).arrayOfMonths(patientMedicine.getMonth()).patientId(patient.getId())
                        .medicineId(patientMedicine.getMedicine().getId()).specializationId(specialization.getId())
                        .isActive(patientMedicine.isActive()).build();
                patientMedicinesDtos.add(patientMedicineDto);
            }
        }
        return basisResponseMapper.createBasisResponse(patientMedicinesDtos);
    }

    @Override
    public BasisResponse getPatientHistory(Long id, int year , int month) {
        Patient patient = patientRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Patient" , "id",id));
        List<History> histories = patient.getHistories();
        List<HistoryDto> historyDtos = new ArrayList<>();
        if (year!=0 && month != 0){
            for (History history:
                 histories) {
                if (history.getStartDate().getMonth().getValue()==month&&
                    history.getStartDate().getYear()==year){
                    HistoryDto historyDto = HistoryDto.builder().historyType(history.getHistoryType()).comment(history.getComment())
                            .link(history.getLink()).id(history.getId()).startAt(history.getStartDate().toString())
                            .updatedAt(history.getUpdatedAt().toString()).build();
                    historyDtos.add(historyDto);
                }
            }
            return basisResponseMapper.createBasisResponse(historyDtos);
        }
        historyDtos = histories.stream().map(history ->
                HistoryDto.builder().historyType(history.getHistoryType()).comment(history.getComment())
                        .link(history.getLink()).id(history.getId()).build()).toList();
        return basisResponseMapper.createBasisResponse(historyDtos);
    }

    @Override
    public BasisResponse getAllPatientMedicine(Long id) {
        Patient patient = patientRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Patient" , "id",id));
        Date date = new Date();
        List<PatientMedicineDto> patientMedicinesDtos = new ArrayList<>();
        for (Patient_Medicine patientMedicine:
                patient.getPatientMedicines()) {
            Specialization specialization = specializationRepo.findById(patientMedicine.getSpecialization())
                    .orElseThrow(()-> new ResourceNotFoundException("specialization" , "id",id));
            PatientMedicineDto patientMedicineDto = PatientMedicineDto.builder().startIn(patientMedicine.getStartIn().toString())
                    .Repetition(patientMedicine.getRepetition()).note(patientMedicine.getNotes())
                    .numberPastille(patientMedicine.getNumberPastille()).numberBox(patientMedicine.getNumberBox())
                    .medicineName(patientMedicine.getMedicine().getNameInEng()).specializationName(specialization.getName())
                    .id(patientMedicine.getId()).arrayOfMonths(patientMedicine.getMonth()).patientId(patient.getId())
                    .medicineId(patientMedicine.getMedicine().getId()).specializationId(specialization.getId())
                    .isActive(patientMedicine.isActive()).build();
            patientMedicinesDtos.add(patientMedicineDto);


        }
        return basisResponseMapper.createBasisResponse(patientMedicinesDtos);
    }

    @Override
    public BasisResponse getPatientDateHistory(Long id) {
        Patient patient = patientRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Patient" , "id",id));
        List<History> patientHistories = patient.getHistories();
        DateDto dateDto = new DateDto();
        Set<Integer> years = new HashSet<>();
        Set<Integer> months = new HashSet<>();
        for (History h: patientHistories) {
            months.add(h.getStartDate().getMonth().getValue());
            years.add(h.getStartDate().getYear());
        }
        dateDto.setMonth(months.stream().toList());
        dateDto.setYear(years.stream().toList());
        return basisResponseMapper.createBasisResponse(dateDto);
    }

    @Override
    public BasisResponse updatePatient(Long id, PatientDto patientDto) {
        Patient patient = patientRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Patient" , "id",id));
        Patient toSave = patientMapper.DtoToEntity(patientDto);
        toSave.setActive(patient.isActive());
        toSave.setId(patient.getId());
        patientRepo.save(toSave);
        return basisResponseMapper.createBasisResponse(patientMapper.EntityToDto(toSave));
    }

    @Override
    public BasisResponse addMedicine(Long medicineId, Long patientId , AddMedicineDto addMedicineDto) {
        Patient patient = patientRepo.findById(patientId).orElseThrow(
                ()-> new ResourceNotFoundException("Patient" , "id",patientId));
        Medicine medicine = medicineRepo.findById(medicineId).orElseThrow(
                ()-> new ResourceNotFoundException("Medicine" , "id",medicineId));
//        Patient_Medicine patientMedicineCheck= patientMedicineRepo.findByPatientAndMedicine(patient,medicine);
//        if (patientMedicineCheck!=null){
//            throw new NabdAPIExeption("Medicine is already exist" , HttpStatus.BAD_REQUEST);
//        }
        medicine.setNumberOfPatientTakeIt(medicine.getNumberOfPatientTakeIt()+1);
        medicineRepo.save(medicine);
        LocalDate localDate = LocalDate.of(addMedicineDto.getYear(), addMedicineDto.getMonth(),1);
        Patient_Medicine patientMedicine = Patient_Medicine.builder().medicine(medicine)
                .patient(patient).numberPastille(addMedicineDto.getNumberPastille())
                .startIn(localDate).specialization(addMedicineDto.getSpecialization())
                .numberBox(addMedicineDto.getNumberBox())
                .month(setArrayOfMonths(addMedicineDto.getMonth(),addMedicineDto.getRepetition()))
                .notes(addMedicineDto.getNotes()).Repetition(addMedicineDto.getRepetition()).active(true).build();
        patientMedicineRepo.save(patientMedicine);
        PatientMedicineDto patientMedicineDto = PatientMedicineDto.builder()
                .arrayOfMonths(patientMedicine.getMonth()).numberBox(patientMedicine.getNumberBox())
                .numberPastille(patientMedicine.getNumberPastille()).Repetition(patientMedicine.getRepetition())
                .build();
        return basisResponseMapper.createBasisResponse(patientMedicineDto);
    }

    @Override
    public BasisResponse deletePatient(Long id) {
        Patient patient = patientRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Patient" , "id",id));
        List<Patient_Medicine> patientMedicine = patientMedicineRepo.findByPatient(patient);
        patientMedicineRepo.deleteAll(patientMedicine);
        patientRepo.delete(patient);
        return basisResponseMapper.createBasisResponse("Patient deleted successfully");
    }

    @Override
    public BasisResponse deactivatePatient(Long id) {
        Patient patient = patientRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Patient" , "id",id));
        patient.setActive(false);
        Patient saved = patientRepo.save(patient);
        PatientDto patientDto= patientMapper.EntityToDto(saved);
        return basisResponseMapper.createBasisResponse(patientDto);
    }

    @Override
    public BasisResponse activatePatient(Long id) {
        Patient patient = patientRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Patient" , "id",id));
        patient.setActive(true);
        Patient saved = patientRepo.save(patient);
        PatientDto patientDto= patientMapper.EntityToDto(saved);
        return basisResponseMapper.createBasisResponse(patientDto);
    }

    private List<PatientDto> getPatientFilter( String filterType , String filterValue , List<Patient> patientList){
        switch (filterType) {
            case "name" -> {
                List<Patient> patients = patientList.stream().filter(
                        patient -> patient.getName().equals(filterValue)).toList();
                return patients.stream().map(patientMapper::EntityToDto).toList();
            }
            case "numberOfFamilyMembers" -> {
                List<Patient> patients = patientList.stream().filter(
                        patient -> patient.getNumberOfFamilyMembers() == Integer.parseInt(filterValue)).toList();
                return patients.stream().map(patientMapper::EntityToDto).toList();
            }
            case "locationsId" -> {
                List<Patient> patients = patientList.stream().filter(
                        patient -> patient.getLocationId().getLocationName().equals(filterValue)).toList();
                return patients.stream().map(patientMapper::EntityToDto).toList();
            }
            case "SpecializationId" -> {
                List<Patient> patients = new ArrayList<>();
                for (Patient patient:
                     patientList) {
                    for (Specialization spe:
                         patient.getSpecializations()) {
                        if (spe.getName().equals(filterValue)){
                            patients.add(patient);
                            break;
                        }
                    }
                }
                return patients.stream().map(patientMapper::EntityToDto).toList();
            }
            default -> {
                return null;
            }
        }
    }
    private List<Integer> setArrayOfMonths(int startIn , int repetition){
        List<Integer> months = new ArrayList<>();
        months.add(startIn);
        int temp = startIn;
        while (temp<12){
            temp+=repetition;
            if (temp>12) break;
            months.add(temp);
        }
        temp=startIn;
        while (temp>1){
            temp-=repetition;
            if (temp<1) break;
            months.add(temp);
        }
        return months;
    }
}
