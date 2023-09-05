package com.example.nabd.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasisResponse {
    private Object data;
    private Date timestamp;
    private String status;
    private Integer pageNo;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPage;
    private Boolean last;
    private String amr;

}
