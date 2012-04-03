package com.mie.mbc.assignment2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.database.Cursor;

import com.mie.mbc.assignment2.Adapters.CursorTransformation;

public class DateExtract extends CursorTransformation {

	public DateExtract(Context context) {
		super(context);
	}

	/**
	 * @param cursor
	 * @param columnIndex
	 */
	@Override
	public String transform(Cursor cursor, int columnIndex) {
		Date returnDate = new Date(cursor.getLong(columnIndex));
		return returnDate.toLocaleString();
	}

	public Date getDate(String unparsedDate) {
		return pullDateOut(unparsedDate);
	}

	private Date pullDateOut(String unparsedDate) {
		String timePattern = ".+<p>Date: ((Mon|Tue|Wed|Thu|Fri|Sat|Sun).+) UTC<br/>.+";
		Pattern pattern = Pattern.compile(timePattern);
		Matcher matcher = pattern.matcher(unparsedDate);
		boolean matchFound = matcher.find();
		Date date = null;
		if (matchFound) {
			SimpleDateFormat sdf = new SimpleDateFormat(
					"EEE, dd MMM yyyy HH:mm:ss");
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			try {
				String dateText = matcher.group(1);
				date = sdf.parse(dateText);
			} catch (ParseException e) {
				date = new Date();
				e.printStackTrace();
			}
		}

		return date;
	}

}
