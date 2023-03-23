package hk;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {

	private LocalDate date;
	private String number;
	private String product;
	private double countProduct;
	List<Syrovyna> listSyrovyna = new ArrayList<Syrovyna>();

	public Order() {
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public double getCountProduct() {
		return countProduct;
	}

	public void setCountProduct(double countProduct) {
		this.countProduct = countProduct;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public List<Syrovyna> getListSyrovyna() {
		return listSyrovyna;
	}

	@Override
	public String toString() {
		return "Order [date=" + date + ", number=" + number + ", product=" + product + ", listSyrovyna=" + listSyrovyna
				+ ", countProduct=" + countProduct + "]";
	}

}
