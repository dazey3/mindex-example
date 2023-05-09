package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String employeeReportUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        employeeReportUrl = "http://localhost:" + port + "/employee/report/{id}";
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

    @Test
    public void testReportingStructure() {
        Employee testEmployee1 = new Employee("Chris", "Doe", "Mindex", "Developer", null);
        Employee testEmployee2 = new Employee("Matt", "Doe", "Mindex", "Developer", null);
        Employee testEmployee3 = new Employee("Ringo", "Doe", "Mindex", "Team Lead", null);
        Employee testEmployee4 = new Employee("Paul", "Doe", "Mindex", "Manager", null);
        Employee testEmployee5 = new Employee("George", "Doe", "Mindex", "Sales", null);
        Employee testEmployee6 = new Employee("Paul", "Doe", "Mindex", "Sales Manager", null);
        Employee testEmployee7 = new Employee("John", "Doe", "Mindex", "Regional Manager", null);
        Employee testEmployee8 = new Employee("Steven", "Doe", "Mindex", "Janitor", null);

        // Create checks
        Employee createdEmployee1 = restTemplate.postForEntity(employeeUrl, testEmployee1, Employee.class).getBody();
        Employee createdEmployee2 = restTemplate.postForEntity(employeeUrl, testEmployee2, Employee.class).getBody();
        testEmployee3.setDirectReports(Arrays.asList(createdEmployee1, createdEmployee2));
        Employee createdEmployee3 = restTemplate.postForEntity(employeeUrl, testEmployee3, Employee.class).getBody();

        testEmployee4.setDirectReports(Arrays.asList(createdEmployee3));
        Employee createdEmployee4 = restTemplate.postForEntity(employeeUrl, testEmployee4, Employee.class).getBody();

        Employee createdEmployee5 = restTemplate.postForEntity(employeeUrl, testEmployee5, Employee.class).getBody();
        testEmployee6.setDirectReports(Arrays.asList(createdEmployee5));
        Employee createdEmployee6 = restTemplate.postForEntity(employeeUrl, testEmployee6, Employee.class).getBody();

        testEmployee7.setDirectReports(Arrays.asList(createdEmployee4, createdEmployee6));
        Employee createdEmployee7 = restTemplate.postForEntity(employeeUrl, testEmployee7, Employee.class).getBody();
        Employee createdEmployee8 = restTemplate.postForEntity(employeeUrl, testEmployee8, Employee.class).getBody();

        // Read Report checks
        ReportingStructure readReportingStructure1 = restTemplate.getForEntity(employeeReportUrl, ReportingStructure.class, createdEmployee4.getEmployeeId()).getBody();
        assertEmployeeEquivalence(createdEmployee4, readReportingStructure1.getEmployee());
        assertEquals(3, readReportingStructure1.getNumberOfReports());

        ReportingStructure readReportingStructure2 = restTemplate.getForEntity(employeeReportUrl, ReportingStructure.class, createdEmployee7.getEmployeeId()).getBody();
        assertEmployeeEquivalence(createdEmployee7, readReportingStructure2.getEmployee());
        assertEquals(6, readReportingStructure2.getNumberOfReports());
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
