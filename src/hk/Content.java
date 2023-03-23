package hk;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;

public class Content {

	private List<String> data;
	private String delim = "\n";

	public Content(Row r) {
		data = extractListFromRow(r);
	}

	private List<String> extractListFromRow(Row r) {
		String[] arr = r.getCell(2).getStringCellValue().split(delim);
		List<String> data = new ArrayList<>();
		for (String s : arr) {
			data.add(s.trim());
		}
		return data;
	}

	public List<String> getData() {
		return data;
	}

}
