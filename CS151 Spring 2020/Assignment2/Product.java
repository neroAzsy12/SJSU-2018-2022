/**
 * @author azsy
 * Date: 02/03/20
 */

public final class Product{
	private final String name;
	private final String description;
	private final String price;
	private final int max;
	
	public Product(String name, String description, String price, int max) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.max = max;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getPrice() {
		return this.price;
	}
	
	public int getMax() {
		return this.max;
	}
	
	public String toString() {
		return "Product name: " + this.name + ", description: " + this.description + ", price: " + this.price + ", max quantity: " + this.max ;
	}
}
