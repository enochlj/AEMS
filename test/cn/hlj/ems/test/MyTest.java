package cn.hlj.ems.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

public class MyTest {

	@Test
	public void test01() {
		System.out.println(null instanceof Integer);
	}

	@Test
	public void test02() {
		String str1 = "班主任,讲师，Cydia";
		String str2 = "讲师";
		String str3 = "管理员,MicWin,讲师";

		String[] strs = { str1, str2, str3 };
		for (int i = 0; i < strs.length; i++) {
			strs[i] = strs[i].replace("，", ",");
			String[] split = strs[i].split(",");
			System.out.print(split.length + " : ");
			for (String s : split) {
				System.out.print(s + "\t");
			}
			System.out.println();
		}
	}

	@Test
	public void test03() {
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();

		String encodePasswordForAdm = encoder.encodePassword("123456", "adminAA");
		System.out.println(encodePasswordForAdm);// b3a6f3c3937d6d6e8da101c7e766cfcb

		String encodePasswordForBillLogin = encoder.encodePassword("123456", "BillLogin");
		System.out.println(encodePasswordForBillLogin);// 48ca6ba30ab8304643646f7f464a90e7

		String encodePasswordForAEageUser01 = encoder.encodePassword("123456", "AEageUser01");
		System.out.println(encodePasswordForAEageUser01);// a22f7aeafb317ec6e2d342624c45a747

		String encodePasswordForOnlyModiEmp01 = encoder.encodePassword("123456", "OnlyModiEmp01");
		System.out.println(encodePasswordForOnlyModiEmp01);// 21d43239ea954c606d1ab1a0619c0447

		String encodePasswordForOnlyDelEmp01 = encoder.encodePassword("123456", "OnlyDelEmp01");
		System.out.println(encodePasswordForOnlyDelEmp01);// a886e08a070f3d518d9c5a17233d7aeb
	}

	@Test
	public void test04() {
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(1);
		list.add(2);
		list.add(2);
		list.add(3);
		list.add(3);
		list.add(3);
		list.add(4);

		System.out.println(list.size());

		List<Integer> list2 = trim(list);
		System.out.println(list2.size());
	}

	public List<Integer> trim(List<Integer> list) {
		HashSet<Integer> set = new HashSet<>();
		set.addAll(list);

		return new ArrayList<Integer>(set);
	}

}
