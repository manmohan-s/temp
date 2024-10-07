package com.infy.springboot_assessment.dto;

import com.infy.springboot_assessment.entity.EmployeeType;
import lombok.ToString;

@ToString
public class EmployeeDTO {

    private long id;
    private String name;
    private int age;
    private String phone;
    private String departmentCode;
    private String unimportantVariable;
    private EmployeeType employeeType;

    public EmployeeDTO() {
    }

    public EmployeeDTO(long id, String name, int age, String phone, String departmentCode, String unimportantVariable, EmployeeType employeeType) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.departmentCode = departmentCode;
        this.unimportantVariable = unimportantVariable;
        this.employeeType = employeeType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getUnimportantVariable() {
        return unimportantVariable;
    }

    public void setUnimportantVariable(String unimportantVariable) {
        this.unimportantVariable = unimportantVariable;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

}
