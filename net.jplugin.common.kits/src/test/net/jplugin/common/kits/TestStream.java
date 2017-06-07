package test.net.jplugin.common.kits;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;

public class TestStream {
	// 中间操作：
	// filter()： 对元素进行过滤
	// sorted()：对元素排序
	// map()：元素映射
	// distinct()：去除重复的元素

	// 最终操作：
	// forEach()：遍历每个元素。
	// reduce()：把Stream 元素组合起来。例如，字符串拼接，数值的 sum，min，max ，average 都是特殊的 reduce。
	// collect()：返回一个新的集合。
	// min()：找到最小值。
	// max()：找到最大值。
	public void test() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(3);
		list.add(4);
		list.add(3);
		list.add(4);

		//
		System.out.println("Test1 sorta.....");
		Optional<Integer> result = list.stream().sorted((c1,c2)->(c1-c2)).reduce((t,u)->t+u);
		System.out.println(result.orElse(100));
		
		System.out.println("Test2.....");
		//lamda两个参数时，需要用括号,另外，可以冒号表示法
		list.stream().sorted((c1, c2) -> c1 - c2)
		.forEach(System.out::println);

		System.out.println("Test3.....");
		//lamda一个参数时候，可以不带括号
		list.stream().sorted((c1, c2) -> (c1-c2))
		.forEach(c->System.out.println(c));

		System.out.println("Test4 多个语句......");
		//多个语句需要用大括号，如果需要return，则最后一句需要
		list.stream().sorted((c1, c2) -> (c1-c2))
		.forEach((c) -> {System.out.println(c+Thread.currentThread().getName());try {
			Thread.sleep(1);
		} catch (Exception e) {
			e.printStackTrace();
		}});
	}

	public static void main(String[] args) {
		new TestStream().test();
	}
}
