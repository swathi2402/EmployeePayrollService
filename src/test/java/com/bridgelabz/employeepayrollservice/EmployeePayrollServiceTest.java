package com.bridgelabz.employeepayrollservice;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class EmployeePayrollServiceTest {

	@Test
	public void given3EmployeesWhenWrittenToFileShouldMatchEmployeeEntries() {
		EmployeePayrollData[] arrayOfEmps = { 
				new EmployeePayrollData(1, "Jeff Bezos", 100000.0),
				new EmployeePayrollData(2, "Bill Gates", 200000.0),
				new EmployeePayrollData(3, "Mark Zuckerberg", 300000.0) 
		};
		EmployeePayrollService employeePayrollService;
		employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
		employeePayrollService.writeEmployeePayrollData(EmployeePayrollService.I0Service.FILE_I0);
		employeePayrollService.printData(EmployeePayrollService.I0Service.FILE_I0);
		long entries = employeePayrollService.countEntries(EmployeePayrollService.I0Service.FILE_I0);
		assertEquals( 3, entries);
	}
}
