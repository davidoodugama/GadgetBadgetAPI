package com.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import model.Admin;

@Path("/Admin")
public class AdminService {
	
	Admin ad = new Admin();

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String getProject(String adminData) {
		
	JsonObject proObj = new JsonParser().parse(adminData).getAsJsonObject();
	String projectName = proObj.get("projectname").getAsString();
	String Doclinks = proObj.get("doclinks").getAsString();
	String Description = proObj.get("description").getAsString();
	String ProjectType = proObj.get("projectType").getAsString();
	String status = proObj.get("status").getAsString();
	
	String output = ad.getProject(projectName, Doclinks, Description,ProjectType,status);
	return output;

	}
	
	@PUT
	@Path("/sendPro")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String sendApprov(String msgData) {
		
	JsonObject proObj = new JsonParser().parse(msgData).getAsJsonObject();
	String projectName = proObj.get("projectname").getAsString();
	String status = proObj.get("status").getAsString();

	String output = ad.sendApprov(projectName,status);
	return output;

	}
	
}
