package com.example.nabd.service.imp;

import com.example.nabd.dtos.AddMedicineDto;
import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.PatientMedicineDto;
import com.example.nabd.entity.Patient_Medicine;
import com.example.nabd.entity.Specialization;
import com.example.nabd.exception.ResourceNotFoundException;
import com.example.nabd.mapper.BasisResponseMapper;
import com.example.nabd.repository.Patient_MedicineRepo;
import com.example.nabd.repository.SpecializationRepo;
import com.example.nabd.service.IPatientMedicineService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class PatientMedicineServiceImp implements IPatientMedicineService {
    private final Patient_MedicineRepo patientMedicineRepo;
    private final BasisResponseMapper basisResponseMapper = new BasisResponseMapper();
    private final SpecializationRepo specializationRepo;

    public PatientMedicineServiceImp(Patient_MedicineRepo patientMedicineRepo, SpecializationRepo specializationRepo) {
        this.patientMedicineRepo = patientMedicineRepo;
        this.specializationRepo = specializationRepo;
    }

    @Override
    public BasisResponse update(Long id, AddMedicineDto addMedicineDto) {
        Patient_Medicine patientMedicine = patientMedicineRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("patientMedicine" , "id",id));
        patientMedicine.setNotes(addMedicineDto.getNotes());
        patientMedicine.setNumberBox(addMedicineDto.getNumberBox());
        patientMedicine.setNumberPastille(addMedicineDto.getNumberPastille());
        patientMedicine.setRepetition(addMedicineDto.getRepetition());
        patientMedicine.setSpecialization(addMedicineDto.getSpecialization());
        patientMedicine.setMonth(setArrayOfMonths(patientMedicine.getStartIn().getMonth(),addMedicineDto.getRepetition()));
        Patient_Medicine savedPatientMedicine= patientMedicineRepo.save(patientMedicine);
        Specialization specialization = specializationRepo.findById(savedPatientMedicine.getSpecialization())
                .orElseThrow(()-> new ResourceNotFoundException("specialization" , "id",id));
        PatientMedicineDto patientMedicineDto = PatientMedicineDto.builder().startIn(savedPatientMedicine.getStartIn())
                .Repetition(savedPatientMedicine.getRepetition()).note(savedPatientMedicine.getNotes())
                .numberPastille(savedPatientMedicine.getNumberPastille()).numberBox(savedPatientMedicine.getNumberBox())
                .medicineName(savedPatientMedicine.getMedicine().getNameInEng()).specializationName(specialization.getName())
                .id(savedPatientMedicine.getId()).arrayOfMonths(savedPatientMedicine.getMonth()).build();
        return basisResponseMapper.createBasisResponse(patientMedicineDto);
    }

    @Override
    public BasisResponse delete(Long id) {
        Patient_Medicine patientMedicine = patientMedicineRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("patientMedicine" , "id",id));
        patientMedicineRepo.delete(patientMedicine);
        return basisResponseMapper.createBasisResponse("Deleted successfully");
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
