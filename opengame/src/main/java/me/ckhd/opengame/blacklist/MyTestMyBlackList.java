package me.ckhd.opengame.blacklist;


public class MyTestMyBlackList {
	public static void main(String argv[]) {
		new MyTestMyBlackList();
	}

	public MyTestMyBlackList() {
		MyBlackList aBlackList = new MyBlackList();
		if (1 != aBlackList.init()) {
			System.out.printf("init failed\r\n");
			return;
		}
		if (1 != aBlackList.isInBlackList("460001200567927")) {
			System.out.printf("error for 460001200567927\r\n");
		} else {
			System.out.printf("succ for 460001200567927\r\n");
		}

		if (1 != aBlackList.isInBlackList("460001200567923")) {
			System.out.printf("succ for 460001200567923\r\n");
		} else {
			System.out.printf("fail for 460001200567923\r\n");
		}

		if (1 != aBlackList.isInBlackList("555001200567923")) {
			System.out.printf("succ for 555001200567923\r\n");
		} else {
			System.out.printf("fail for 555001200567923\r\n");
		}
		System.out.println("460023130972621:" + BlackListUtils.getInstance().isBlackList("460023130972621"));
		System.out.println("460005452965220:" + BlackListUtils.getInstance().isBlackList("460005452965220"));
		
	}
}