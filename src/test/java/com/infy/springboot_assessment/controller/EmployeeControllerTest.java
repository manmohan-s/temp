package com.infy.springboot_assessment.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.infy.springboot_assessment.dto.EmployeeDTO;
import com.infy.springboot_assessment.entity.EmployeeType;
import com.infy.springboot_assessment.exception.EmployeeAlreadyExistException;
import com.infy.springboot_assessment.exception.EmployeeDoNotExistException;
import com.infy.springboot_assessment.service.EmployeeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmployeeController.class)
class EmployeeControllerTest {
    @MockBean
    private EmployeeService mockEmployeeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testCreateEmployee_whenValidEmployeeDTOReceived_shouldCreateEmployeeAndReturnDTO() throws Exception {
        EmployeeDTO inputEmployeeDTO = prepareEmployeeDTO();
        MvcResult mvcResult = null;
        given:{
            Assertions.assertNotNull(mockMvc);
            Assertions.assertNotNull(objectMapper);
            Assertions.assertNotNull(inputEmployeeDTO);
            Assertions.assertNull(mvcResult);
        }
        when:{
            EmployeeDTO mockEmployeeDTOByService = prepareEmployeeDTO();
            mockEmployeeDTOByService.setUnimportantVariable(null); //since this variable is not persisted in DB
            Mockito.when(mockEmployeeService.addEmployee(any(EmployeeDTO.class))).thenReturn(mockEmployeeDTOByService);

            //below method converts JavaObject to Json string
            String inputEmployeeDTOJson = objectMapper.writeValueAsString(inputEmployeeDTO);
            Assertions.assertNotNull(inputEmployeeDTOJson);
            Assertions.assertFalse(inputEmployeeDTOJson.isEmpty());
            System.out.println(inputEmployeeDTOJson);
            mvcResult = mockMvc.perform(post("/employee")
                    .contentType("application/json")
                    .content(inputEmployeeDTOJson))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is((int) inputEmployeeDTO.getId())))
                    .andExpect(jsonPath("$.name", is(inputEmployeeDTO.getName())))
                    .andExpect(jsonPath("$.age", is(inputEmployeeDTO.getAge())))
                    .andExpect(jsonPath("$.phone", is(inputEmployeeDTO.getPhone())))
                    .andExpect(jsonPath("$.departmentCode", is(inputEmployeeDTO.getDepartmentCode())))
                    .andReturn();
        }
        then:{
            //Expect statements worked as usual, can use responseString, employeeDTOFromResponse for further validations/ response
            String responseString = mvcResult.getResponse().getContentAsString();
            EmployeeDTO employeeDTOFromResponse = objectMapper.readValue(responseString, EmployeeDTO.class);
            assertEquals(inputEmployeeDTO.getId(), employeeDTOFromResponse.getId());
            System.out.println(employeeDTOFromResponse);
        }

    }

    @Test
    void testCreateEmployee_whenInvalidEmployeeDTOReceived_shouldReturnErrorAndStatus() throws Exception {
        EmployeeDTO inputEmployeeDTO = prepareEmployeeDTO();
        MvcResult mvcResult = null;
        given:{
            Assertions.assertNotNull(mockMvc);
            Assertions.assertNotNull(objectMapper);
            Assertions.assertNotNull(inputEmployeeDTO);
            Assertions.assertNull(mvcResult);
        }
        when:{
            EmployeeDTO mockEmployeeDTOByService = prepareEmployeeDTO();
            mockEmployeeDTOByService.setUnimportantVariable(null); //since this variable is not persisted in DB
            Mockito.when(mockEmployeeService.addEmployee(any(EmployeeDTO.class))).thenThrow(new EmployeeAlreadyExistException(inputEmployeeDTO.getId()));

            //below method converts JavaObject to Json string
            String inputEmployeeDTOJson = objectMapper.writeValueAsString(inputEmployeeDTO);
            Assertions.assertNotNull(inputEmployeeDTOJson);
            Assertions.assertFalse(inputEmployeeDTOJson.isEmpty());
            System.out.println(inputEmployeeDTOJson);
            mvcResult = mockMvc.perform(post("/employee")
                            .contentType("application/json")
                            .content(inputEmployeeDTOJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode", is(409)))
                    .andExpect(jsonPath("$.errorMessage", is("Employee already exists in our system for id: "+inputEmployeeDTO.getId())))
                    .andReturn();
        }
        then:{
            //Expect statements worked as usual, can use responseString, employeeDTOFromResponse for further validations/ response
            String responseString = mvcResult.getResponse().getContentAsString();
            System.out.println(responseString);
        }

    }

    @Test
    void testFetchAllEmployees_whenRequestReceived_ShouldReturnAllEmployees() throws Exception {
        List<EmployeeDTO> outputEmployeeDTOs = new ArrayList<>();
        MvcResult mvcResult = null;
        given:{
            Assertions.assertNotNull(mockMvc);
            Assertions.assertNotNull(objectMapper);
            Assertions.assertNotNull(outputEmployeeDTOs);
            Assertions.assertNull(mvcResult);
        }
        when:{
            outputEmployeeDTOs.add(new EmployeeDTO(1, "EmployeeName1", 21, "9191919191", "CS", null, EmployeeType.FULLTIME));
            outputEmployeeDTOs.add(new EmployeeDTO(2, "EmployeeName2", 22, "9191919191", "CS", null, EmployeeType.PARTTIME));
            outputEmployeeDTOs.add(new EmployeeDTO(3, "EmployeeName3", 23, "9191919191", "CS", null, EmployeeType.FULLTIME));
            Mockito.when(mockEmployeeService.fetchAllEmployees()).thenReturn(outputEmployeeDTOs);

            mvcResult = mockMvc.perform(get("/employee"))
                    .andExpect(status().isOk())
                    .andReturn();
        }
        then:{
            //Expect statements worked as usual, can use responseString, employeeDTOFromResponse for further validations/ response
            String responseString = mvcResult.getResponse().getContentAsString();
            List<EmployeeDTO> outputList = objectMapper.readValue(responseString, new TypeReference<>(){});
            assertEquals(3, outputList.size());
            System.out.println(responseString);
            System.out.println(outputList);
        }
    }

    @Test
    void testFetchEmployeeUsingPathVariable_whenRequestReceived_ShouldReturnMatchingEmployee() throws Exception {
        EmployeeDTO inputEmployeeDTO = prepareEmployeeDTO();
        MvcResult mvcResult = null;
        given:{
            Assertions.assertNotNull(mockMvc);
            Assertions.assertNotNull(objectMapper);
            Assertions.assertNotNull(inputEmployeeDTO);
            Assertions.assertNull(mvcResult);
        }
        when:{
            EmployeeDTO mockEmployeeDTOByService = prepareEmployeeDTO();
            mockEmployeeDTOByService.setUnimportantVariable(null); //since this variable is not persisted in DB
            Mockito.when(mockEmployeeService.fetchEmployeeByEmployeeId(inputEmployeeDTO.getId())).thenReturn(mockEmployeeDTOByService);

            mvcResult = mockMvc.perform(get("/employee/"+inputEmployeeDTO.getId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is((int) mockEmployeeDTOByService.getId())))
                    .andExpect(jsonPath("$.name", is(mockEmployeeDTOByService.getName())))
                    .andExpect(jsonPath("$.age", is(mockEmployeeDTOByService.getAge())))
                    .andExpect(jsonPath("$.phone", is(mockEmployeeDTOByService.getPhone())))
                    .andExpect(jsonPath("$.departmentCode", is(mockEmployeeDTOByService.getDepartmentCode())))
                    .andReturn();
        }
        then:{
            //Expect statements worked as usual, can use responseString, employeeDTOFromResponse for further validations/ response
            String responseString = mvcResult.getResponse().getContentAsString();
            EmployeeDTO employeeDTOFromResponse = objectMapper.readValue(responseString, EmployeeDTO.class);
            assertEquals(inputEmployeeDTO.getId(), employeeDTOFromResponse.getId());
            System.out.println(employeeDTOFromResponse);
        }
    }

    @Test
    void testDeleteEmployeeUsingPathVariable_whenValidEmpIDReceived_ShouldReturnSuccessMsg() throws Exception {
        long inputEmployeeID = 1L;
        MvcResult mvcResult = null;
        given:{
            Assertions.assertNotNull(mockMvc);
            Assertions.assertNotNull(objectMapper);
            Assertions.assertNull(mvcResult);
        }
        when:{
            Mockito.doNothing().when(mockEmployeeService).deleteEmployee(inputEmployeeID);

            mvcResult = mockMvc.perform(delete("/employee/"+inputEmployeeID))
                    .andExpect(status().isAccepted())
                    .andReturn();
        }
        then:{
            //Expect statements worked as usual, can use responseString, employeeDTOFromResponse for further validations/ response
            String responseString = mvcResult.getResponse().getContentAsString();
            assertEquals("Employee with id: "+inputEmployeeID+" successfully deleted", responseString);
            System.out.println(responseString);
        }
    }

    @Test
    void testDeleteEmployeeUsingPathVariable_whenInvalidEmpIDReceived_ShouldReturnErrorMsg() throws Exception {
        long inputEmployeeID = 1L;
        MvcResult mvcResult = null;
        given:{
            Assertions.assertNotNull(mockMvc);
            Assertions.assertNotNull(objectMapper);
            Assertions.assertNull(mvcResult);
        }
        when:{
            Mockito.doThrow(new EmployeeDoNotExistException(inputEmployeeID)).when(mockEmployeeService).deleteEmployee(inputEmployeeID);

            mvcResult = mockMvc.perform(delete("/employee/"+inputEmployeeID))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode", is(HttpStatus.NOT_FOUND.value())))
                    .andExpect(jsonPath("$.errorMessage", is("Employee do not exists in our system for id: "+inputEmployeeID)))
                    .andReturn();
        }
        then:{
            //Expect statements worked as usual, can use responseString, employeeDTOFromResponse for further validations/ response
            String responseString = mvcResult.getResponse().getContentAsString();
            System.out.println(responseString);
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
}
