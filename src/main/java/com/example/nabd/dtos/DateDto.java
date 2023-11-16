package com.example.nabd.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DateDto {
    List<Integer> year;
    List<Integer> month;
}
