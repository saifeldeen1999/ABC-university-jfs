package com.luv2code.jsf.jdbc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.luv2code.jsf.jdbc.DepartmentDbUtil;
import com.luv2code.jsf.jdbc.entity.Department;


@ManagedBean
@SessionScoped
public class DepartmentController {

	private List<Department> departments;
	private DepartmentDbUtil departmentDbUtil;
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public DepartmentController() throws Exception {
		departments = new ArrayList<>();
		
		departmentDbUtil = DepartmentDbUtil.getInstance();
	}
	
	public List<Department> getDepartments() {
		return departments;
	}

	public void loadDepartments() {

		logger.info("Loading departments");
		
		departments.clear();

		try {
			
			// get all departments from database
			departments = departmentDbUtil.getDepartments();
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading departments", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
		}
	}
		
	public String addDepartment(Department theDepartment) {

		logger.info("Adding Department: " + theDepartment);

		try {
			
			// add Department to the database
			departmentDbUtil.addDepartment(theDepartment);
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error adding Departments", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);

			return null;
		}
		
		return "list-departments?faces-redirect=true";
	}

	public String loadDepartment(int departmentId) {
		
		logger.info("loading Department: " + departmentId);
		
		try {
			// get Department from database
			Department theDepartment = departmentDbUtil.getDepartment(departmentId);
			
			// put in the request attribute ... so we can use it on the form page
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();		

			Map<String, Object> requestMap = externalContext.getRequestMap();
			requestMap.put("department", theDepartment);	
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading Department id:" + departmentId, exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
			
			return null;
		}
				
		return "update-department-form.xhtml";
	}	
	
	public String updateDepartment(Department theDepartment) {

		logger.info("updating Department: " + theDepartment);
		
		try {
			
			// update student in the database
			departmentDbUtil.updateDepartment(theDepartment);
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error updating department: " + theDepartment, exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
			
			return null;
		}
		
		return "list-departments?faces-redirect=true";		
	}
	
	public String deleteDepartment(int departmentId) {

		logger.info("Deleting Department id: " + departmentId);
		
		try {

			// delete the Department from the database
			departmentDbUtil.deleteDepartment(departmentId);
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error deleting department id: " + departmentId, exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
			
			return null;
		}
		
		return "list-departments";	
	}	
	
	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
}
