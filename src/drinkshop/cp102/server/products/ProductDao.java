package drinkshop.cp102.server.products;

import java.util.List;

public interface ProductDao {
	int insert(Product spot, byte[] image);

	int update(Product spot, byte[] image);

	int delete(int id);

	Product findById(int id);

	byte[] getImage(int id);

	List<Product> getAllProduct();

	List<Product> getProductDetail(int searchID);
}
