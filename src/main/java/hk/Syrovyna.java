package hk;

public class Syrovyna {
	private String name;
	private double count;
	private String prevDoc;
	private String partner;

	public Syrovyna() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	public String getPrevDoc() {
		return prevDoc;
	}

	public void setPrevDoc(String prevDoc) {
		this.prevDoc = prevDoc;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	@Override
	public String toString() {
		return "Syrovyna [name=" + name + ", count=" + count + ", prevDoc=" + prevDoc + ", partner=" + partner + "]";
	}

}
