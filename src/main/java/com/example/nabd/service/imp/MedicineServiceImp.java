package com.example.nabd.service.imp;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.MedicineDto;
import com.example.nabd.dtos.UserDto;
import com.example.nabd.entity.Medicine;
import com.example.nabd.entity.User;
import com.example.nabd.enums.MedicineStatus;
import com.example.nabd.enums.Roles;
import com.example.nabd.mapper.BasisResponseMapper;
import com.example.nabd.repository.MedicineRepo;
import com.example.nabd.service.IMedicineService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicineServiceImp implements IMedicineService {
    private final MedicineRepo medicineRepo;
    private final BasisResponseMapper basisResponseMapper = new BasisResponseMapper();


    public MedicineServiceImp(MedicineRepo medicineRepo) {
        this.medicineRepo = medicineRepo;
    }

    @Override
    public BasisResponse create(MedicineDto medicineDto) {
        Medicine medicine = Medicine.builder().medicineStatus(MedicineStatus.Updated)
                .price(medicineDto.getPrice()).nameInEng(medicineDto.getNameInEng())
                .nameInArb(medicineDto.getNameInArb()).numberOfPastilleInEachBox(medicineDto.getNumberOfPastilleInEachBox())
                .activeSubstance(medicineDto.getActiveSubstance()).build();
        Medicine savedMedicine = medicineRepo.save(medicine);
        MedicineDto medicineDto1 =MedicineDto.builder().medicineStatus(MedicineStatus.Updated)
                .price(savedMedicine.getPrice()).nameInEng(savedMedicine.getNameInEng())
                .nameInArb(savedMedicine.getNameInArb()).numberOfPastilleInEachBox(savedMedicine.getNumberOfPastilleInEachBox())
                .activeSubstance(savedMedicine.getActiveSubstance()).build();
        return basisResponseMapper.createBasisResponse(medicineDto1);
    }

    @Override
    public BasisResponse getMedicine(int pageNo, int pageSize, String sortBy, String filter) {
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);
        Page<Medicine> medicines = medicineRepo.findAll(pageable);
        List<Medicine> medicineslist = medicines.getContent();
        if (filter!=null){
            List<Medicine> medicineFilterList = medicineslist.stream().filter(medicine ->
                    medicine.getNameInEng().startsWith(filter)).toList();
            List<MedicineDto> medicineDtoList = medicineFilterList.stream().map(medicine -> MedicineDto.builder()
                    .price(medicine.getPrice()).nameInEng(medicine.getNameInEng())
                    .nameInArb(medicine.getNameInArb()).numberOfPastilleInEachBox(medicine.getNumberOfPastilleInEachBox())
                    .activeSubstance(medicine.getActiveSubstance())
                    .numberOfPatientTakeIt(medicine.getNumberOfPatientTakeIt()).medicineStatus(medicine.getMedicineStatus())
                    .build()).toList();
            return basisResponseMapper.createBasisResponse(medicineDtoList);
        }
        List<MedicineDto> medicineDtoList = medicineslist.stream().map(medicine -> MedicineDto.builder().
                price(medicine.getPrice()).nameInEng(medicine.getNameInEng())
                .nameInArb(medicine.getNameInArb()).numberOfPastilleInEachBox(medicine.getNumberOfPastilleInEachBox())
                .activeSubstance(medicine.getActiveSubstance())
                .numberOfPatientTakeIt(medicine.getNumberOfPatientTakeIt()).medicineStatus(medicine.getMedicineStatus())
                .build()).toList();
        return basisResponseMapper.createBasisResponseForMedicine(medicineDtoList,pageNo,medicines);
    }

    @Override
    public String delete(String name) {
        Medicine medicine = medicineRepo.findByNameInEng(name);
        medicineRepo.delete(medicine);
        return "medicine delete successfully";
    }
}