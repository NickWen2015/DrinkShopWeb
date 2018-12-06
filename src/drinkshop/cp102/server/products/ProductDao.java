package drinkshop.cp102.server.products;

import java.util.List;

public interface ProductDao {
	
	int insertCategory(String categoryName);
	
	List<Category> getAllCategory();
	
	int productInsert(Product product, byte[] image);

	int productUpdate(Product product, byte[] image);
	
	int getProductsVersions(int version);
	
	int productsVersionsUpdate();//更新產品版次

	int productDelete(int product_id);

	Product findById(int id);

	/**
	 * 取的商品圖片
	 * */
	byte[] getProductImage(int productID);

	/**
	 * 取的全部商品
	 * 
	 * @author mrosstro
	 * @return 傳回所有的 商品id, 類別名稱, 商品名稱, M尺寸的商品價格 ,L尺寸的商品價格
	 * */
	List<Product> getAllProduct();

	/**
	 * 取得單一商品詳細
	 * 
	 * @author mrosstro
	 * @param productID 商品id
	 * @return 商品id, 類別名稱, 商品名稱, M尺寸的商品價格 ,L尺寸的商品價格
	 * */
	List<Product> getProductDetail(int productID);
}
