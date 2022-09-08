package com.luv2code.jsf.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.luv2code.jsf.jdbc.entity.Department;

public class DepartmentDbUtil {

	private static DepartmentDbUtil instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/test_university_db";
	
	public static DepartmentDbUtil getInstance() throws Exception {
		if (instance == null) {
			instance = new DepartmentDbUtil();
		}
		
		return instance;
	}
	
	private DepartmentDbUtil() throws Exception {		
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
		
	public List<Department> getDepartments() throws Exception {

		List<Department> departments = new ArrayList<>();

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from department order by title";

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);

			// process result set
			while (myRs.next()) {
				
				// retrieve data from result set row
				int id = myRs.getInt("id");
				String title = myRs.getString("title");


				// create new Department object
				Department tempDepartment = new Department(id, title);

				// add it to the list of Departments
				departments.add(tempDepartment);
			}
			
			return departments;		
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}

	public void addDepartment(Department theDepartment) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "insert into department (title) values (?)";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setString(1, theDepartment.getTitle());

			
			myStmt.execute();			
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	public Department getDepartment(int departmentId) throws Exception {
	
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from department where id=?";

			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setInt(1, departmentId);
			
			myRs = myStmt.executeQuery();

			Department theDepartment = null;
			
			// retrieve data from result set row
			if (myRs.next()) {
				int id = myRs.getInt("id");
				String title = myRs.getString("title");


				theDepartment = new Department(id, title);
			}
			else {
				throw new Exception("Could not find department Id: " + departmentId);
			}

			return theDepartment;
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	public void updateDepartment(Department theDepartment) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "update department "
						+ " set title=?"
						+ " where id=?";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setString(1, theDepartment.getTitle());

			myStmt.setInt(2, theDepartment.getId());
			
			myStmt.execute();
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	public void deleteDepartment(int departmentId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "delete from department where id=?";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, departmentId);
			
			myStmt.execute();
		}
		finally {
			close (myConn, myStmt);
		}		
	}	
	
	private Connection getConnection() throws Exception {

		Connection theConn = dataSource.getConnection();
		
		return theConn;
	}
	
	private void close(Connection theConn, Statement theStmt) {
		close(theConn, theStmt, null);
	}
	
	private void close(Connection theConn, Statement theStmt, ResultSet theRs) {

		try {
			if (theRs != null) {
				theRs.close();
			}

			if (theStmt != null) {
				theStmt.close();
			}

			if (theConn != null) {
				theConn.close();
			}
			
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}	
}
