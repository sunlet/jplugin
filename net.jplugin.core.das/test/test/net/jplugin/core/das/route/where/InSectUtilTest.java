package test.net.jplugin.core.das.route.where;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.das.route.api.DataSourceInfo;
import net.jplugin.core.das.route.impl.sqlhandler2.InSectUtil;

public class InSectUtilTest {

	public void test(){
		test6();
		test5();
		test1();
		test2();
		test3();
		test4();
		
	}
	public void test6(){
		DataSourceInfo ds1,ds2,ds3,ds4;
		DataSourceInfo[] arr1,arr2,arr3;
		
		ds1 = DataSourceInfo.build("ds1");
		arr1 = new DataSourceInfo[]{ds1};
		
		ds1 = DataSourceInfo.build("ds1", "t11","t12");
		ds3 = DataSourceInfo.build("ds3", "t31","t32");
		arr2 = new DataSourceInfo[]{ds1,ds3};
		
		DataSourceInfo[] result = InSectUtil.computeInsect(buildList(arr1,arr2));
		print (result);
		AssertKit.assertEqual(0, getCount(result));
	}
	public void test5(){
		DataSourceInfo ds1,ds2,ds3,ds4;
		DataSourceInfo[] arr1,arr2,arr3;
		
		ds1 = DataSourceInfo.build("ds1");
		ds2 = DataSourceInfo.build("ds2", "t21","t22");
		ds3 = DataSourceInfo.build("ds3");
		arr1 = new DataSourceInfo[]{ds1,ds2,ds3};
		
		ds1 = DataSourceInfo.build("ds1", "t11","t12");
		ds3 = DataSourceInfo.build("ds3", "t31","t32");
		arr2 = new DataSourceInfo[]{ds1,ds3};
		
		DataSourceInfo[] result = InSectUtil.computeInsect(buildList(arr1,arr2));
		print (result);
		AssertKit.assertEqual(0, getCount(result));
	}
	public void test4(){
		DataSourceInfo ds1,ds2,ds3,ds4;
		DataSourceInfo[] arr1,arr2,arr3;
		
		ds1 = DataSourceInfo.build("ds1");
		ds2 = DataSourceInfo.build("ds2", "t21","t22");
		ds3 = DataSourceInfo.build("ds3");
		arr1 = new DataSourceInfo[]{ds1,ds2,ds3};
		
		ds1 = DataSourceInfo.build("ds1", "t11","t12");
		ds2 = DataSourceInfo.build("ds2", "t22");
		ds3 = DataSourceInfo.build("ds3", "t31","t32");
		arr2 = new DataSourceInfo[]{ds1,ds2,ds3};
		
		DataSourceInfo[] result = InSectUtil.computeInsect(buildList(arr1,arr2));
		print (result);
		AssertKit.assertEqual(1, getCount(result));
	}
	public void test3(){
		DataSourceInfo ds1,ds2,ds3,ds4;
		DataSourceInfo[] arr1,arr2,arr3;
		
		ds1 = DataSourceInfo.build("ds1", "t11","t12");
		ds2 = DataSourceInfo.build("ds2", "t21");
		ds3 = DataSourceInfo.build("ds3", "t31");
		arr1 = new DataSourceInfo[]{ds1,ds2,ds3};
		
		ds1 = DataSourceInfo.build("ds1", "t11","t12");
		ds2 = DataSourceInfo.build("ds2", "t21","t22");
		ds3 = DataSourceInfo.build("ds3");
		arr2 = new DataSourceInfo[]{ds1,ds2,ds3};
		
		DataSourceInfo[] result = InSectUtil.computeInsect(buildList(arr1,arr2));
		print (result);
		AssertKit.assertEqual(3, getCount(result));
	}
	public void test2(){
		DataSourceInfo ds1,ds2,ds3,ds4;
		DataSourceInfo[] arr1,arr2,arr3;
		
		ds1 = DataSourceInfo.build("ds1", "t11","t12");
		ds2 = DataSourceInfo.build("ds2", "t21");
		ds3 = DataSourceInfo.build("ds3", "t31");
		arr1 = new DataSourceInfo[]{ds1,ds2,ds3};
		
		ds1 = DataSourceInfo.build("ds1", "t11","t12");
		ds2 = DataSourceInfo.build("ds2", "t21","t22");
		ds3 = DataSourceInfo.build("ds3", "t31","t32");
		arr2 = new DataSourceInfo[]{ds1,ds2,ds3};
		
		DataSourceInfo[] result = InSectUtil.computeInsect(buildList(arr1,arr2));
		print (result);
		AssertKit.assertEqual(4, getCount(result));
	}
	public void test1(){
		DataSourceInfo ds1,ds2,ds3,ds4;
		DataSourceInfo[] arr1,arr2,arr3;
		
		ds1 = DataSourceInfo.build("ds1", "t11","t12");
		ds2 = DataSourceInfo.build("ds2", "t21","t22");
		ds3 = DataSourceInfo.build("ds3", "t31","t32");
		arr1 = new DataSourceInfo[]{ds1,ds2,ds3};
		
		ds1 = DataSourceInfo.build("ds1", "t11","t12");
		ds2 = DataSourceInfo.build("ds2", "t21","t22");
		ds3 = DataSourceInfo.build("ds3", "t31","t32");
		arr2 = new DataSourceInfo[]{ds1,ds2,ds3};
		
		DataSourceInfo[] result = InSectUtil.computeInsect(buildList(arr1,arr2));
		print (result);
		AssertKit.assertEqual(6, getCount(result));
	}
	
	private Object getCount(DataSourceInfo[] result) {
		int r = 0;
		for (DataSourceInfo d:result){
			r += d.getDestTbs().length;
		}
		return r;
	}

	private void print(DataSourceInfo[] result) {
		System.out.println("\n\n===================");
		for (DataSourceInfo r:result){
			System.out.println(r);
		}
	}

	private List<DataSourceInfo[]> buildList(DataSourceInfo[] ... arr) {
		List l = new ArrayList<>();
		for (DataSourceInfo[] a:arr){
			l.add(a);
		}
		return l;
	}

	public static void main(String[] args) {
		new InSectUtilTest().test();
	}
}
