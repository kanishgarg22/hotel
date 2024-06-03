package com.ums.ums;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class A {
    public static void main(String[] args) {
        Employee e1 = new Employee();
        e1.setId(1);
        e1.setName("mike");
        Employee e2 = new Employee();
        e1.setId(2);
        e1.setName("sam");


        List<Employee> employees = new ArrayList<Employee>();
        employees.add(e1);
        employees.add(e2);

        List<EmployeeDto> dtos = employees.stream().map(e -> convertToDto(e)).collect(Collectors.toList());
        System.out.println(dtos);
    }
   static EmployeeDto convertToDto(Employee e){
        EmployeeDto dto = new EmployeeDto();
        dto.setId(e.getId());
        dto.setName(e.getName());
        return dto;
   }
}
