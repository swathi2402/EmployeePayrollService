package com.bridgelabz.employeepayrollservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
		try {
			employeePayrollService.writeEmployeePayrollData(EmployeePayrollService.I0Service.FILE_I0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
		assertEquals(4, employeePayrollData.size());
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
		assertEquals(3, employeesListInDateRange.size());
	}

	@Test
	public void givenEmployees_getSumOfSalaryOfFemaleEmployees() throws SQLException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<Character, Double> sumOfSalary = employeePayrollService.getSumOfSalaryBasedOnGender();
		assertEquals((double) sumOfSalary.get('F'), 400000.0, 0.0);
	}

	@Test
	public void givenEmployees_getSumOfSalaryOfMaleEmployees() throws SQLException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<Character, Double> sumOfSalary = employeePayrollService.getSumOfSalaryBasedOnGender();
		assertEquals((double) sumOfSalary.get('M'), 500000.0, 0.0);
	}

	@Test
	public void givenEmployees_getAverageOfSalaryOfFemaleEmployees() throws SQLException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<Character, Double> averageOfSalaries = employeePayrollService.getAverageOfSalaryBasedOnGender();
		assertEquals((double) averageOfSalaries.get('F'), 400000.0, 0.0);
	}

	@Test
	public void givenEmployees_getAverageOfSalaryOfMaleEmployees() throws SQLException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<Character, Double> averageOfSalaries = employeePayrollService.getAverageOfSalaryBasedOnGender();
		assertEquals((double) averageOfSalaries.get('M'), 500000.0, 0.0);
	}

	@Test
	public void givenEmployees_getCountOfMaleEmployees() throws SQLException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<Character, Integer> count = employeePayrollService.getCountBasedOnGender();
		assertEquals((int) count.get('M'), 1);
	}

	@Test
	public void givenEmployees_getCountOfFemaleEmployees() throws SQLException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<Character, Integer> count = employeePayrollService.getCountBasedOnGender();
		assertEquals((int) count.get('F'), 1);
	}

	@Test
	public void givenEmployees_getMinimumOfSalaryOfFemaleEmployees() throws SQLException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<Character, Double> minimumOfSalaries = employeePayrollService.getMinimunOfSalaryBasedOnGender();
		assertEquals((double) minimumOfSalaries.get('F'), 400000.0, 0.0);
	}

	@Test
	public void givenEmployees_getMinimumOfSalaryOfMaleEmployees() throws SQLException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<Character, Double> minimumOfSalaries = employeePayrollService.getMinimunOfSalaryBasedOnGender();
		assertEquals((double) minimumOfSalaries.get('M'), 500000.0, 0.0);
	}

	@Test
	public void givenEmployees_getMaximumOfSalaryOfFemaleEmployees() throws SQLException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<Character, Double> maximumOfSalaries = employeePayrollService.getMaximumOfSalaryBasedOnGender();
		assertEquals((double) maximumOfSalaries.get('F'), 400000.0, 0.0);
	}

	@Test
	public void givenEmployees_getMaximumOfSalaryOfMaleEmployees() throws SQLException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<Character, Double> maximumOfSalaries = employeePayrollService.getMaximumOfSalaryBasedOnGender();
		assertEquals((double) maximumOfSalaries.get('M'), 500000.0, 0.0);
	}

	@Test
	public void givenEmplyoees_WrongSyntaxInQuery_ShouldGiveCustomException() throws EmployeePayrollException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		try {
			employeePayrollService.readEmployeePayrollDBData(I0Service.DB_IO);
			employeePayrollService.getCountBasedOnGender();
		} catch (EmployeePayrollException e) {
			assertEquals(EmployeePayrollException.ExceptionType.SQL_EXCEPTION, e.type);
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void givenNewEmployee_WhenAdded_ShouldBeInSyncWithDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		try {
			employeePayrollService.readEmployeePayrollDBData(I0Service.DB_IO);
			employeePayrollService.addEmployeeToPayroll("Mark", 'M', 200000.0, LocalDate.now());
			boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Mark");
			assertTrue(result);
		} catch (EmployeePayrollException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void givenNewEmployee_WhenAddedTransition_ShouldBeInSyncWithDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		try {
			employeePayrollService.readEmployeePayrollDBData(I0Service.DB_IO);
			employeePayrollService.addEmployeeToPayroll(1, 101, "Sam", 500000.0, "7788990066", "Karnataka", 'F',
					LocalDate.now());
			boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Sam");
			assertTrue(result);
		} catch (EmployeePayrollException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void givenEmployee_WhenDeleted_ShouldBeInSyncWithDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		try {
			employeePayrollService.readEmployeePayrollDBData(I0Service.DB_IO);
			int result = employeePayrollService.deleteEmployeeToPayroll("Sam");
			assertEquals(1, result);
		} catch (EmployeePayrollException e) {
			e.printStackTrace();
		}
	}
}
