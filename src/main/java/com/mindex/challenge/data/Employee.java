package com.mindex.challenge.data;

import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String employeeId;
    private String firstName;
    private String lastName;
    private String position;
    private String department;

    @Embedded
    private Compensation compensation;

    @OneToMany
    private List<Employee> directReports;

    public Employee() { }

    public Employee(String firstName, String lastName, String position, String department, Compensation compensation, List<Employee> directReports) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.department = department;
        this.compensation = compensation;
        this.directReports = directReports;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Compensation getCompensation() {
        return compensation;
    }

    public void setCompensation(Compensation compensation) {
        this.compensation = compensation;
    }

    public List<Employee> getDirectReports() {
        return directReports;
    }

    public void setDirectReports(List<Employee> directReports) {
        this.directReports = directReports;
    }

    @Override
    public String toString() {
        return "Employee [employeeId=" + this.employeeId + "]";
    }
}
