package com.bridgelabz.employeepayrollservice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class EmployeePayrollService {

	public enum I0Service {
		CONSOLE_IO, FILE_I0, DB_IO, REST_I0
	}

	private List<EmployeePayrollData> employeePayrollList;
	private EmployeePayrollDBService employeePayrollDBService;

	public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
		this.employeePayrollList = employeePayrollList;
	}

	public EmployeePayrollService() {
		employeePayrollDBService = EmployeePayrollDBService.getInstance();
	}

	private void readEmployeePayrollData(Scanner consoleInputReader) {
		System.out.println("Enter Employee ID: ");
		int id = consoleInputReader.nextInt();
		System.out.println("Enter Employee Name: ");
		String name = consoleInputReader.next();
		System.out.println("Enter Employee Salary: ");
		double salary = consoleInputReader.nextDouble();
		employeePayrollList.add(new EmployeePayrollData(id, name, salary));
	}

	void writeEmployeePayrollData(I0Service ioservice) throws EmployeePayrollException {
		switch (ioservice) {
		case CONSOLE_IO:
			System.out.println("\nWriting Employee Payroll to Console\n" + employeePayrollList);
			break;

		case FILE_I0:
			new EmployeePayrollFileIOService().writeData(employeePayrollList);
			break;

		default:
			break;
		}
	}

	public void printData(I0Service ioservice) {
		if (ioservice.equals(I0Service.FILE_I0)) {
			new EmployeePayrollFileIOService().printData();
		}
	}

	public long countEntries(I0Service ioservice) {
		if (ioservice.equals(I0Service.FILE_I0)) {
			return new EmployeePayrollFileIOService().countEntries();
		}
		return 0;
	}

	public void addEmployeeToPayroll(String name, char gender, double salary, LocalDate startDate)
			throws EmployeePayrollException {
		this.employeePayrollList.add(employeePayrollDBService.addEmployeeToPayroll(name, gender, salary, startDate));

	}

	public void addEmployeeToPayroll(int companyId, int departmentId, String name, double salary, String phoneNumber,
			String address, char gender, LocalDate startDate) throws EmployeePayrollException {
		this.employeePayrollList.add(employeePayrollDBService.addEmployeeToPayrollTransaction(companyId, departmentId,
				name, salary, phoneNumber, address, gender, startDate));
	}

	public long readEmployeePayrollData(I0Service ioservice) {
		if (ioservice.equals(I0Service.FILE_I0)) {
			this.employeePayrollList = new EmployeePayrollFileIOService().readData();
			System.out.println("Employee Details: ");
			this.employeePayrollList.forEach(employee -> System.out.println(employee));
		}
		return this.employeePayrollList.size();
	}

	public List<EmployeePayrollData> readEmployeePayrollDBData(I0Service ioservice) throws EmployeePayrollException {
		if (ioservice.equals(I0Service.DB_IO))
			this.employeePayrollList = employeePayrollDBService.readDataTransition();
		return this.employeePayrollList;
	}

	public void updateEmployeeSalary(String name, double salary) throws EmployeePayrollException {
		int result = employeePayrollDBService.updateEmployeeData(name, salary);
		if (result == 0)
			return;
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if (employeePayrollData != null)
			employeePayrollData.basic_pay = salary;
	}

	public int deleteEmployeeToPayroll(String name) throws EmployeePayrollException {
		int result = employeePayrollDBService.deleteEmployeeData(name);
		return result;
	}

	public boolean checkEmployeePayrollInSyncWithDB(String name) throws EmployeePayrollException {
		List<EmployeePayrollData> employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
		return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
	}

	private EmployeePayrollData getEmployeePayrollData(String name) {
		return this.employeePayrollList.stream()
				.filter(employeePayrollDataItem -> employeePayrollDataItem.name.equals(name)).findFirst().orElse(null);
	}

	public List<EmployeePayrollData> getEmployeesFromDateRange(String date) throws EmployeePayrollException {
		List<EmployeePayrollData> employeesInGivenRange = employeePayrollDBService.getEmployeesFromDateRange(date);
		return employeesInGivenRange;
	}

	public static void main(String[] args) throws EmployeePayrollException {
		ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
		Scanner consoleInputReader = new Scanner(System.in);
		employeePayrollService.readEmployeePayrollData(consoleInputReader);
		employeePayrollService.writeEmployeePayrollData(I0Service.CONSOLE_IO);
	}

	public Map<Character, Double> getSumOfSalaryBasedOnGender() throws EmployeePayrollException {
		Map<Character, Double> sumOfSalaryMap = employeePayrollDBService.getSumOfSalariesBasedOnGender();
		return sumOfSalaryMap;
	}

	public double getAverageOfSalaryBasedOnGender(char gender) throws EmployeePayrollException {
		double averageOfSalaries = employeePayrollDBService.getAverageOfSalaryBasedOnGender(gender);
		return averageOfSalaries;
	}

	public int getCountBasedOnGender(char gender) throws EmployeePayrollException {
		int count = employeePayrollDBService.getCountBasedOnGender(gender);
		return count;
	}

	public double getMinimunOfSalaryBasedOnGender(char gender) throws EmployeePayrollException {
		double minimumOfSalaries = employeePayrollDBService.getMinimunOfSalaryBasedOnGender(gender);
		return minimumOfSalaries;
	}

	public double getMaximumOfSalaryBasedOnGender(char gender) throws EmployeePayrollException {
		double maximumOfSalaries = employeePayrollDBService.getMaximunOfSalaryBasedOnGender(gender);
		return maximumOfSalaries;
	}

}