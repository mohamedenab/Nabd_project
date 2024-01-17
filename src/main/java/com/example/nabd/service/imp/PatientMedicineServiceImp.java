package com.example.nabd.service.imp;

import com.example.nabd.dtos.medicineDtos.AddMedicineDto;
import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.patientDtos.PatientMedicineDto;
import com.example.nabd.entity.Patient;
import com.example.nabd.entity.Patient_Medicine;
import com.example.nabd.entity.Specialization;
import com.example.nabd.exception.ResourceNotFoundException;
import com.example.nabd.mapper.BasisResponseMapper;
import com.example.nabd.repository.MedicineRepo;
import com.example.nabd.repository.PatientRepo;
import com.example.nabd.repository.Patient_MedicineRepo;
import com.example.nabd.repository.SpecializationRepo;
import com.example.nabd.service.IPatientMedicineService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class PatientMedicineServiceImp implements IPatientMedicineService {
    private final Patient_MedicineRepo patientMedicineRepo;
    private final PatientRepo patientRepo;
    private final MedicineRepo medicineRepo;
    private final BasisResponseMapper basisResponseMapper = new BasisResponseMapper();
    private final SpecializationRepo specializationRepo;

    public PatientMedicineServiceImp(Patient_MedicineRepo patientMedicineRepo, PatientRepo patientRepo, MedicineRepo medicineRepo, SpecializationRepo specializationRepo) {
        this.patientMedicineRepo = patientMedicineRepo;
        this.patientRepo = patientRepo;
        this.medicineRepo = medicineRepo;
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
        patientMedicine.setMonth(setArrayOfMonths(patientMedicine.getStartIn().getMonth().getValue(),addMedicineDto.getRepetition()));
        Patient_Medicine savedPatientMedicine= patientMedicineRepo.save(patientMedicine);
        Specialization specialization = specializationRepo.findById(savedPatientMedicine.getSpecialization())
                .orElseThrow(()-> new ResourceNotFoundException("specialization" , "id",id));
        PatientMedicineDto patientMedicineDto = PatientMedicineDto.builder().startIn(savedPatientMedicine.getStartIn().toString())
                .Repetition(savedPatientMedicine.getRepetition()).note(savedPatientMedicine.getNotes())
                .numberPastille(savedPatientMedicine.getNumberPastille()).numberBox(savedPatientMedicine.getNumberBox())
                .medicineName(savedPatientMedicine.getMedicine().getNameInEng()).specializationName(specialization.getName())
                .id(savedPatientMedicine.getId()).arrayOfMonths(savedPatientMedicine.getMonth()).build();
        return basisResponseMapper.createBasisResponse(patientMedicineDto);
    }

    @Override
    public BasisResponse deleteMedicineBySpecialization(Long patientId, Long specializationId) {
        Patient patient = patientRepo.findById(patientId).orElseThrow(
                ()->new ResourceNotFoundException("specialization" , "id",patientId));
        List<Patient_Medicine> patientMedicines = patientMedicineRepo.findByPatientAndSpecialization(patient,specializationId);
        for (Patient_Medicine p:
                patientMedicines) {
            p.getMedicine().setNumberOfPatientTakeIt(p.getMedicine().getNumberOfPatientTakeIt()-1);
            medicineRepo.save(p.getMedicine());
            patientMedicineRepo.delete(p);
        }
        return basisResponseMapper.createBasisResponse("Deleted successfully");
    }

    @Override
    public BasisResponse delete(Long id) {
        Patient_Medicine patientMedicine = patientMedicineRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("patientMedicine" , "id",id));
        patientMedicine.getMedicine().setNumberOfPatientTakeIt(patientMedicine.getMedicine().getNumberOfPatientTakeIt()-1);
        medicineRepo.save(patientMedicine.getMedicine());
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
