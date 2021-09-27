package com.bridgelabz.employeepayrollservice;

import java.sql.SQLException;

public class EmployeePayrollException extends SQLException {

	enum ExceptionType {
		 UNKOWN_DATABASE, SQL_EXCEPTION
	}

	ExceptionType type;

	public EmployeePayrollException(ExceptionType type, String message) {
		super(message);
		this.type = type;
	}
}
