package com.bridgelabz.employeepayrollservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.bridgelabz.employeepayrollservice.EmployeePayrollService.I0Service;

public class EmployeePayrollServiceTest {

	@Test
	public void given3EmployeesWhenWrittenToFileShouldMatchEmployeeEntries() {
		EmployeePayrollData[] arrayOfEmps = { new EmployeePayrollData(1, "Jeff Bezos", 100000.0),
				new EmployeePayrollData(2, "Bill Gates", 200000.0),
				new EmployeePayrollData(3, "Mark Zuckerberg", 300000.0) };
		EmployeePayrollService employeePayrollService;
		employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
		employeePayrollService.writeEmployeePayrollData(EmployeePayrollService.I0Service.FILE_I0);
		employeePayrollService.printData(EmployeePayrollService.I0Service.FILE_I0);
		long entries = employeePayrollService.countEntries(EmployeePayrollService.I0Service.FILE_I0);
		assertEquals(3, entries);
	}

	@Test
	public void givenFileOnReadingFromFileShouldMatchEmployeeCount() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		long entries = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.I0Service.FILE_I0);
		assertEquals(3, entries);
	}

	@Test
	public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() throws SQLException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService
				.readEmployeePayrollDBData(I0Service.DB_IO);
		assertEquals(3, employeePayrollData.size());
	}

	@Test
	public void givenNewSalaryForEmployee_WhenUpdatesWithStatements_ShouldSyncWithDB() throws SQLException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollDBData(I0Service.DB_IO);
		employeePayrollService.updateEmployeeSalary("Terisa", 300000.00);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
		assertTrue(result);
	}

	@Test
	public void givenNewSalaryForEmployee_WhenUpdates_ShouldSyncWithDB() throws SQLException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollDBData(I0Service.DB_IO);
		employeePayrollService.updateEmployeeSalary("Terisa", 400000.00);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
		assertTrue(result);
	}

	@Test
	public void givenDateRange_WhenRetrieved_ShouldMatchEmplyoeeCount() throws SQLException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollDBData(I0Service.DB_IO);
		String date = "2020-01-01";
		List<EmployeePayrollData> employeesListInDateRange = employeePayrollService.getEmployeesFromDateRange(date);
		assertEquals(2, employeesListInDateRange.size());
	}

	@Test
	public void givenEmployees_getSumOfSalaryOfFemaleEmployees() throws SQLException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		double sumOfSalary = employeePayrollService.getSumOfSalaryBasedOnGender('F');
		assertEquals(300000.0, sumOfSalary, 0.0);
	}
}
