package com.bridgelabz.employeepayrollservice;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {

	public enum I0Service {
		CONSOLE_IO, FILE_I0, DB_IO, REST_I0
	}

	private List<EmployeePayrollData> employeePayrollList;

	public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
		this.employeePayrollList = employeePayrollList;
	}
	 
	public EmployeePayrollService() {
		// TODO Auto-generated constructor stub
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

	void writeEmployeePayrollData(I0Service ioservice) {
		if (ioservice.equals(I0Service.CONSOLE_IO))
			System.out.println("\nWriting Employee Payroll to Console\n" + employeePayrollList);
		else if (ioservice.equals(I0Service.FILE_I0)) {
			new EmployeePayrollFileIOService().writeData(employeePayrollList);
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

	public long readEmployeePayrollData(I0Service ioservice) {
		if (ioservice.equals(I0Service.FILE_I0)) {
			this.employeePayrollList = new EmployeePayrollFileIOService().readData();
			System.out.println("Employee Details: ");
			this.employeePayrollList.forEach(employee -> System.out.println(employee));
		}
		return this.employeePayrollList.size();
	}

	public List<EmployeePayrollData> readEmployeePayrollDBData(I0Service ioservice) throws SQLException {
		if(ioservice.equals(I0Service.DB_IO))
			this.employeePayrollList = new EmployeePayrollDBService().readData();
		return this.employeePayrollList;
	}

	public static void main(String[] args) {
		ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
		Scanner consoleInputReader = new Scanner(System.in);
		employeePayrollService.readEmployeePayrollData(consoleInputReader);
		employeePayrollService.writeEmployeePayrollData(I0Service.CONSOLE_IO);
	}


}