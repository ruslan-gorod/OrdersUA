package hk;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.apache.poi.ss.usermodel.Row;

public class Record {
    private String dt;
    private String kt;
    private String doc;
    private Content content;
	private LocalDate date;
    private double count;
    private double sum;
    private String mvo;
	private String product;
	private String docWithDate;
	private String partner;
	
	public void setProduct(String product) {
		this.product = product;
	}

	public String getDocWithDate() {
		return docWithDate;
	}

	public void setDocWithDate(String docWithDate) {
		this.docWithDate = docWithDate;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
    
	@Override
	public String toString() {
		return dt + " " + kt + " - " + getContent();
	}
	
    public Record (Row r){
		try {
			setDate(LocalDate.parse(r.getCell(0).getStringCellValue(), formatter));
			setDoc(r.getCell(1).getStringCellValue());
			setContent(new Content(r));
			setDt(r.getCell(3).getStringCellValue());
			setKt(r.getCell(4).getStringCellValue());
			if (r.getCell(5).toString().trim().length() > 0) {
				setSum(r.getCell(5).getNumericCellValue());
			} else {
				setSum(0);
			}
			if (r.getCell(6).toString().trim().length() > 0) {
				setCount(r.getCell(6).getNumericCellValue());
			} else {
				setCount(0);
			}
		} catch (DateTimeParseException e) {
			System.out.println(r.getCell(0));
		}
    }
    
    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getKt() {
        return kt;
    }

    public void setKt(String kt) {
        this.kt = kt;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		long temp;
		temp = Double.doubleToLongBits(count);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((doc == null) ? 0 : doc.hashCode());
		result = prime * result + ((dt == null) ? 0 : dt.hashCode());
		result = prime * result + ((kt == null) ? 0 : kt.hashCode());
		temp = Double.doubleToLongBits(sum);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Record other = (Record) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (Double.doubleToLongBits(count) != Double.doubleToLongBits(other.count))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (doc == null) {
			if (other.doc != null)
				return false;
		} else if (!doc.equals(other.doc))
			return false;
		if (dt == null) {
			if (other.dt != null)
				return false;
		} else if (!dt.equals(other.dt))
			return false;
		if (kt == null) {
			if (other.kt != null)
				return false;
		} else if (!kt.equals(other.kt))
			return false;
		return Double.doubleToLongBits(sum) == Double.doubleToLongBits(other.sum);
	}

	public String getMvo() {
		return mvo;
	}

	public void setMvo(String mvo) {
		this.mvo = mvo;
	}
	
}
