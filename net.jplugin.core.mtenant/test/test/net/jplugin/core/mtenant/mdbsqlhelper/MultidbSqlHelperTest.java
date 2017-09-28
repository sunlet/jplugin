package test.net.jplugin.core.mtenant.mdbsqlhelper;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.mtenant.handler.MultiDbSqlHelper;

public class MultidbSqlHelperTest {

	public void test(){
		String source,target;
		
		source = "select * from abc";
		target = "select * from sss.abc";
		check(source,target);

		source = "select * from abc;select * from xyz";
		target = "select * from sss.abc ; select * from sss.xyz";
		check(source,target);
		
		source = "select * from (select * from abc)";
		target = "select * from( select * from sss.abc )";
		check(source,target);
		

		source = "Delete From tb201709_GCategoryStockReportPro Where   AccDate=?;    /*将数据存储到临时表*/   INSERT INTO   tb201709_GCategoryStockReportPro(AccDate,DeptCode,DeptCategoryCode,GoodsCategoryCode,CategoryLevel,StockCost,SaleCost,ActiveSKU,HighStockSKU,ZeroSaleSKU,FrozenSKU,NegativeOnHandSKU,OutOfStockSKU,   ApplyMoney,OrderMoney,HighStockAgeSKU)   SELECT   AccDate,DeptCode,DeptCategoryCode,GoodsCategoryCode,CategoryLevel,StockCost,SaleCost,ActiveSKU,HighStockSKU,ZeroSaleSKU,FrozenSKU,NegativeOnHandSKU,OutOfStockSKU,   ApplyMoney,OrderMoney,HighStockAgeSKU   FROM   tb201709_GCategoryStockReport   Where   AccDate=?;             update sb    set    sb.DeptCategoryCode=s.CategoryCode,    sb.DeptCategoryName=s.CategoryName    from tb201709_GCategoryStockReportPro sb,tbDeptCategory s    where SUBSTRING(sb.DeptCategoryCode,1,LEN(s.CategoryCode))=s.CategoryCode    and s.CategoryLevel=1    and s.CategoryItemCode='0000';             update sb    set    sb.DeptCategoryCode=s.CategoryCode,    sb.DeptCategoryName=s.CategoryName    from tb2017_GCategoryStockReportPro sb,tbDeptCategory s    where SUBSTRING(sb.DeptCategoryCode,1,LEN(s.CategoryCode))=s.CategoryCode    and s.CategoryLevel=1    and s.CategoryItemCode='0000';             update sb    set    sb.DeptCategoryCode=s.CategoryCode,    sb.DeptCategoryName=s.CategoryName    from tbGCategoryStockReportPro sb,tbDeptCategory s    where SUBSTRING(sb.DeptCategoryCode,1,LEN(s.CategoryCode))=s.CategoryCode    and s.CategoryLevel=1    and s.CategoryItemCode='0000';                UPDATE tb201709_GCategoryStockReportPro    SET StockCostTBJE=A.StockCost,    SaleCostTBJE=A.SaleCost    FROM tb201709_GCategoryStockReportPro Z,    (    SELECT     AccDate,DeptCode,DeptCategoryCode,GoodsCategoryCode,CategoryLevel,StockCost,SaleCost     FROM tb201609_GCategoryStockReport     where AccDate=CONVERT(varchar(100), DateAdd(year,-1,cast(? as date)), 112)    ) A    WHERE Z.AccDate =CONVERT(varchar(100), DateAdd(year,1,cast(A.AccDate as    date)), 112) AND Z.DeptCode= A.DeptCode AND Z.DeptCategoryCode=    A.DeptCategoryCode    AND Z.GoodsCategoryCode = A.GoodsCategoryCode AND Z.CategoryLevel =    A.CategoryLevel AND Z.AccDate=?;             UPDATE tb201709_GCategoryStockReportPro    SET StockCostHBJE=A.StockCost,    SaleCostHBJE=A.SaleCost    FROM tb201709_GCategoryStockReportPro Z,    (    SELECT     AccDate,DeptCode,DeptCategoryCode,GoodsCategoryCode,CategoryLevel,StockCost,SaleCost     FROM tb201709_GCategoryStockReport      where AccDate=CONVERT(varchar(100), DateAdd(Day,-1,cast(? as date)), 112)    ) A    WHERE Z.AccDate =CONVERT(varchar(100), DateAdd(Day,1,cast(A.AccDate as    date)), 112) AND Z.DeptCode= A.DeptCode AND Z.DeptCategoryCode=    A.DeptCategoryCode    AND Z.GoodsCategoryCode = A.GoodsCategoryCode AND Z.CategoryLevel =    A.CategoryLevel AND Z.AccDate=?;";
		target = MultiDbSqlHelper.handle(source, "sss");
		System.out.println(target);
	}

	private void check(String source, String target) {
		
		String t = MultiDbSqlHelper.handle(source, "sss");
		AssertKit.assertEqual(t,target);
	}
}
