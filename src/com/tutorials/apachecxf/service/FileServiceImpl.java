package com.tutorials.apachecxf.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import javax.activation.DataHandler;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.cxf.common.util.CollectionUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.tutorials.apachecxf.aspects.Log;

@Service("fileService")
public class FileServiceImpl implements IFileService {

	protected static final Logger log = LogManager.getLogger();

	@Override
	@Log
	public Response test() {
		log.info("tes method");
		return Response.ok("OK").build();
	}

	@Override
	public Response uploadExcelFile(Attachment attachment) {
		String fileName = null;
		MultivaluedMap<String, String> multivaluedMap = attachment.getHeaders();
		String[] contentDisposition = multivaluedMap.getFirst("Content-Disposition").split(";");
		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {
				String[] name = filename.split("=");
				fileName = name[1].trim().replaceAll("\"", "");
			}
		}
		DataHandler dataHandler = attachment.getDataHandler();
		try (Workbook wb = new XSSFWorkbook(dataHandler.getInputStream())) {
			Sheet sheet = wb.getSheetAt(0);
			Iterator<Row> rows = sheet.rowIterator();
			while (rows.hasNext()) {
				Row row = rows.next();
				Iterator<Cell> cells = row.cellIterator();
				while (cells.hasNext()) {
					Cell cell = cells.next();
					System.out.println(cell.toString());
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return Response.ok(fileName).build();
	}

	public Response downloadReport(List<String> params) {
		File file = new File("report.xlsx");
		if (!CollectionUtils.isEmpty(params)) {
			try (FileOutputStream fileOut = new FileOutputStream(file); Workbook workbook = new XSSFWorkbook();) {
				Sheet sheet = workbook.createSheet("report");
				Row rowhead = sheet.createRow((short) 0);
				IntStream.range(0, params.size())
						.forEach(index -> rowhead.createCell(index).setCellValue(params.get(index)));
				workbook.write(fileOut);
				System.out.println("Excel file has been generated successfully.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition", "attachment; filename=" + file.getName());
		return response.build();
	}

}