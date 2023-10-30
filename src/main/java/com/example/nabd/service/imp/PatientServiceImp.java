package com.example.nabd.service.imp;

import com.example.nabd.dtos.*;
import com.example.nabd.entity.*;
import com.example.nabd.exception.NabdAPIExeption;
import com.example.nabd.exception.ResourceNotFoundException;
import com.example.nabd.mapper.BasisResponseMapper;
import com.example.nabd.repository.MedicineRepo;
import com.example.nabd.repository.PatientRepo;
import com.example.nabd.repository.Patient_MedicineRepo;
import com.example.nabd.service.IPatientService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PatientServiceImp implements IPatientService {
    private final ModelMapper modelMapper;
    private final PatientRepo patientRepo;
    private final MedicineRepo medicineRepo;
    private final Patient_MedicineRepo patientMedicineRepo;
    private final BasisResponseMapper basisResponseMapper = new BasisResponseMapper();

    public PatientServiceImp(ModelMapper modelMapper, PatientRepo patientRepo, MedicineRepo medicineRepo, Patient_MedicineRepo patientMedicineRepo) {
        this.modelMapper = modelMapper;
        this.patientRepo = patientRepo;
        this.medicineRepo = medicineRepo;
        this.patientMedicineRepo = patientMedicineRepo;
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

    @Override
    public BasisResponse getPatientById(Long id) {
        Patient patient = patientRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Patient" , "id",id));
        PatientDto patientDto= mapToDto(patient);
        return basisResponseMapper.createBasisResponse(patientDto);
    }

    @Override
    public BasisResponse getPatientMedicine(Long id) {
        Patient patient = patientRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Patient" , "id",id));
        Date date = new Date();
        List<Medicine> medicineList = new ArrayList<>();
        for (Patient_Medicine patientMedicine:
                patient.getPatientMedicines()) {
            Medicine medicine = patientMedicine.getMedicine();
            Patient_Medicine patientMedicine1 = patientMedicineRepo.findByPatientAndMedicine(patient,medicine);
            System.out.println(patientMedicine1.getMonth().contains(date.getMonth()+1));
            System.out.println(patientMedicine1.getStartIn().after(date));
            if (patientMedicine1.getMonth().contains(date.getMonth()+1)&&patientMedicine1.getStartIn().before(date)){
                medicineList.add(patientMedicine.getMedicine());
            }

        }
        List<MedicineDto> medicineDtoList = medicineList.stream().map(medicine ->
                MedicineDto.builder().id(medicine.getId()).price(medicine.getPrice()).nameInEng(medicine.getNameInEng())
                        .nameInArb(medicine.getNameInArb()).numberOfPastilleInEachBox(medicine.getNumberOfPastilleInEachBox())
                        .activeSubstance(medicine.getActiveSubstance())
                        .numberOfPatientTakeIt(medicine.getNumberOfPatientTakeIt()).medicineStatus(medicine.getMedicineStatus())
                        .build()).toList();
        return basisResponseMapper.createBasisResponse(medicineDtoList);
    }

    @Override
    public BasisResponse getPatientHistory(Long id, int year , int month) {
        Patient patient = patientRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Patient" , "id",id));
        List<History> histories = patient.getHistories();
        List<HistoryDto> historyDtos = new ArrayList<>();
        if (year!=0 && month != 0){
            for (History history:
                 histories) {
                if (history.getStartDate().getMonth()==month
                        &&history.getStartDate().getYear()==year){
                    HistoryDto historyDto = HistoryDto.builder().historyType(history.getHistoryType()).comment(history.getComment())
                            .link(history.getLink()).id(history.getId()).build();
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
        List<Medicine> medicineList = new ArrayList<>();
        for (Patient_Medicine patientMedicine:
                patient.getPatientMedicines()) {
            medicineList.add(patientMedicine.getMedicine());
        }
        List<MedicineDto> medicineDtoList = medicineList.stream().map(medicine ->
                MedicineDto.builder().id(medicine.getId()).price(medicine.getPrice()).nameInEng(medicine.getNameInEng())
                        .nameInArb(medicine.getNameInArb()).numberOfPastilleInEachBox(medicine.getNumberOfPastilleInEachBox())
                        .activeSubstance(medicine.getActiveSubstance())
                        .numberOfPatientTakeIt(medicine.getNumberOfPatientTakeIt()).medicineStatus(medicine.getMedicineStatus())
                        .build()).toList();
        return basisResponseMapper.createBasisResponse(medicineDtoList);
    }

    @Override
    public BasisResponse updatePatient(Long id, PatientDto patientDto) {
        Patient patient = patientRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Patient" , "id",id));
        Patient toSave = mapToEntity(patientDto);
        toSave.setId(patient.getId());
        patientRepo.save(toSave);
        return basisResponseMapper.createBasisResponse(mapToDto(toSave));
    }

    @Override
    public BasisResponse addMedicine(Long medicineId, Long patientId , AddMedicineDto addMedicineDto) {
        Patient patient = patientRepo.findById(patientId).orElseThrow(
                ()-> new ResourceNotFoundException("Patient" , "id",patientId));
        Medicine medicine = medicineRepo.findById(medicineId).orElseThrow(
                ()-> new ResourceNotFoundException("Medicine" , "id",medicineId));
        Patient_Medicine patientMedicineCheck= patientMedicineRepo.findByPatientAndMedicine(patient,medicine);
        if (patientMedicineCheck!=null){
            throw new NabdAPIExeption("Medicine is already exist" , HttpStatus.BAD_REQUEST);
        }
        System.out.println(addMedicineDto.getStartIn().getMonth());
        medicine.setNumberOfPatientTakeIt(medicine.getNumberOfPatientTakeIt()+1);
        medicineRepo.save(medicine);
        Patient_Medicine patientMedicine = Patient_Medicine.builder().medicine(medicine)
                .patient(patient).numberPastille(addMedicineDto.getNumberPastille())
                .startIn(addMedicineDto.getStartIn()).specialization(addMedicineDto.getSpecialization())
                .numberBox(addMedicineDto.getNumberBox())
                .month(setArrayOfMonths(addMedicineDto.getStartIn().getMonth()+1,addMedicineDto.getRepetition())).build();
        patientMedicineRepo.save(patientMedicine);
        PatientMedicineDto patientMedicineDto = PatientMedicineDto.builder()
                .arrayOfMonths(patientMedicine.getMonth()).numberBox(patientMedicine.getNumberBox())
                .numberPastille(patientMedicine.getNumberPastille()).Repetition(patientMedicine.getRepetition())
                .build();
        return basisResponseMapper.createBasisResponse(patientMedicineDto);
    }

    @Override
    public String deletePatient(Long id) {
        Patient patient = patientRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Patient" , "id",id));
        patientRepo.delete(patient);
        return "Patient deleted successfully";
    }

    @Override
    public String deactivatePatient(Long id) {
        Patient patient = patientRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Patient" , "id",id));
        patient.setActive(false);
        patientRepo.save(patient);
        return "Patient deactivated successfully";
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
            case "locationsId" -> {
                List<Patient> patients = patientList.stream().filter(
                        patient -> patient.getLocationId().getLocationName().equals(filterValue)).toList();
                return patients.stream().map(this::mapToDto).toList();
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
                return patients.stream().map(this::mapToDto).toList();
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
            months.add(temp);
        }
        temp=startIn;
        while (temp>1){
            temp-=repetition;
            months.add(temp);
        }
        return months;
    }
}
