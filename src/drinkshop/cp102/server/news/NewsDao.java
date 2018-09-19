package drinkshop.cp102.server.news;

import java.util.List;





	public interface NewsDao {
		int insert(News news, byte[] image);

		int update(News news, byte[] image);

		int delete(int id);

		News findById(int id);

		List<News> getAllNews();  

		byte[] getImage(int id); //給id取圖
		
		List<News> getNews_activity_id();
	}



