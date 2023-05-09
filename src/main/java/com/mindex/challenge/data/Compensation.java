package com.mindex.challenge.data;

import java.util.Date;

public class Compensation {
    private Employee employee;
    // whole dollar amount
    private int salary;
    private Date effectiveDate;

    public Compensation () { }

    public Compensation (Employee employee, int salary, Date effectiveDate) {
        this.employee = employee;
        this.salary = salary;
        this.effectiveDate = effectiveDate;
    }

    public Employee getEmployee() {
        return employee;
    }
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public int getSalary() {
        return salary;
    }
    public void setSalary(int salary) {
        this.salary = salary;
    }
    public Date getEffectiveDate() {
        return effectiveDate;
    }
    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @Override
    public String toString() {
        return "Compensation [employee=" + this.employee + ", salary=" + this.salary + "]";
    }
}
