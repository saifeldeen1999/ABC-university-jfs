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

import com.luv2code.jsf.jdbc.ProfessorDbUtil;
import com.luv2code.jsf.jdbc.entity.Professor;

@ManagedBean
@SessionScoped
public class ProfessorController {

	private List<Professor> professors;
	private ProfessorDbUtil professorDbUtil;
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public ProfessorController() throws Exception {
		professors = new ArrayList<>();
		
		professorDbUtil = ProfessorDbUtil.getInstance();
	}
	
	public List<Professor> getProfessors() {
		return professors;
	}

	public void loadProfessors() {

		logger.info("Loading professors");
		
		professors.clear();

		try {
			
			// get all students from database
			professors = professorDbUtil.getProfessors();
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading professors", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
		}
	}
		
	public String addProfessor(Professor theProfessor) {

		logger.info("Adding professor: " + theProfessor);

		try {
			
			// add student to the database
			professorDbUtil.addProfessor(theProfessor);
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error adding professors", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);

			return null;
		}
		
		return "list-professors?faces-redirect=true";
	}

	public String loadProfessor(int professorId) {
		
		logger.info("loading professor: " + professorId);
		
		try {
			// get student from database
			Professor theProfessor = professorDbUtil.getProfessor(professorId);
			
			// put in the request attribute ... so we can use it on the form page
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();		

			Map<String, Object> requestMap = externalContext.getRequestMap();
			requestMap.put("professor", theProfessor);	
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading professor id:" + professorId, exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
			
			return null;
		}
				
		return "update-professor-form.xhtml";
	}	
	
	public String updateProfessor(Professor theProfessor) {

		logger.info("updating professor: " + theProfessor);
		
		try {
			
			// update student in the database
			professorDbUtil.updateProfessor(theProfessor);
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error updating professor: " + theProfessor, exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
			
			return null;
		}
		
		return "list-professors?faces-redirect=true";		
	}
	
	public String deleteProfessor(int professorId) {

		logger.info("Deleting professor id: " + professorId);
		
		try {

			// delete the student from the database
			professorDbUtil.deleteProfessor(professorId);
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error deleting professor id: " + professorId, exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
			
			return null;
		}
		
		return "list-professors";	
	}	
	
	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
}
