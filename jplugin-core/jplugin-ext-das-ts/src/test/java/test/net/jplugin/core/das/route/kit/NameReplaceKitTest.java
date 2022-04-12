package test.net.jplugin.core.das.route.kit;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.das.dds.kits.SqlParserKit;
import net.jplugin.core.das.route.impl.sqlhandler2.TableNameReplacerKit;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

public class NameReplaceKitTest {

	public static void main(String[] args) {
		new NameReplaceKitTest().test();
	}
	
	public void test(){
		testSelect();
		testUpdate();
		testDelete();
	}
	
	private void testUpdate() {
		Update upd = getUpdate("update tb1 set f1=1 where  tb1.f1 = 1");
		TableNameReplacerKit.handleUpdate(upd, "AAA");
//		System.out.println(upd.toString());
		AssertKit.assertEqual("UPDATE AAA SET f1 = 1 WHERE AAA.f1 = 1",upd.toString());
		
		upd = getUpdate("update tb1 tt set f1=1 where  tb1.f1 = 1");
		TableNameReplacerKit.handleUpdate(upd, "AAA");
//		System.out.println(upd.toString());
		AssertKit.assertEqual("UPDATE AAA tt SET f1 = 1 WHERE tb1.f1 = 1",upd.toString());
	}

	private void testDelete() {
		Delete del = getDelete("delete from  tb1 where  tb1.f1 = 1");
		TableNameReplacerKit.handleDelete(del, "AAA");
//		System.out.println(del.toString());
		AssertKit.assertEqual("DELETE FROM AAA WHERE AAA.f1 = 1",del.toString());
		
		del = getDelete("delete from  tb1 t where  tb1.f1 = 1");
		TableNameReplacerKit.handleDelete(del, "AAA");
//		System.out.println(del.toString());
		AssertKit.assertEqual("DELETE FROM AAA t WHERE tb1.f1 = 1",del.toString());
		
	}

	public void testSelect(){
		
		
		
		PlainSelect ps = getSelect("select * from tb1");
		TableNameReplacerKit.handleSelect(ps, "AAA");
		AssertKit.assertEqual("SELECT * FROM AAA",ps.toString());
		
		
		ps = getSelect("select count(*) from tb_route0 where f1='a'");
		TableNameReplacerKit.handleSelect(ps, "AAA");
		System.out.println(ps.toString());
		AssertKit.assertEqual("SELECT count(*) FROM AAA WHERE f1 = 'a'",ps.toString());
		
		
		ps = getSelect("select * from tb1 where tb1.f1 = 1");
		TableNameReplacerKit.handleSelect(ps, "AAA");
		System.out.println(ps.toString());
		AssertKit.assertEqual("SELECT * FROM AAA WHERE AAA.f1 = 1",ps.toString());
		
		ps = getSelect("select * from tb1 where TB1.f1 = 1");
		TableNameReplacerKit.handleSelect(ps, "AAA");
		System.out.println(ps.toString());
		AssertKit.assertEqual("SELECT * FROM AAA WHERE AAA.f1 = 1",ps.toString());
		
		ps = getSelect("select * from tb1 where tb2.f1 = 1");
		TableNameReplacerKit.handleSelect(ps, "AAA");
		System.out.println(ps.toString());
		AssertKit.assertEqual("SELECT * FROM AAA WHERE tb2.f1 = 1",ps.toString());
		
		ps = getSelect("select * from tb1 where tb1.f1 = 1 and tb2.f1=2");
		TableNameReplacerKit.handleSelect(ps, "AAA");
		System.out.println(ps.toString());
		AssertKit.assertEqual("SELECT * FROM AAA WHERE AAA.f1 = 1 AND tb2.f1 = 2",ps.toString());
		
		ps = getSelect("select * from student tb1 where tb1.f1 = 1");
		TableNameReplacerKit.handleSelect(ps, "AAA");
		System.out.println(ps.toString());
		AssertKit.assertEqual("SELECT * FROM AAA tb1 WHERE tb1.f1 = 1",ps.toString());
		
		ps = getSelect("select * from student tb1 where (tb1.f1 = 1 AND student.f2=2)");
		TableNameReplacerKit.handleSelect(ps, "AAA");
		System.out.println(ps.toString());
		AssertKit.assertEqual("SELECT * FROM AAA tb1 WHERE (tb1.f1 = 1 AND student.f2 = 2)",ps.toString());

		
		
	}
	
	public PlainSelect getSelect(String sql){
		Statement stmt = SqlParserKit.parse(sql);
		Select select = (Select) stmt;
		return (PlainSelect) select.getSelectBody();
	}
	
	private Update getUpdate(String sql){
		return (Update) SqlParserKit.parse(sql);
	}
	
	private Delete getDelete(String sql){
		return (Delete) SqlParserKit.parse(sql);
	}
	


}
