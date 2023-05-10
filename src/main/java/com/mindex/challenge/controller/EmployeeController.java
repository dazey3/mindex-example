package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public Employee create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return employeeService.create(employee);
    }

    @GetMapping("/{id}")
    public Employee read(@PathVariable String id) {
        LOG.debug("Received employee read request for id [{}]", id);

        return employeeService.read(id);
    }

    @GetMapping("/compensation/{id}")
    public Compensation readCompensation(@PathVariable String id) {
        LOG.debug("Received compensation read request for employee with id [{}]", id);

        return employeeService.readCompensation(id);
    }

    @PutMapping("/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee update request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }

    @PutMapping("/compensation/{id}")
    public Compensation updateCompensation(@PathVariable String id, @RequestBody Compensation compensation) {
        LOG.debug("Received compensation [{}] update request for id [{}]", compensation, id);

        return employeeService.update(id, compensation);
    }

    // Reporting

    @GetMapping("/report/{id}")
    public ReportingStructure report(@PathVariable String id) {
        LOG.debug("Received employee report request for id [{}]", id);

        return employeeService.report(id);
    }
}
