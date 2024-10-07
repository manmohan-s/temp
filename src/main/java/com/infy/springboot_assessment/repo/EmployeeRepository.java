package com.infy.springboot_assessment.repo;

import com.infy.springboot_assessment.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
