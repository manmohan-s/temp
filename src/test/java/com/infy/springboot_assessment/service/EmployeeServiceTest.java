package com.infy.springboot_assessment.service;


import com.infy.springboot_assessment.dto.EmployeeDTO;
import com.infy.springboot_assessment.entity.Employee;
import com.infy.springboot_assessment.entity.EmployeeType;
import com.infy.springboot_assessment.exception.EmployeeAlreadyExistException;
import com.infy.springboot_assessment.exception.EmployeeDoNotExistException;
import com.infy.springboot_assessment.repo.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository mockEmployeeRepository;

    private EmployeeService employeeService;

    @BeforeEach
    void init(){
        employeeService = new EmployeeService(mockEmployeeRepository);
    }

    @Test
    void testAddEmployee_whenNewEmployeeDTOReceived_shouldMakeCallToRepositoryToSave() throws EmployeeAlreadyExistException {
        EmployeeDTO employeeDTO;
        Employee employee;
        EmployeeDTO employeeDTOCreated;
        given:{
            employeeDTO = prepareEmployeeDTO();
            employee = prepareEmployee();
            Assertions.assertNotNull(employeeService);
            Assertions.assertNotNull(employeeDTO);
            Assertions.assertNotNull(employee);

        }
        when:{
            Mockito.when(mockEmployeeRepository.existsById(any(Long.class))).thenReturn(false);
            Mockito.when(mockEmployeeRepository.save(any(Employee.class))).thenReturn(employee);
            employeeDTOCreated = employeeService.addEmployee(employeeDTO);
        }
        then:{
            Assertions.assertNotNull(employeeDTOCreated);
            Assertions.assertEquals( employeeDTO.getId(), employeeDTOCreated.getId());
            Assertions.assertEquals( employeeDTO.getName(), employeeDTOCreated.getName());
            Assertions.assertEquals( employeeDTO.getAge(), employeeDTOCreated.getAge());
        }
    }

    @Test
    void testAddEmployee_whenExistingEmployeeDTOReceived_shouldThrowCustomException() {
        EmployeeDTO employeeDTO;
        Employee employee;
        EmployeeAlreadyExistException employeeAlreadyExistException;
        given:{
            employeeDTO = prepareEmployeeDTO();
            employee = prepareEmployee();
            Assertions.assertNotNull(employeeService);
            Assertions.assertNotNull(employeeDTO);
            Assertions.assertNotNull(employee);

        }
        when:{
            Mockito.when(mockEmployeeRepository.existsById(any(Long.class))).thenReturn(true);
            employeeAlreadyExistException =  assertThrows(EmployeeAlreadyExistException.class, () -> employeeService.addEmployee(employeeDTO));
        }
        then:{
            Assertions.assertNotNull(employeeAlreadyExistException);
            Assertions.assertEquals( "Employee already exists in our system for id: 1", employeeAlreadyExistException.getMessage());
        }
    }

    @Test
    void testFetchAllEmployees_whenFetchRequestReceived_shouldReturnEmployeeList() {
        List<EmployeeDTO> outputEmployeeDTOList = new ArrayList<>();
        List<Employee> employeeList = new ArrayList<>();
        given:{
            employeeList.add(prepareEmployee());
            Employee employee = prepareEmployee();
            employee.setId(2);
            employeeList.add(employee);
            Assertions.assertNotNull(employeeService);
            Assertions.assertNotEquals(0, employeeList.size());
        }
        when:{
            Mockito.when(mockEmployeeRepository.findAll()).thenReturn(employeeList);
            outputEmployeeDTOList = employeeService.fetchAllEmployees();
        }
        then:{
            Assertions.assertEquals(2, outputEmployeeDTOList.size());
            List<Long> employeeIds = outputEmployeeDTOList.stream().map(EmployeeDTO::getId).toList();
            Assertions.assertTrue(employeeIds.containsAll(List.of(1L,2L)));
        }
    }


    @Test
    void testFetchEmployeeByEmployeeId_whenEmployeeIdExists_shouldReturnEmployeeDTO() throws EmployeeDoNotExistException {
        Long inputEmployeeId = 1L;
        Employee employee;
        EmployeeDTO returnedEmployeeDTO = null;
        given:{
            employee = prepareEmployee();
            Assertions.assertNotNull(employeeService);
            Assertions.assertNotNull(inputEmployeeId);
            Assertions.assertNotNull(employee);
            Assertions.assertNull(returnedEmployeeDTO);

        }
        when:{
            Mockito.when(mockEmployeeRepository.findById(inputEmployeeId)).thenReturn(Optional.of(employee));
            returnedEmployeeDTO = employeeService.fetchEmployeeByEmployeeId(inputEmployeeId);
        }
        then:{
            Assertions.assertNotNull(returnedEmployeeDTO);
            Assertions.assertEquals(inputEmployeeId, returnedEmployeeDTO.getId());
        }
    }

    @Test
    void testFetchEmployeeByEmployeeId_whenEmployeeIdDoNotExist_shouldThrowCustomError() throws EmployeeDoNotExistException {
        Long inputEmployeeId = 9999L;
        Employee employee;
        EmployeeDoNotExistException exceptionThrown;
        given:{
            employee = prepareEmployee();
            Assertions.assertNotNull(employeeService);
            Assertions.assertNotNull(inputEmployeeId);
            Assertions.assertNotNull(employee);

        }
        when:{
            Mockito.when(mockEmployeeRepository.findById(inputEmployeeId)).thenReturn(Optional.empty());
            exceptionThrown = Assertions.assertThrows(EmployeeDoNotExistException.class, () -> employeeService.fetchEmployeeByEmployeeId(inputEmployeeId));

        }
        then:{
            Assertions.assertNotNull(exceptionThrown);
            Assertions.assertEquals("Employee do not exists in our system for id: "+inputEmployeeId, exceptionThrown.getMessage());
        }
    }



    @Test
    void testDeleteEmployee_whenEmployeeIdExists_shouldDeleteEmployee() throws EmployeeDoNotExistException {
        Long inputEmployeeId = 1L;
        given:{
            Assertions.assertNotNull(employeeService);
            Assertions.assertNotNull(inputEmployeeId);

        }
        when:{
            Mockito.when(mockEmployeeRepository.existsById(inputEmployeeId)).thenReturn(true);
            Mockito.doNothing().when(mockEmployeeRepository).deleteById(inputEmployeeId);
            employeeService.deleteEmployee(inputEmployeeId);
        }
        then:{
            Mockito.verify(mockEmployeeRepository, Mockito.times(1)).deleteById(inputEmployeeId);
        }
    }

    @Test
    void testDeleteEmployee_whenEmployeeIdDoNotExist_shouldThrowCustomError() throws EmployeeDoNotExistException {
        Long inputEmployeeId = 1L;
        given:{
            Assertions.assertNotNull(employeeService);
            Assertions.assertNotNull(inputEmployeeId);

        }
        when:{
            Mockito.when(mockEmployeeRepository.existsById(inputEmployeeId)).thenReturn(false);
            assertThrows(EmployeeDoNotExistException.class, () -> employeeService.deleteEmployee(inputEmployeeId));
        }
        then:{
            Mockito.verify(mockEmployeeRepository, Mockito.times(0)).deleteById(inputEmployeeId);
        }
    }

    private EmployeeDTO prepareEmployeeDTO(){
        return new EmployeeDTO(
                1,
                "EmployeeName",
                21,
                "9191919191",
                "CS",
                "blah",
                EmployeeType.FULLTIME);
    }

    private Employee prepareEmployee(){
        return Employee.createEntity(prepareEmployeeDTO());
    }
}
