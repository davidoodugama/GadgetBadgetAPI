package com.service;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import model.Fund;
import model.ProposalProject;

/**
 * ProposalProjectService
 * 
 ********************************************************************************************************
 * ### Date Author Description
 * -------------------------------------------------------------------------------------------------------
 * 1 14-04-2021 David Created
 * 
 ********************************************************************************************************
 */

@Path("/Project")

public class ProposalProjectService {

	ProposalProject pro = new ProposalProject();
	Fund f = new Fund();

	// *****************************************************Read all the Projects*******************************************//

	@GET
	@Path("/")
	@Produces(MediaType.TEXT_HTML)
	public String readProjects() {
		return pro.readProjects();
	}

	// **************************************************************END****************************************************//

	// *****************************************************Inserting Project details***************************************//
	

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String insertProject(@FormParam("projectname") String projectname, @FormParam("doclinks") String doclinks,
			@FormParam("description") String description, @FormParam("projectType") String projectType) {
		String output = pro.insertProject(projectname, doclinks, description, projectType);
		return output;

	}

	// **************************************************************END****************************************************//

	// *****************************************************Read funds by Project ID****************************************//

	@GET
	@Path("/{ProId}")
	@Produces(MediaType.TEXT_HTML)
	public String readFundById(@PathParam("ProId") String ProId) {
		String output = f.readFundById(ProId);
		return output;
	}

	// **************************************************************END****************************************************//

	// ***************************************Get Approval Message from Admin if rejects deleting the row ******************//

	@POST
	@Path("/proname")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String getApproval(String approvedata) {

		JsonObject proObj = new JsonParser().parse(approvedata).getAsJsonObject();
		String proname = proObj.get("projectname").getAsString();
		String status = proObj.get("status").getAsString();
		String output = pro.getApproval(proname, status);
		return output;
	}

	// **************************************************************END****************************************************//

	// ***************************************************Delete Project and Funds******************************************//


	@DELETE
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteProjects(String proData) {
		JsonObject proObj = new JsonParser().parse(proData).getAsJsonObject();
		String proname = proObj.get("proname").getAsString();
		String output = pro.deleteProjects(proname);
		return output;
	}

	// **************************************************************END****************************************************//

	// *****************************************************Update Project**************************************************//

	@PUT
	@Path("/proId")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String updateProject(String projectData) {

		JsonObject proObj = new JsonParser().parse(projectData).getAsJsonObject();
		String ProID = proObj.get("projectID").getAsString();
		String projectName = proObj.get("projectname").getAsString();
		String Doclinks = proObj.get("doclinks").getAsString();
		String Description = proObj.get("description").getAsString();
		String ProjectType = proObj.get("projectType").getAsString();
		String status = proObj.get("status").getAsString();
		
		String output = pro.updateProject(ProID, projectName, Doclinks, Description, ProjectType,status);
		return output;
	}

	// **************************************************************END****************************************************//

	// communicate with FeedBack Service
	
}
