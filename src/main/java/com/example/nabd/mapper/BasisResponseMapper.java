package com.example.nabd.mapper;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.entity.Locations;
import com.example.nabd.entity.Medicine;
import com.example.nabd.entity.Patient;
import com.example.nabd.entity.User;
import org.springframework.data.domain.Page;

import java.util.Date;

public class BasisResponseMapper {
    public BasisResponse createBasisResponseForUser(Object data , int pageNo , Page<User> pageList){
        return BasisResponse.builder()
                .data(data).pageNo(pageNo).pageSize(pageList.getSize())
                .totalElements(pageList.getTotalElements()).totalPage(pageList.getTotalPages())
                .last(pageList.isLast()).timestamp(new Date().toString()).status("Success").build();
    }
    public BasisResponse createBasisResponseForLocation(Object data , int pageNo , Page<Locations> pageList){
        return BasisResponse.builder()
                .data(data).pageNo(pageNo).pageSize(pageList.getSize())
                .totalElements(pageList.getTotalElements()).totalPage(pageList.getTotalPages())
                .last(pageList.isLast()).timestamp(new Date().toString()).status("Success").build();
    }
    public BasisResponse createBasisResponseForPatient(Object data , int pageNo , Page<Patient> pageList){
        return BasisResponse.builder()
                .data(data).pageNo(pageNo).pageSize(pageList.getSize())
                .totalElements(pageList.getTotalElements()).totalPage(pageList.getTotalPages())
                .last(pageList.isLast()).timestamp(new Date().toString()).status("Success").build();
    }
    public BasisResponse createBasisResponseForMedicine(Object data , int pageNo , Page<Medicine> pageList){
        return BasisResponse.builder()
                .data(data).pageNo(pageNo).pageSize(pageList.getSize())
                .totalElements(pageList.getTotalElements()).totalPage(pageList.getTotalPages())
                .last(pageList.isLast()).timestamp(new Date().toString()).status("Success").build();
    }
    public BasisResponse createBasisResponse(Object data){
        return BasisResponse.builder().data(data)
                .timestamp(new Date().toString()).status("Success").build();
    }
}
