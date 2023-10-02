package com.example.nabd.service.imp;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.SpecializationDto;
import com.example.nabd.entity.Specialization;
import com.example.nabd.mapper.BasisResponseMapper;
import com.example.nabd.repository.SpecializationRepo;
import com.example.nabd.service.ISpecializationService;
import org.springframework.stereotype.Service;

@Service
public class SpecializationServiceImp implements ISpecializationService {
    private final SpecializationRepo specializationRepo;
    private final BasisResponseMapper basisResponseMapper = new BasisResponseMapper();


    public SpecializationServiceImp(SpecializationRepo specializationRepo) {
        this.specializationRepo = specializationRepo;
    }

    @Override
    public BasisResponse createSpecialization(SpecializationDto specializationDto) {
        Specialization specialization = new Specialization();
        specialization.setName(specializationDto.getName());
        Specialization specializationSaved = specializationRepo.save(specialization);
        return basisResponseMapper.createBasisResponse(specializationSaved);
    }
}
