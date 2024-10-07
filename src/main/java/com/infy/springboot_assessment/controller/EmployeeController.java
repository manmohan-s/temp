package com.infy.springboot_assessment.controller;

import com.infy.springboot_assessment.dto.EmployeeDTO;
import com.infy.springboot_assessment.exception.EmployeeAlreadyExistException;
import com.infy.springboot_assessment.exception.EmployeeDoNotExistException;
import com.infy.springboot_assessment.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
@AllArgsConstructor
public class EmployeeController {

    private EmployeeService employeeService;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);


    @PostMapping(consumes = "application/json")
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) throws EmployeeAlreadyExistException {
        logger.info("Employee addition - DTO received: {}", employeeDTO);
        EmployeeDTO employeeDTOCreated = employeeService.addEmployee(employeeDTO);
        logger.info("Employee addition - DTO created : {}", employeeDTOCreated);
        return new ResponseEntity<>(employeeDTOCreated, HttpStatus.CREATED);
    }


    @GetMapping(produces = "application/json")
    public ResponseEntity<List<EmployeeDTO>> fetchAllEmployees(){
        logger.info("Employee fetch - Received request to fetch all employees");
        List<EmployeeDTO> employeeDTOList  = employeeService.fetchAllEmployees();
        logger.info("Employee fetch - Employee DTO list received: {}", employeeDTOList);
        return ResponseEntity.ok(employeeDTOList);
    }


    @GetMapping("/{cId}")
    public ResponseEntity<EmployeeDTO> fetchEmployeeUsingPathVariable(@PathVariable("cId") long employeeId) throws EmployeeDoNotExistException {
        logger.info("Employee fetch - Received request to fetch employee with ID: {}",employeeId);
        EmployeeDTO employeeDTO = employeeService.fetchEmployeeByEmployeeId(employeeId);
        logger.info("Employee fetch - DTO fetched: {}",employeeDTO);
        return ResponseEntity.ok(employeeDTO);

    }

    @DeleteMapping("/{cId}")
    public ResponseEntity<String> deleteEmployeeUsingPathVariable(@PathVariable("cId") long employeeId) throws EmployeeDoNotExistException {
        logger.info("Employee delete - Received request to delete employee with ID: {}",employeeId);
        employeeService.deleteEmployee(employeeId);
        logger.info("Employee deletion - Employee with ID: {} successfully deleted ",employeeId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(String.format("Employee with id: %d successfully deleted", employeeId));
    }


    @PutMapping
    public ResponseEntity<EmployeeDTO> updateEmployee(@RequestBody EmployeeDTO employeeDTO) throws EmployeeDoNotExistException {
        logger.info("Employee updation - DTO received: {}", employeeDTO);
        employeeDTO = employeeService.updateEmployee(employeeDTO);
        logger.info("Employee updation - DTO updated: {}", employeeDTO);
        return ResponseEntity.accepted().body(employeeDTO);
    }


}
