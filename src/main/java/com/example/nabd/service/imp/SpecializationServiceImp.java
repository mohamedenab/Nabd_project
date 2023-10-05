package com.example.nabd.service.imp;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.SpecializationDto;
import com.example.nabd.entity.Specialization;
import com.example.nabd.exception.ResourceNotFoundException;
import com.example.nabd.mapper.BasisResponseMapper;
import com.example.nabd.repository.SpecializationRepo;
import com.example.nabd.service.ISpecializationService;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public BasisResponse getSpecializations() {
        List<Specialization> specializationList =specializationRepo.findAll();
        List<SpecializationDto> specializationDtoList = specializationList.stream().map(specialization ->
                SpecializationDto.builder().name(specialization.getName()).id(specialization.getId()).build()).toList();
        return basisResponseMapper.createBasisResponse(specializationDtoList);
    }

    @Override
    public BasisResponse updateSpecialization(Long id, SpecializationDto specializationDto) {
        Specialization specialization = specializationRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Specialization","id",id));
        specialization.setName(specializationDto.getName());
        Specialization specializationSaved = specializationRepo.save(specialization);
        specializationDto.setName(specializationSaved.getName());
        specializationDto.setId(specializationSaved.getId());
        return basisResponseMapper.createBasisResponse(specializationDto);
    }

    @Override
    public String deleteSpecialization(Long id) {
        Specialization specialization = specializationRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Specialization","id",id));
        specializationRepo.delete(specialization);
        return "Specialization delete successfully";
    }
}
