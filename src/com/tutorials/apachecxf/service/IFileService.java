package com.tutorials.apachecxf.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

public interface IFileService {

	// http://localhost:8080/ApacheCXFService/services/v1/test
	@GET
	@Path("/test")
	public Response test();

	// http://localhost:8080/ApacheCXFService/services/v1/upload
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadExcelFile(@Multipart("file") Attachment attachment);

	// http://localhost:8080/ApacheCXFService/services/v1/downloadReport
	@GET
	@Path("/downloadReport")
	@Produces("application/vnd.ms-excel")
	public Response downloadReport(@QueryParam(value = "param") List<String> params);

}