package com.soinsoftware.vissa.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.soinsoftware.vissa.model.Person;
import com.soinsoftware.vissa.model.User;

public class ExcelWriter<E> {

	private final String fileName;
	private final Workbook workbook;
	private final CreationHelper createHelper;

	public ExcelWriter(final String fileName) {
		this.fileName = fileName;
		workbook = new XSSFWorkbook();
		createHelper = workbook.getCreationHelper();
	}

	public static void main(String[] args) throws IOException, InvalidFormatException {
		List<User> employees = new ArrayList<>();
		employees.add(User.builder().person(Person.builder().name("Carlos").lastName("Rodriguez").build()).build());
		employees.add(User.builder().person(Person.builder().name("Lina").lastName("Florez").build()).build());
		employees.add(User.builder().person(Person.builder().name("Ezequiel").lastName("Florez").build()).build());
		String sheetName = "Empleados";
		List<String> columns = Arrays.asList("Name", "Email", "Date Of Birth", "Salary");

		ExcelWriter<User> excelWriter = new ExcelWriter<>("poi-generated-file.xlsx");
		excelWriter.createSheet(sheetName, columns, employees);
		excelWriter.exportFile();
	}

	private Font createFontHeader() {
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.RED.getIndex());
		return headerFont;
	}

	private CellStyle createCellStyle() {
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(createFontHeader());
		return headerCellStyle;
	}

	private void createHeaderRow(final Sheet sheet, final List<String> columns) {
		CellStyle headerCellStyle = createCellStyle();
		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < columns.size(); i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(columns.get(i));
			cell.setCellStyle(headerCellStyle);
		}
	}

	private void createInformationRows(final Sheet sheet, final List<String> columns, final List<E> dataInList) {
		CellStyle dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
		int rowNum = 1;
		for (Object data : dataInList) {
			Row row = sheet.createRow(rowNum++);
			
			// Override and change to map the cell with the POJO
			User user = (User) data;
			row.createCell(0).setCellValue(user.getPerson().getName());
			row.createCell(1).setCellValue(user.getPerson().getLastName());
			Cell dateOfBirthCell = row.createCell(2);
			dateOfBirthCell.setCellValue(user.getCreationDate());
			dateOfBirthCell.setCellStyle(dateCellStyle);
			row.createCell(3).setCellValue(0);
		}
		
		for (int i = 0; i < columns.size(); i++) {
			sheet.autoSizeColumn(i);
		}
	}

	public void createSheet(final String sheetName, final List<String> columns, final List<E> dataInList) {
		Sheet sheet = workbook.createSheet(sheetName);
		createHeaderRow(sheet, columns);
		createInformationRows(sheet, columns, dataInList);
	}

	public void exportFile() throws IOException, InvalidFormatException {
		try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
			workbook.write(fileOut);
		} finally {
			workbook.close();
		}
	}
}