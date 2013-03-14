package com.plastku.pingallery.util;

import java.util.Formatter;



public class StringUtils
{
	public static String minutesToHours(int minutes)
	{
		//minutes/60.0
		Formatter formatter = new Formatter();
		Formatter format = formatter.format("%.1f", minutes/60.0f);
		return format.toString();
	}

	public static boolean isEmptyOrNull(String value)
	{
		return ( value == null || "".equals(value));
	}
}