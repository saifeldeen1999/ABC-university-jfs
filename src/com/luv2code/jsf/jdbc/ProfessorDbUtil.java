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

import com.luv2code.jsf.jdbc.entity.Professor;
import com.luv2code.jsf.jdbc.entity.Student;

public class ProfessorDbUtil {

	private static ProfessorDbUtil instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/test_university_db";
	
	public static ProfessorDbUtil getInstance() throws Exception {
		if (instance == null) {
			instance = new ProfessorDbUtil();
		}
		
		return instance;
	}
	
	private ProfessorDbUtil() throws Exception {		
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
		
	public List<Professor> getProfessors() throws Exception {

		List<Professor> professors = new ArrayList<>();

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from professor order by last_name";

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);

			// process result set
			while (myRs.next()) {
				
				// retrieve data from result set row
				int id = myRs.getInt("id");
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");

				// create new Professor object
				Professor tempProfessor = new Professor(id, firstName, lastName,
						email);

				// add it to the list of Professors
				professors.add(tempProfessor);
			}
			
			return professors;		
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}

	public void addProfessor(Professor theProfessor) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "insert into professor (first_name, last_name, email) values (?, ?, ?)";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setString(1, theProfessor.getFirstName());
			myStmt.setString(2, theProfessor.getLastName());
			myStmt.setString(3, theProfessor.getEmail());
			
			myStmt.execute();			
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	public Professor getProfessor(int professorId) throws Exception {
	
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from professor where id=?";

			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setInt(1, professorId);
			
			myRs = myStmt.executeQuery();

			Professor theProfessor = null;
			
			// retrieve data from result set row
			if (myRs.next()) {
				int id = myRs.getInt("id");
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");

				theProfessor = new Professor(id, firstName, lastName,
						email);
			}
			else {
				throw new Exception("Could not find student id: " + professorId);
			}

			return theProfessor;
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	public void updateProfessor(Professor theProfessor) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "update professor "
						+ " set first_name=?, last_name=?, email=?"
						+ " where id=?";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setString(1, theProfessor.getFirstName());
			myStmt.setString(2, theProfessor.getLastName());
			myStmt.setString(3, theProfessor.getEmail());
			myStmt.setInt(4, theProfessor.getId());
			
			myStmt.execute();
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	public void deleteProfessor(int professorId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "delete from professor where id=?";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, professorId);
			
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
