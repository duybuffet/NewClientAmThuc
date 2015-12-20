package com.example.connect;

public class GLOBAL {
	public static class TO_SERVER {

		public static final int CONNECT = 1;
		public static final int LOGIN = 2;
		public static final int SIGNUP = 3;
		public static final int LOGOUT = 4;
		public static final int CHAT = 5;
		public static final int LIST_CATEGORY = 6;
		public static final int ADD_ORDER = 7;
		public static final int SHOW_ORDER = 8;
		public static final int LOAD_TABLES = 9;
		public static final int RELOAD_TABLES = 10;
		public static final int UPDATE_ORDER = 11;
		public static final int ADD_LINE_TO_ORDER = 12;
		public static final int UPDATE_LINE_OF_ORDER = 13;
		public static final int DELETE_LINE_OF_ORDER = 14;
		public static final int CANCEL_ORDER = 15;
		public static final int BILL = 16;
		public static final int LOAD_ORDER_DETAILS = 17;
        public static final int UPDATE_ORDER_DETAILS_STATUS = 18;
        public static final int RELOAD_ORDER_DETAILS = 19;
	}

	public static class FROM_SERVER {
		public static final int CONNECTED = 1;
		public static final int LOGIN = 2;
		public static final int SIGNUP = 3;
		public static final int UPDATE_USERS_LIST = 4;
		public static final int TIMEOUT = -1;
		public static final int CHAT = 5;
		public static final int RECEIVED_CHAT_MESSAGE = 6;
		public static final int LIST_CATEGORY = 7;
		public static final int ADD_ORDER = 8;
		public static final int SHOW_ORDERS = 9;
		public static final int LOAD_TABLES = 10;
		public static final int RELOAD_TABLES = 11;
		public static final int UPDATE_ORDER = 12;
		public static final int ADD_LINE_TO_ORDER = 13;
		public static final int UPDATE_LINE_OF_ORDER = 14;
		public static final int DELETE_LINE_OF_ORDER = 15;
		public static final int CANCEL_ORDER = 16;
		public static final int BILL = 17;
		public static final int LOAD_ORDER_DETAILS = 18;
        public static final int UPDATE_ORDER_DETAILS_STATUS = 19;
        public static final int RELOAD_ORDER_DETAILS = 20;
	}

	public static class ORDER_AND_TABLE_STATUS {
		public static final int TABLE_FREE = 0;
		public static final int NOT_YET_ORDER = 1;
		public static final int ORDERED = 2;
		public static final int ORDER_READY_TO_SERVED = 3;
		public static final int ORDER_SERVED = 4;
		public static final int ORDER_BILLED = 5;
		public static final int ORDER_CANCEL = 6;
	}
	
	public static class ORDER_DETAILS_STATUS {
        public static final int WAITING = 0;
        public static final int FINISH = 1;
        public static final int CANCEL = 2;
        public static final String[] DISPLAY = {"ĐANG CHỜ", "ĐANG THỰC HIỆN", "HOÀN THÀNH", "HỦY"};
    }

	public static class CONFIG {
		public static int PORT = 2015;
		public static String IPSERVER = "192.168.1.101";

		public static String SUCCESS = "SUCCESS";
		public static String FAIL = "FAIL";
		public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
		public static final String DATE_DISPLAY_FORMAT = "dd/MM/yyyy";
	}

	public static class AREA {
		public static final int FLOOR_1 = 1;
		public static final int FLOOR_2 = 2;
		public static final int FLOOR_3 = 3;
	}

	public static class TABLE_TYPE {
		public static final int TABLE_2 = 2;
		public static final int TABLE_4 = 4;
		public static final int TABLE_8 = 8;
	}

	public static class CONSTANTS {
		public static final String TOTAL_TITLE = "THÀNH TIỀN : ";
		public static final String ITEMS_TOTAL_TITLE = "Tiền món : ";
		public static final String PRICE_UNIT = " VNĐ";
		public static final String ORDER_ITEMS_QTT = "Lượng món : ";
		
	}
	
	public static class USER_LEVEL {
        public static final int ADMIN = 2;
        public static final int CHEF = 1;
        public static final int WAITER = 0;
    }
}
