package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.CompensationService;
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
public class CompensationServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String employeeReportUrl;

    private String employeeCompensationUrl;
    private String employeeIdCompensationUrl;

    @Autowired
    private CompensationService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        employeeReportUrl = "http://localhost:" + port + "/employee/report/{id}";

        employeeCompensationUrl = "http://localhost:" + port + "/compensation";
        employeeIdCompensationUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void testCreateReadCompensation() throws ParseException {
        Employee testEmployee1 = new Employee("Chris", "Doe", "Mindex", "Developer", null);
        Employee testEmployee2 = new Employee("Matt", "Doe", "Mindex", "Developer", null);
        Employee testEmployee3 = new Employee("Will", "Doe", "Mindex", "Team Lead", null);
        Employee testEmployee4 = new Employee("Paul", "Doe", "Mindex", "Manager", null);

        // Create employees
        Employee createdEmployee1 = restTemplate.postForEntity(employeeUrl, testEmployee1, Employee.class).getBody();
        Employee createdEmployee2 = restTemplate.postForEntity(employeeUrl, testEmployee2, Employee.class).getBody();
        Employee createdEmployee3 = restTemplate.postForEntity(employeeUrl, testEmployee3, Employee.class).getBody();
        Employee createdEmployee4 = restTemplate.postForEntity(employeeUrl, testEmployee4, Employee.class).getBody();

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        Compensation testCompensation1 = new Compensation(createdEmployee1, 100000, formatter.parse("09-03-2018"));
        Compensation testCompensation2 = new Compensation(createdEmployee2, 110000, formatter.parse("02-10-2022"));
        Compensation testCompensation3 = new Compensation(createdEmployee3, 140000, formatter.parse("29-05-2023"));
        Compensation testCompensation4 = new Compensation(createdEmployee4, 160000, formatter.parse("23-06-2015"));

        // Create compensations
        Compensation createdCompensation1 = restTemplate.postForEntity(employeeCompensationUrl, testCompensation1, Compensation.class).getBody();
        Compensation createdCompensation2 = restTemplate.postForEntity(employeeCompensationUrl, testCompensation2, Compensation.class).getBody();
        Compensation createdCompensation3 = restTemplate.postForEntity(employeeCompensationUrl, testCompensation3, Compensation.class).getBody();
        Compensation createdCompensation4 = restTemplate.postForEntity(employeeCompensationUrl, testCompensation4, Compensation.class).getBody();

        // Create checks
        assertNotNull(createdCompensation1.getEmployee());
        assertEmployeeEquivalence(createdEmployee1, createdCompensation1.getEmployee());
        assertNotNull(createdCompensation2.getEmployee());
        assertEmployeeEquivalence(createdEmployee2, createdCompensation2.getEmployee());
        assertNotNull(createdCompensation3.getEmployee());
        assertEmployeeEquivalence(createdEmployee3, createdCompensation3.getEmployee());
        assertNotNull(createdCompensation4.getEmployee());
        assertEmployeeEquivalence(createdEmployee4, createdCompensation4.getEmployee());

        // Read checks
        Compensation readCompensation = restTemplate.getForEntity(employeeIdCompensationUrl, Compensation.class, createdCompensation3.getEmployee().getEmployeeId()).getBody();
        assertEmployeeEquivalence(createdCompensation3.getEmployee(), readCompensation.getEmployee());
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
