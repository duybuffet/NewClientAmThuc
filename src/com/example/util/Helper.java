package com.example.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Helper {
	public static String formatNumber(Double number) {
		if (number < 1000) {
			return String.valueOf(number);
		}
		try {
			NumberFormat formatter = new DecimalFormat("###,###");
			String resp = formatter.format(number);
			resp = resp.replaceAll(",", ".");
			return resp;
		} catch (Exception e) {
			return "";
		}
	}
	
	public static String formatNumber(Float number) {
        return formatNumber(Double.parseDouble(String.valueOf(number)));
    }
}
