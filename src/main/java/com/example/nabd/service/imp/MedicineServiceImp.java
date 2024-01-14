package com.example.nabd.service.imp;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.MedicineDto;
import com.example.nabd.dtos.PatientDto;
import com.example.nabd.entity.Medicine;
import com.example.nabd.entity.Patient;
import com.example.nabd.entity.Patient_Medicine;
import com.example.nabd.enums.MedicineStatus;
import com.example.nabd.exception.NabdAPIExeption;
import com.example.nabd.exception.ResourceNotFoundException;
import com.example.nabd.mapper.BasisResponseMapper;
import com.example.nabd.mapper.PatientMapper;
import com.example.nabd.repository.MedicineRepo;
import com.example.nabd.repository.PatientRepo;
import com.example.nabd.repository.Patient_MedicineRepo;
import com.example.nabd.service.IMedicineService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicineServiceImp implements IMedicineService {
    private final MedicineRepo medicineRepo;
    private final PatientRepo patientRepo;
    private final Patient_MedicineRepo patientMedicineRepo;
    private final PatientMapper patientMapper;
    private final BasisResponseMapper basisResponseMapper = new BasisResponseMapper();

    public MedicineServiceImp(MedicineRepo medicineRepo, PatientRepo patientRepo,
            Patient_MedicineRepo patientMedicineRepo, PatientMapper patientMapper) {
        this.medicineRepo = medicineRepo;
        this.patientRepo = patientRepo;
        this.patientMedicineRepo = patientMedicineRepo;
        this.patientMapper = patientMapper;
    }

    @Override
    public BasisResponse create(MedicineDto medicineDto) {
        Medicine medicine = Medicine.builder().medicineStatus(MedicineStatus.UPDATED)
                .price(medicineDto.getPrice()).nameInEng(medicineDto.getNameInEng())
                .nameInArb(medicineDto.getNameInArb())
                .numberOfPastilleInEachBox(medicineDto.getNumberOfPastilleInEachBox())
                .activeSubstance(medicineDto.getActiveSubstance()).build();
        Medicine savedMedicine = medicineRepo.save(medicine);
        MedicineDto medicineDto1 = MedicineDto.builder().medicineStatus(MedicineStatus.UPDATED)
                .price(savedMedicine.getPrice()).nameInEng(savedMedicine.getNameInEng())
                .nameInArb(savedMedicine.getNameInArb())
                .numberOfPastilleInEachBox(savedMedicine.getNumberOfPastilleInEachBox())
                .activeSubstance(savedMedicine.getActiveSubstance()).build();
        return basisResponseMapper.createBasisResponse(medicineDto1);
    }

    @Override
    public BasisResponse update(Long id, MedicineDto medicineDto) {
        Medicine medicine = medicineRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Medicine", "Id", id));
        String activeSubstance = medicineDto.getActiveSubstance()==null ? "null" : medicineDto.getActiveSubstance();
        medicine.setMedicineStatus(MedicineStatus.UPDATED);
        medicine.setPrice(medicineDto.getPrice());
        medicine.setNameInEng(medicineDto.getNameInEng());
        medicine.setNameInArb(medicine.getNameInArb());
        medicine.setNumberOfPastilleInEachBox(medicineDto.getNumberOfPastilleInEachBox());
        medicine.setActiveSubstance(activeSubstance);
        Medicine savedMedicine = medicineRepo.save(medicine);
        MedicineDto medicineDto1 = MedicineDto.builder().medicineStatus(MedicineStatus.UPDATED)
                .price(savedMedicine.getPrice()).nameInEng(savedMedicine.getNameInEng())
                .nameInArb(savedMedicine.getNameInArb())
                .numberOfPastilleInEachBox(savedMedicine.getNumberOfPastilleInEachBox())
                .activeSubstance(savedMedicine.getActiveSubstance()).build();
        return basisResponseMapper.createBasisResponse(medicineDto1);
    }

    @Override
    public BasisResponse getMedicine(int pageNo, int pageSize, String sortBy, String filter) {
        // Create a PageRequest for pagination
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Medicine> medicinePage;

        if (filter != null) {
            // Apply pagination and filtering
            medicinePage = medicineRepo.findByNameInEngContaining(filter, pageRequest);
        } else {
            // Apply pagination without filtering
            medicinePage = medicineRepo.findAll(pageRequest);
        }

        List<MedicineDto> medicineDtoList = medicinePage.getContent().stream()
                .map(medicine -> MedicineDto.builder().id(medicine.getId())
                        .price(medicine.getPrice()).nameInEng(medicine.getNameInEng())
                        .nameInArb(medicine.getNameInArb())
                        .numberOfPastilleInEachBox(medicine.getNumberOfPastilleInEachBox())
                        .activeSubstance(medicine.getActiveSubstance())
                        .numberOfPatientTakeIt(medicine.getNumberOfPatientTakeIt())
                        .medicineStatus(medicine.getMedicineStatus())
                        .build())
                .toList();

        return basisResponseMapper.createBasisResponseForMedicine(medicineDtoList, pageNo, medicinePage);
    }

    @Override
    public BasisResponse getMedicineNameById(Long id) {
        Medicine medicine = medicineRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Medicine", "Id", id));
        MedicineDto medicineDto = MedicineDto.builder().id(id).nameInEng(medicine.getNameInEng())
                .nameInArb(medicine.getNameInArb()).medicineStatus(medicine.getMedicineStatus())
                .activeSubstance(medicine.getActiveSubstance()).price(medicine.getPrice())
                .numberOfPastilleInEachBox(medicine.getNumberOfPastilleInEachBox())
                .numberOfPatientTakeIt(medicine.getNumberOfPatientTakeIt()).build();
        return basisResponseMapper.createBasisResponse(medicineDto);
    }

    @Override
    public BasisResponse getPatientMedicine(Long id) {
        Medicine fist = medicineRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Medicine", "Id", id));
        List<Patient> patients = fist.getPatientMedicines().stream().map(Patient_Medicine::getPatient).toList();
        List<PatientDto> patientDtoList = patients.stream().map(patient -> PatientDto.builder()
                .name(patient.getName()).mobileNumbers(patient.getMobileNumbers())
                .id(patient.getId()).build()).toList();
        return basisResponseMapper.createBasisResponse(patientDtoList);
    }

    @Override
    public BasisResponse replaceMedicineWithAnother(Long firstId, Long secondId) {
        Medicine fist = medicineRepo.findById(firstId).orElseThrow(
                () -> new ResourceNotFoundException("Medicine", "Id", firstId));
        Medicine second = medicineRepo.findById(secondId).orElseThrow(
                () -> new ResourceNotFoundException("Medicine", "Id", secondId));
        for (Patient_Medicine patientMedicine : fist.getPatientMedicines()) {
            if (!cheakHaveTheSameMedicine(patientMedicine.getPatient(), second)) {
                patientMedicine.setMedicine(second);
                second.setNumberOfPatientTakeIt(second.getNumberOfPatientTakeIt() + 1);
                fist.setNumberOfPatientTakeIt(fist.getNumberOfPatientTakeIt() - 1);
                patientMedicineRepo.save(patientMedicine);
            } else {
                fist.setNumberOfPatientTakeIt(fist.getNumberOfPatientTakeIt() - 1);
                patientMedicineRepo.delete(patientMedicine);
            }
        }
        medicineRepo.save(fist);
        medicineRepo.save(second);
        MedicineDto medicineDto = MedicineDto.builder().price(second.getPrice()).nameInEng(second.getNameInEng())
                .nameInArb(second.getNameInArb()).numberOfPastilleInEachBox(second.getNumberOfPastilleInEachBox())
                .activeSubstance(second.getActiveSubstance())
                .numberOfPatientTakeIt(second.getNumberOfPatientTakeIt()).medicineStatus(second.getMedicineStatus())
                .build();
        return basisResponseMapper.createBasisResponse(medicineDto);
    }

    @Override
    public BasisResponse removeMedicineFromPatient(Long medicineId, Long patientId) {
        Patient patient = patientRepo.findById(patientId).orElseThrow(
                () -> new ResourceNotFoundException("Patient", "id", patientId));
        Medicine medicine = medicineRepo.findById(medicineId).orElseThrow(
                () -> new ResourceNotFoundException("Medicine", "id", medicineId));
        List<Patient_Medicine> patientMedicineCheck = patientMedicineRepo.findByPatientAndMedicine(patient, medicine);
        patientMedicineRepo.deleteAll(patientMedicineCheck);
        return basisResponseMapper.createBasisResponse("Medicine deleted form patient successfully");
    }

    @Override
    public ResponseEntity<Object> delete(Long id) {
        Medicine medicine = medicineRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Medicine", "Id", id));
        if (medicine == null)
            throw new NabdAPIExeption("no medicine with this name", HttpStatus.BAD_REQUEST);
        if (medicine.getPatientMedicines().size() > 0)
            throw new NabdAPIExeption("There are patients who take this medicine", HttpStatus.BAD_REQUEST);
        medicineRepo.delete(medicine);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public BasisResponse deactivatemedicine(Long medicineId, Long patientId) {
        Patient patient = patientRepo.findById(patientId).orElseThrow(
                ()-> new ResourceNotFoundException("Patient" , "id",patientId));
        Medicine medicine = medicineRepo.findById(medicineId).orElseThrow(
                ()-> new ResourceNotFoundException("Medicine" , "id",medicineId));
        List<Patient_Medicine> patientMedicineCheck= patientMedicineRepo.findByPatientAndMedicine(patient,medicine);
        if (patientMedicineCheck.isEmpty()){
            throw new NabdAPIExeption("No Medicine exist to this patient" , HttpStatus.BAD_REQUEST);
        }
        for (Patient_Medicine p : patientMedicineCheck){
            p.setActive(false);
        }
        patientMedicineRepo.saveAll(patientMedicineCheck);
        return basisResponseMapper.createBasisResponse("Medicine deactivate");
    }

    @Override
    public BasisResponse activatemedicine(Long medicineId, Long patientId) {
        Patient patient = patientRepo.findById(patientId).orElseThrow(
                ()-> new ResourceNotFoundException("Patient" , "id",patientId));
        Medicine medicine = medicineRepo.findById(medicineId).orElseThrow(
                ()-> new ResourceNotFoundException("Medicine" , "id",medicineId));
        List<Patient_Medicine> patientMedicineCheck= patientMedicineRepo.findByPatientAndMedicine(patient,medicine);
        if (patientMedicineCheck.isEmpty()){
            throw new NabdAPIExeption("No Medicine exist to this patient" , HttpStatus.BAD_REQUEST);
        }
        for (Patient_Medicine p : patientMedicineCheck){
            p.setActive(true);
        }
        patientMedicineRepo.saveAll(patientMedicineCheck);
        return basisResponseMapper.createBasisResponse("Medicine activate");
    }

    private boolean cheakHaveTheSameMedicine(Patient patient, Medicine medicine) {
        for (Patient_Medicine p : patient.getPatientMedicines()) {
            if (p.getMedicine().getNameInEng().equals(medicine.getNameInEng()))
                return true;
        }
        return false;
    }
}