package hk;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import scala.Tuple2;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.Entry;

public class Main {
	private static File saveDir;
	private static final String dir = "orders";
	private static final Map<String, String> reestrPN = new HashMap<>();
	private static final Map<String, String> reestrPerem = new HashMap<>();
	private static boolean needCreateDir = true;
	private static int countSavedFiles = 0;

	public static void main(String[] args) throws IOException {
		Set<String> mvos = new TreeSet<>();
		List<Record> records = new ArrayList<>();
		File[] files = new File(".").listFiles();

		Map<String, Set<Tuple2>> mvoKalk = new HashMap<>();
		if (files != null) {
			init(mvos, records, files);
		}
		if (needCreateDir) {
			saveDir = new File("./" + dir);
			saveDir.mkdir();
		}
		Map<String, Set<LocalDate>> mapMvoDate = prepareForAggregate(mvos, records, mvoKalk);
		Map<String, List<Order>> mapMvoKalk = aggregate(records, mvoKalk);
		saveOrders(mapMvoDate, mapMvoKalk);
		String pathToFile = new File(".").getAbsolutePath();
		String message = "Created " + countSavedFiles + " files in:\n"
				+ pathToFile.substring(0, pathToFile.length() - 1) + dir;
		JOptionPane.showMessageDialog(null, message, "Complete", JOptionPane.INFORMATION_MESSAGE);
	}

	private static void init(Set<String> mvos, List<Record> records, File[] files) {
		for (File f : files) {
			if (f.isDirectory() && f.getName().equals(dir)) {
				Helper.deleteFile(f);
				saveDir = new File("./" + dir);
				saveDir.mkdir();
				needCreateDir = false;
			}
			if (f.isFile() && f.getName().contains("xls")) {
				try {
					try (Workbook wb = WorkbookFactory.create(f)) {
						readAndCreateRecords(mvos, records, wb);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void readAndCreateRecords(Set<String> mvos, List<Record> records, Workbook wb) {
		for (Row r : wb.getSheetAt(0)) {
			Record record = new Record(r);
			if (Helper.isRecordForMvo(record)) {
				record.setMvo(record.getContent().getData().get(3));
				mvos.add(record.getMvo());
				reestrPerem.put(
						record.getDoc().substring(7) + " ("
								+ record.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yy")) + ")",
						record.getContent().getData().get(5));
			}
			if (Helper.isRecordForPNInfo(record)) {
//				record.setProduct(record.getContent().getData().get(2));
				record.setPartner(record.getContent().getData().get(4));
				record.setDocWithDate(record.getContent().getData().get(3));
				reestrPN.put(record.getDocWithDate(), record.getPartner());
			}
			records.add(record);
		}
	}

	private static Map<String, Set<LocalDate>> prepareForAggregate(Set<String> mvos, List<Record> records,
			Map<String, Set<Tuple2>> mvoKalk) {
		Map<String, Set<LocalDate>> mapMvoDate = new HashMap<>();
		for (String mvo : mvos) {
			Set<Tuple2> kalksForMap = new HashSet<>();
			Set<LocalDate> setForMapMvoDate = new HashSet<>();
			for (Record r : records) {
				if (Helper.isRecordForMvo(r) && mvo.equals(r.getMvo())) {
					kalksForMap.add(new Tuple2(r.getDoc(), r.getDate()));
					setForMapMvoDate.add(r.getDate());
				}
			}
			mvoKalk.put(mvo, kalksForMap);
			mapMvoDate.put(mvo, setForMapMvoDate);
		}
		return mapMvoDate;
	}

	private static Map<String, List<Order>> aggregate(List<Record> records, Map<String, Set<Tuple2>> mvoKalk) {
		Map<String, List<Order>> mapMvoKalk = new HashMap<>();
		for (Entry<String, Set<Tuple2>> entry : mvoKalk.entrySet()) {
			List<Order> listForMapKalkData = new ArrayList<>();
			for (Tuple2 kalkDate : entry.getValue()) {
				Order order = new Order();
				order.setDate((LocalDate) kalkDate._2());
				order.setNumber(kalkDate._1().toString());
				for (Record r : records) {
					if (r.getDoc().equals(kalkDate._1()) && r.getDate().equals(kalkDate._2())) {
						if (Helper.isCanSetProduct(r)) {
							order.setProduct(r.getContent().getData().get(2));
							order.setCountProduct(r.getCount());
						}
						if (Helper.isUse_25(r)) {
							setSyrovyna(order, r, true);
						}
						if (Helper.isUse_201(r)) {
							setSyrovyna(order, r, false);
						}
					}
				}
				listForMapKalkData.add(order);
			}
			mapMvoKalk.put(entry.getKey(), listForMapKalkData);
		}
		return mapMvoKalk;
	}

	private static void saveOrders(Map<String, Set<LocalDate>> mapMvoDate, Map<String, List<Order>> mapMvoKalk)
			throws IOException {
		for (Entry<String, List<Order>> item : mapMvoKalk.entrySet()) {
			Set<LocalDate> setLocalDate = mapMvoDate.get(item.getKey());
			String dirForOrdersMvo = "./" + dir + "/" + item.getKey();
			File dirForMvo = new File(dirForOrdersMvo);
			dirForMvo.mkdir();
			for (LocalDate localDate : setLocalDate) {
				File mvoOrder = new File(
						dirForOrdersMvo + "/" + localDate.format(DateTimeFormatter.ofPattern("yyyy_MM_dd")) + ".xls");
				List<Order> ordersToFile = new ArrayList<>();
				for (Order order : item.getValue()) {
					if (order.getDate().equals(localDate)) {
						ordersToFile.add(order);
					}
				}
				Helper.createWorkbook(mvoOrder, ordersToFile, localDate, item.getKey());
				countSavedFiles++;
			}
		}
	}

	private static void setSyrovyna(Order order, Record r, boolean needUseReestrPerem) {
		Syrovyna syrovyna = new Syrovyna();
		syrovyna.setCount(r.getCount());
		syrovyna.setName(r.getContent().getData().get(4));
		syrovyna.setPrevDoc(r.getContent().getData().get(5));
		if (needUseReestrPerem) {
			syrovyna.setPartner(reestrPN.get(reestrPerem.get(syrovyna.getPrevDoc())));
		} else {
			syrovyna.setPartner(reestrPN.get(syrovyna.getPrevDoc()));
		}
		order.getListSyrovyna().add(syrovyna);
	}

}
