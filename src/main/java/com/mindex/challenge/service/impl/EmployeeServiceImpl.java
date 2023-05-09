package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        employee.setEmployeeId(UUID.randomUUID().toString());

        LOG.debug("Creating employee [{}]", employee);
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Reading employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    // Reporting

    @Override
    public ReportingStructure report(String id) {
        LOG.debug("Building report for employee with id [{}]", id);

        Employee employee = read(id);
        Set<String> reports = new HashSet<String>();
        numberOfReports(employee, reports);

        return new ReportingStructure(employee, reports.size());
    }

    private void numberOfReports(Employee employee, Set<String> reports) {
        // with no direct reports, end
        if(employee.getDirectReports() == null) {
            return;
        }

        // Look at all the reports this employee has
        for (Employee report : employee.getDirectReports()) {
            String id = report.getEmployeeId();
            // if we've already processed this subordinate, skip
            if(id != null && !reports.contains(id) && !id.equals(employee.getEmployeeId())) {
                report = read(id);
                // Add the subordinate to the Set
                reports.add(id);
                // if the subordinate has subordinates, process them further
                if(!(report.getDirectReports() != null  && report.getDirectReports().isEmpty())) {
                    numberOfReports(report, reports);
                }
            }
        }
    }
}
