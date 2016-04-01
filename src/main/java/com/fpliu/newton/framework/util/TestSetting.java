package com.fpliu.newton.framework.util;

public class TestSetting {

	public static class TaskTestSettings {
		public static String ViewFormMessage = "";

		public static boolean RefreshAccesstoken = false;
		public static boolean LonginFirst = true;

		public static int totalPageNumber = 1;

		public static String TaskJsonStepsHtml = "";

		public static void setTotalPageNumber(int totalPageNumber) {
			TaskTestSettings.totalPageNumber = totalPageNumber;
		}

		public static boolean isRefreshAccesstoken() {
			return RefreshAccesstoken;
		}

		public static void setRefreshAccesstoken(boolean refreshAccesstoken) {
			RefreshAccesstoken = refreshAccesstoken;
		}

	}

	public static class TraceSettings {
		public static int TraceMessageType_FIRSTOPEN = 0;
		public static int TraceMessageType_REGISTER = 1;
		public static int TraceMessageType_ONBINDACCOUNT = 2;
		public static int TraceMessageType_LOGIN = 3;
		public static int TraceMessageType_CRASH = 99;

		public static int TraceMessageType = TraceMessageType_FIRSTOPEN;
	}

}
