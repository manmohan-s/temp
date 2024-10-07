package com.infy.springboot_assessment.service;


import com.infy.springboot_assessment.dto.EmployeeDTO;
import com.infy.springboot_assessment.entity.Employee;
import com.infy.springboot_assessment.exception.EmployeeAlreadyExistException;
import com.infy.springboot_assessment.exception.EmployeeDoNotExistException;
import com.infy.springboot_assessment.repo.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeService {
    private EmployeeRepository employeeRepository;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    public EmployeeDTO addEmployee(EmployeeDTO employeeDTO) throws EmployeeAlreadyExistException {
        if(employeeRepository.existsById(employeeDTO.getId())){
            logger.warn("Employee addition - Could not create Employee, given ID already exists: {}", employeeDTO.getId());
            throw new EmployeeAlreadyExistException(employeeDTO.getId());
        }
        Employee employee = Employee.createEntity(employeeDTO);
        logger.info("Employee addition - Entity prepared: {}", employee);
        Employee employeeCreated = employeeRepository.save(Employee.createEntity(employeeDTO));
        logger.info("Employee addition - Entity added: {}", employeeCreated);
        employeeDTO = Employee.createDTO(employeeCreated);
        return employeeDTO;
    }

    public List<EmployeeDTO> fetchAllEmployees(){
        logger.info("Employee fetch - Received request to fetch all employees");
        List<Employee> employees = employeeRepository.findAll();
        logger.info("Employee fetch - Employee entity list received: {}", employees);
        return employees.stream().map(Employee::createDTO).collect(Collectors.toList());
    }

    public EmployeeDTO fetchEmployeeByEmployeeId(long employeeId) throws EmployeeDoNotExistException {
        logger.info("Employee fetch - Received request to fetch employee with ID: {}",employeeId);
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        if(employeeOptional.isPresent()){
            logger.info("Employee fetch - Entity fetched: {}",employeeOptional.get());
        }
        else{
            logger.warn("Employee fetch - Could not find the Employee with given ID: {}", employeeId);
            throw new EmployeeDoNotExistException(employeeId);
        }
        return Employee.createDTO(employeeOptional.orElseThrow());
    }

    public void deleteEmployee(long employeeId) throws EmployeeDoNotExistException {
        logger.info("Employee deletion - Received request to delete employee with ID: {}",employeeId);
        if(! employeeRepository.existsById(employeeId)){
            logger.warn("Employee deletion - Could not find the Employee with given ID: {}", employeeId);
            throw new EmployeeDoNotExistException(employeeId);
        }
        employeeRepository.deleteById(employeeId);
        logger.info("Employee deletion - Employee with ID: {} successfully deleted ",employeeId);
    }

    public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO) throws EmployeeDoNotExistException {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeDTO.getId());
        if(employeeOptional.isEmpty()){
            logger.warn("Employee updation - Could not find the Employee with given ID: {}", employeeDTO.getId());
            throw new EmployeeDoNotExistException(employeeDTO.getId());
        }
        Employee employee = employeeOptional.get();
        logger.info("Employee updation - Entity fetched for updation: {}", employee);
        employee.setName(employeeDTO.getName());
        employee.setAge(employeeDTO.getAge());
        employee.setPhone(employeeDTO.getPhone());
        employee.setDepartmentCode(employeeDTO.getDepartmentCode());
        employee.setEmployeeType(employeeDTO.getEmployeeType());
        employee = employeeRepository.save(employee);
        logger.info("Employee updation - Entity updated: {}", employee);
        employeeDTO = Employee.createDTO(employee);
        return employeeDTO;
    }

}
