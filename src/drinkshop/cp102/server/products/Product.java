package drinkshop.cp102.server.products;

public class Product {

	private int id;
	private String name;
	private String phoneNo;
	private String address;
	private double latitude;
	private double longitude;

	public Product(int id, String name, String phoneNo, String address,
			double latitude, double longitude) {
		this.id = id;
		this.name = name;
		this.phoneNo = phoneNo;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
