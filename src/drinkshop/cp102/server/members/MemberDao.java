package drinkshop.cp102.server.members;

import java.util.List;

public interface MemberDao {
	int insert(Member member);
	int update(Member member);
	int delete(int member_id);
	Member findById(int member_id);
	List<Member> getAll();
}
