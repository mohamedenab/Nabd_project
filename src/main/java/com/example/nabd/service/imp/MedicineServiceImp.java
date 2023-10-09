package com.example.nabd.service.imp;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.MedicineDto;
import com.example.nabd.entity.Medicine;
import com.example.nabd.enums.MedicineStatus;
import com.example.nabd.mapper.BasisResponseMapper;
import com.example.nabd.repository.MedicineRepo;
import com.example.nabd.service.IMedicineService;
import org.springframework.stereotype.Service;

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
    public String delete(String name) {
        Medicine medicine = medicineRepo.findByNameInEng(name);
        medicineRepo.delete(medicine);
        return "medicine delete successfully";
    }
}