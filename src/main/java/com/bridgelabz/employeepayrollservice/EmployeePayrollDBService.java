package com.bridgelabz.employeepayrollservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBService {

	private PreparedStatement employeePayrollDataStatement;
	private static EmployeePayrollDBService employeePayrollDBService;

	public static EmployeePayrollDBService getInstance() {
		if (employeePayrollDBService == null)
			employeePayrollDBService = new EmployeePayrollDBService();
		return employeePayrollDBService;
	}

	public EmployeePayrollDBService() {

	}

	private Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/employee_service?useSSL=false";
		String userName = "root";
		String password = "swathi*123";
		Connection connection;

		System.out.println("Connecting to database" + jdbcURL);
		connection = DriverManager.getConnection(jdbcURL, userName, password);
		System.out.println("Connection is successfull" + connection);

		return connection;
	}

	public List<EmployeePayrollData> readData() throws SQLException {
		String sql = "SELECT id, name, basic_pay, start FROM employee_payroll";
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				Double salary = result.getDouble("basic_pay");
				LocalDate startDate = result.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	public int updateEmployeeData(String name, double salary) {
		return this.updateEmployeeDataUsingStatement(name, salary);
	}

	private int updateEmployeeDataUsingStatement(String name, double salary) {
		String sql = String.format("UPDATE employee_payroll SET basic_pay= %.2f WHERE name ='%s';", salary, name);
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<EmployeePayrollData> getEmployeePayrollData(String name) {
		List<EmployeePayrollData> employeePayrollList = null;
		if (this.employeePayrollDataStatement == null)
			this.prepareStatementForEmployeeData();
		try {
			employeePayrollDataStatement.setString(1, name);
			ResultSet resultSet = employeePayrollDataStatement.executeQuery();
			employeePayrollList = this.getEmployeePayrollData(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try {
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				Double salary = resultSet.getDouble("basic_pay");
				LocalDate startDate = resultSet.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	private void prepareStatementForEmployeeData() {
		try {
			Connection connection = this.getConnection();
			String sql = "SELECT id, name, basic_pay, start FROM employee_payroll WHERE name=?";
			employeePayrollDataStatement = connection.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<EmployeePayrollData> getEmployeesFromDateRange(String date) {
		String sql = String.format(
				"SELECT id, name, basic_pay, start FROM employee_payroll WHERE start BETWEEN CAST('%s' AS DATE) AND DATE(NOW());",
				date);
		List<EmployeePayrollData> employeesListInDateRange = new ArrayList<>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			employeesListInDateRange = this.getEmployeePayrollData(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeesListInDateRange;
	}

	public double getSumOfSalariesBasedOnGender(char gender) {
		String sql = String.format(
				"SELECT gender, SUM(basic_pay) FROM employee_payroll WHERE gender = '%c' GROUP BY gender;", gender);

		double sumOfSalaries = 0.0;
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				sumOfSalaries = resultSet.getDouble(2);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sumOfSalaries;
	}

	public double getAverageOfSalaryBasedOnGender(char gender) {
		String sql = String.format(
				"SELECT gender, AVG(basic_pay) FROM employee_payroll WHERE gender = '%c' GROUP BY gender;", gender);

		double averageOfSalaries = 0.0;
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				averageOfSalaries = resultSet.getDouble(2);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return averageOfSalaries;
	}

	public int getCountBasedOnGender(char gender) {
		String sql = String.format(
				"SELECT gender, COUNT(basic_pay) FROM employee_payroll WHERE gender = '%c' GROUP BY gender;", gender);

		int count = 0;
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				count = resultSet.getInt(2);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public double getMinimunOfSalaryBasedOnGender(char gender) {
		String sql = String.format(
				"SELECT gender, MIN(basic_pay) FROM employee_payroll WHERE gender = '%c' GROUP BY gender;", gender);

		double minimumOfSalaries = 0.0;
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				minimumOfSalaries = resultSet.getDouble(2);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return minimumOfSalaries;
	}

	public double getMaximunOfSalaryBasedOnGender(char gender) {
		String sql = String.format(
				"SELECT gender, MAX(basic_pay) FROM employee_payroll WHERE gender = '%c' GROUP BY gender;", gender);

		double maximumOfSalaries = 0.0;
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				maximumOfSalaries = resultSet.getDouble(2);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return maximumOfSalaries;
	}
}
