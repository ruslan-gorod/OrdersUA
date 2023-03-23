package hk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

public class Helper {

	public static boolean isRecordForMvo(Record r) {
		return r.getDt().equals("23") && r.getKt().equals("201") && r.getDoc().contains("Кальк. ХК");
	}

	public static boolean isRecordForPNInfo(Record r) {
		return (r.getDt().equals("201") && r.getKt().equals("631"))
				|| (r.getDt().equals("201") && r.getKt().equals("632"));
	}

	public static boolean isCanSetProduct(Record r) {
		return (r.getDt().equals("26") && r.getKt().equals("23") && r.getCount() != 0)
				|| (r.getDt().equals("25") && r.getKt().equals("23") && r.getCount() != 0);
	}

	public static boolean isUse_25(Record r) {
		return r.getDt().equals("23") && r.getKt().equals("25") && r.getCount() != 0;
	}

	public static boolean isUse_201(Record r) {
		return r.getDt().equals("23") && r.getKt().equals("201") && r.getCount() != 0;
	}

	public static void deleteFile(File element) {
		if (element.isDirectory()) {
			for (File sub : element.listFiles()) {
				deleteFile(sub);
			}
		}
		element.delete();
	}

	public static void createWorkbook(File fileName, List<Order> ordersToFile, LocalDate ld, String mvo)
			throws IOException {
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("akt");
			createHeader(sheet, ld);
			int i = 6;
			for (Order order : ordersToFile) {
				i = createOrderRecord(sheet, i, order);
			}
			sheetPrintSetup(sheet);
			createFooter(mvo, sheet, i);
			workbook.write(fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void sheetPrintSetup(HSSFSheet sheet) {
		for (int j = 0; j < 4; j++) {
			sheet.autoSizeColumn(j);
		}
		sheet.setHorizontallyCenter(true);
		sheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);
		sheet.getPrintSetup().setFitHeight((short) 5);
		sheet.getPrintSetup().setFitWidth((short) 1);
		sheet.setAutobreaks(true);
		sheet.setFitToPage(true);
	}

	private static void createFooter(String mvo, HSSFSheet sheet, int i) {
		Row rowlast = sheet.createRow(i + 1);
		Cell cellrowlast0 = rowlast.createCell(0);
		Cell cellrowlast2 = rowlast.createCell(2);
		cellrowlast0.setCellValue("Здав:");
		cellrowlast2.setCellValue(mvo);
		CellStyle cellStyleLast = rowlast.getSheet().getWorkbook().createCellStyle();
		Font fcs = rowlast.getSheet().getWorkbook().createFont();
		fcs.setTypeOffset((short) 2000);
		fcs.setFontHeight((short) 300);
		cellStyleLast.setFont(fcs);
		cellrowlast0.setCellStyle(cellStyleLast);
		cellrowlast2.setCellStyle(cellStyleLast);

		Row rowlastP = sheet.createRow(i + 3);
		Cell cellrowlastP0 = rowlastP.createCell(0);
		Cell cellrowlastP2 = rowlastP.createCell(2);
		cellrowlastP0.setCellValue("Прийняв:");
		cellrowlastP2.setCellValue("Кич Я.С.");
		cellrowlastP0.setCellStyle(cellStyleLast);
		cellrowlastP2.setCellStyle(cellStyleLast);
	}

	private static int createOrderRecord(HSSFSheet sheet, int i, Order order) {
		Row row1 = sheet.createRow(i++);
		Cell cell10 = row1.createCell(0);
		Cell cell11 = row1.createCell(1);
		Cell cell12 = row1.createCell(2);
		Cell cell13 = row1.createCell(3);
		cell10.setCellValue("Готова продукція:");
		cell11.setCellValue(order.getProduct());
		cell12.setCellValue("Кількість:");
		cell13.setCellValue(order.getCountProduct());

		Row row2 = sheet.createRow(i++);
		Cell cell20 = row2.createCell(0);
		cell20.setCellValue("Використано:");

		Row row3 = sheet.createRow(i++);
		Cell cell30 = row3.createCell(0);
		Cell cell31 = row3.createCell(1);
		Cell cell32 = row3.createCell(2);
		Cell cell33 = row3.createCell(3);
		cell30.setCellValue("Сировина та матеріали");
		cell31.setCellValue("Кількість");
		cell32.setCellValue("Постачальник");
		cell33.setCellValue("Дата поставки");
		CellStyle cellStyle3 = row3.getSheet().getWorkbook().createCellStyle();
//		cellStyle3.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		cellStyle3.setBorderBottom(BorderStyle.MEDIUM);
//		cellStyle3.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		cellStyle3.setBorderLeft(BorderStyle.MEDIUM);
//		cellStyle3.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		cellStyle3.setBorderRight(BorderStyle.MEDIUM);
//		cellStyle3.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		cellStyle3.setBorderTop(BorderStyle.MEDIUM);
		cell30.setCellStyle(cellStyle3);
		cell31.setCellStyle(cellStyle3);
		cell32.setCellStyle(cellStyle3);
		cell33.setCellStyle(cellStyle3);

		i = createRecodSyrovyna(sheet, i, order.getListSyrovyna());

		return ++i;
	}

	private static int createRecodSyrovyna(HSSFSheet sheet, int i, List<Syrovyna> list) {
		for (Syrovyna s : list) {
			Row row4 = sheet.createRow(i++);
			Cell cell40 = row4.createCell(0);
			Cell cell41 = row4.createCell(1);
			Cell cell42 = row4.createCell(2);
			Cell cell43 = row4.createCell(3);
			cell40.setCellValue(s.getName());
			cell41.setCellValue(s.getCount());
			cell42.setCellValue(s.getPartner());
			cell43.setCellValue(s.getPrevDoc().substring(s.getPrevDoc().indexOf("(") + 1, s.getPrevDoc().indexOf(")")));
			CellStyle cellStyle4 = row4.getSheet().getWorkbook().createCellStyle();
			cellStyle4.setBorderBottom(BorderStyle.MEDIUM);
			cellStyle4.setBorderLeft(BorderStyle.MEDIUM);
			cellStyle4.setBorderRight(BorderStyle.MEDIUM);
			cellStyle4.setBorderTop(BorderStyle.MEDIUM);
			cell40.setCellStyle(cellStyle4);
			cell41.setCellStyle(cellStyle4);
			cell42.setCellStyle(cellStyle4);
			cell43.setCellStyle(cellStyle4);
		}
		return i;
	}

	private static void createHeader(HSSFSheet sheet, LocalDate ld) {
		Row row0 = sheet.createRow(0);
		Cell cell00 = row0.createCell(0);
		Cell cell02 = row0.createCell(2);
		cell00.setCellValue("ТзОВ \"Хінкель-Когут\"");
		cell02.setCellValue("Затверджую");

		Row row1 = sheet.createRow(1);
		Cell cell12 = row1.createCell(2);
		cell12.setCellValue("Директор");

		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 3));
		sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 3));

		Row row3 = sheet.createRow(3);
		Cell cell30 = row3.createCell(0);
		cell30.setCellValue("Акт переробки сировини");
		CellStyle cellStyle3 = row3.getSheet().getWorkbook().createCellStyle();
		Font fcs3 = row3.getSheet().getWorkbook().createFont();
//		fcs3.setBoldweight((short) 1000);
		fcs3.setTypeOffset((short) 2000);
		fcs3.setFontHeight((short) 500);
		cellStyle3.setAlignment(HorizontalAlignment.CENTER);
		cellStyle3.setFont(fcs3);
		cell30.setCellStyle(cellStyle3);

		Row row4 = sheet.createRow(4);
		Cell cell40 = row4.createCell(0);
		CellStyle cellStyle4 = row4.getSheet().getWorkbook().createCellStyle();
		cellStyle4.setAlignment(HorizontalAlignment.CENTER);
		cell40.setCellStyle(cellStyle4);
		cell40.setCellValue("від " + ld.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "р.");

	}
}
