package com.infy.springboot_assessment.entity;
import com.infy.springboot_assessment.dto.EmployeeDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Employee {
    @Id
    private long id;
    @Column
    private String name;
    @Column
    private int age;
    @Column(name = "phone_no")
    private String phone;
    @Column
    private String departmentCode;
    @Transient //this property is not saved in database
    private String unimportantVariable;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private EmployeeType employeeType;

    public static Employee createEntity(EmployeeDTO employeeDTO){
        return new Employee(employeeDTO.getId(), employeeDTO.getName(), employeeDTO.getAge(), employeeDTO.getPhone(), employeeDTO.getDepartmentCode(), employeeDTO.getUnimportantVariable(), employeeDTO.getEmployeeType());
    }

    public static EmployeeDTO createDTO(Employee employee){
        return new EmployeeDTO(employee.getId(), employee.getName(), employee.getAge(), employee.getPhone(),
                employee.getDepartmentCode(), employee.getUnimportantVariable(), employee.getEmployeeType());
    }
}
