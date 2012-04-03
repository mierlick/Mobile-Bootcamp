package com.mie.mbc.assignment2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.BitSet;
import java.util.Date;
import java.util.Stack;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.util.Log;

public class QuakeXmlProvider extends ContentProvider {

	private static final String LOG_TAG = "QueryXmlProvider";
	private AndroidHttpClient mHttpClient;
	private String[] allColumns = { 
			Quake.QUAKE_ID.getValue(),
			Quake.QUAKE_TITLE.getValue() + " AS" + DatabaseUtils.sqlEscapeString(Quake.RSS_QUAKE_TITLE.getValue()), 
			Quake.QUAKE_MAGNITUDE.getValue() + " AS" + DatabaseUtils.sqlEscapeString(Quake.RSS_QUAKE_MAGNITUDE.getValue()),
			Quake.QUAKE_LATITUDE.getValue() + " AS" + DatabaseUtils.sqlEscapeString(Quake.RSS_QUAKE_LATITUDE.getValue()), 
			Quake.QUAKE_LONGITUDE.getValue() + " AS" + DatabaseUtils.sqlEscapeString(Quake.RSS_QUAKE_LONGITUDE.getValue()),
			Quake.QUAKE_LINK.getValue() + " AS" + DatabaseUtils.sqlEscapeString(Quake.RSS_QUAKE_LINK.getValue()), 
			Quake.QUAKE_TIME.getValue() + " AS" + DatabaseUtils.sqlEscapeString(Quake.RSS_QUAKE_TIME.getValue()) };

	private SQLiteHelper dbHelper;
	private static final String LAST_UPDATED = "lastUpdate.txt"; 

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count;
		count = db.delete(Quake.DATABASE_TABLE_NAME.getValue(), where, whereArgs);
		return count;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean onCreate() {
		dbHelper = new SQLiteHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		XmlPullParser parser = null;
		mHttpClient = null;
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		final String url = uri.getQueryParameter("url");
		boolean reset = Boolean.parseBoolean(uri.getQueryParameter("reset"));
		
		Date lastUpdate = new Date(getLastUpdatedDate());
		long dayInSeconds = 86400000;
		boolean updatedToday = new Date().getTime() < lastUpdate.getTime() + dayInSeconds;
		
		if (reset || !updatedToday) {
			
			if (url != null) {
				parser = getUriXmlPullParser(url);
			} 
	
			if (parser != null) {
				XMLCursor xmlCursor = new XMLCursor(selection, projection);
				try {
					xmlCursor.parseWith(parser);
	
					xmlCursor.moveToFirst();
	
					while (xmlCursor.isAfterLast() == false) {
						String quakeTime = xmlCursor.getString(6);
						Date quakeDate = new DateExtract(getContext()).getDate(quakeTime);
						ContentValues initialValues = new ContentValues();
						initialValues.put(Quake.QUAKE_TITLE.getValue(), xmlCursor.getString(1));
						initialValues.put(Quake.QUAKE_MAGNITUDE.getValue(), xmlCursor.getString(2));
						initialValues.put(Quake.QUAKE_LATITUDE.getValue(), xmlCursor.getFloat(3));
						initialValues.put(Quake.QUAKE_LONGITUDE.getValue(), xmlCursor.getFloat(4));
						initialValues.put(Quake.QUAKE_LINK.getValue(), xmlCursor.getString(5));
						initialValues.put(Quake.QUAKE_TIME.getValue(), quakeDate.getTime());
						db.replace(Quake.DATABASE_TABLE_NAME.getValue(), Quake.QUAKE_TITLE.getValue(), initialValues);
						xmlCursor.moveToNext();
					}
					writeLastUpdatedDate(new Date().getTime());
					xmlCursor.close();
				} catch (IOException e) {
					Log.w(LOG_TAG, "I/O error while parsing XML " + uri, e);
				} catch (XmlPullParserException e) {
					Log.w(LOG_TAG, "Error while parsing XML " + uri, e);
				} finally {
					if (mHttpClient != null) {
						mHttpClient.close();
					}
				}
			} else {
				return null;
			}
		}
		
		Cursor cursor = db.query(Quake.DATABASE_TABLE_NAME.getValue(), allColumns, 
				null, selectionArgs, null, null, Quake.QUAKE_TIME.getValue() + " DESC");
		
		return cursor;
	}

	/**
	 * Creates an XmlPullParser for the provided URL. Can be overloaded to
	 * provide your own parser.
	 * 
	 * @param url
	 *            The URL of the XML document that is to be parsed.
	 * @return An XmlPullParser on this document.
	 */
	protected XmlPullParser getUriXmlPullParser(String url) {
		XmlPullParser parser = null;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			parser = factory.newPullParser();
		} catch (XmlPullParserException e) {
			Log.e(LOG_TAG, "Unable to create XmlPullParser", e);
			return null;
		}

		InputStream inputStream = null;
		try {
			final HttpGet get = new HttpGet(url);
			mHttpClient = AndroidHttpClient.newInstance("Android");
			HttpResponse response = mHttpClient.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				final HttpEntity entity = response.getEntity();
				if (entity != null) {
					inputStream = entity.getContent();
				}
			}
		} catch (IOException e) {
			Log.w(LOG_TAG, "Error while retrieving XML file " + url, e);
			e.printStackTrace();
			return null;
		}

		try {
			if (inputStream != null) {
				parser.setInput(inputStream, null);
			}
		} catch (XmlPullParserException e) {
			Log.w(LOG_TAG, "Error while reading XML file from " + url, e);
			e.printStackTrace();
			return null;
		}

		return parser;
	}
	
	private void writeLastUpdatedDate(Long time) {
		OutputStreamWriter out;
		try {
			out = new OutputStreamWriter(getContext().openFileOutput(
					LAST_UPDATED, 0));
			out.write(time.toString());
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private long getLastUpdatedDate() {
		try {
			InputStream in = getContext().openFileInput(LAST_UPDATED);
			if (in != null) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));
				String date = reader.readLine();
				in.close();
				return Long.parseLong(date);
			}
		} catch (FileNotFoundException e) {
			writeLastUpdatedDate((long) 0);
			return getLastUpdatedDate();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Creates an XmlPullParser for the provided local resource. Can be
	 * overloaded to provide your own parser.
	 * 
	 * @param resourceUri
	 *            A fully qualified resource name referencing a local XML
	 *            resource.
	 * @return An XmlPullParser on this resource.
	 */


	/**
	 * This ContentProvider is read-only. This method throws an
	 * UnsupportedOperationException.
	 **/
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		throw new UnsupportedOperationException();
	}

	private static class XMLCursor extends MatrixCursor {
		private final Pattern mSelectionPattern;
		private Pattern[] mProjectionPatterns;
		private String[] mAttributeNames;
		private String[] mCurrentValues;
		private BitSet[] mActiveTextDepthMask;
		private final int mNumberOfProjections;

		public XMLCursor(String selection, String[] projections) {
			super(projections);
			// The first column in projections is used for the _ID
			mNumberOfProjections = projections.length - 1;
			mSelectionPattern = createPattern(selection);
			createProjectionPattern(projections);
		}

		private Pattern createPattern(String input) {
			String pattern = input.replaceAll("//", "/(.*/|)").replaceAll("^/",
					"^/")
					+ "$";
			return Pattern.compile(pattern);
		}

		private void createProjectionPattern(String[] projections) {
			mProjectionPatterns = new Pattern[mNumberOfProjections];
			mAttributeNames = new String[mNumberOfProjections];
			mActiveTextDepthMask = new BitSet[mNumberOfProjections];
			// Add a column to store _ID
			mCurrentValues = new String[mNumberOfProjections + 1];

			for (int i = 0; i < mNumberOfProjections; i++) {
				mActiveTextDepthMask[i] = new BitSet();
				String projection = projections[i + 1]; // +1 to skip the _ID
														// column
				int atIndex = projection.lastIndexOf('@', projection.length());
				if (atIndex >= 0) {
					mAttributeNames[i] = projection.substring(atIndex + 1);
					projection = projection.substring(0, atIndex);
				} else {
					mAttributeNames[i] = null;
				}

				// Conforms to XPath standard: reference to local context starts
				// with a .
				if (projection.charAt(0) == '.') {
					projection = projection.substring(1);
				}
				mProjectionPatterns[i] = createPattern(projection);
			}
		}

		public void parseWith(XmlPullParser parser) throws IOException,
				XmlPullParserException {
			StringBuilder path = new StringBuilder();
			Stack<Integer> pathLengthStack = new Stack<Integer>();

			// There are two parsing mode: in root mode, rootPath is updated and
			// nodes matching
			// selectionPattern are searched for and currentNodeDepth is
			// negative.
			// When a node matching selectionPattern is found, currentNodeDepth
			// is set to 0 and
			// updated as children are parsed and projectionPatterns are
			// searched in nodePath.
			int currentNodeDepth = -1;

			// Index where local selected node path starts from in path
			int currentNodePathStartIndex = 0;

			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {

				if (eventType == XmlPullParser.START_TAG) {
					// Update path
					pathLengthStack.push(path.length());
					path.append('/');
					String prefix = null;
					try {
						// getPrefix is not supported by local Xml resource
						// parser
						prefix = parser.getPrefix();
					} catch (RuntimeException e) {
						prefix = null;
					}
					if (prefix != null) {
						path.append(prefix);
						path.append(':');
					}
					path.append(parser.getName());

					if (currentNodeDepth >= 0) {
						currentNodeDepth++;
					} else {
						// A node matching selection is found: initialize child
						// parsing mode
						if (mSelectionPattern.matcher(path.toString())
								.matches()) {
							currentNodeDepth = 0;
							currentNodePathStartIndex = path.length();
							mCurrentValues[0] = Integer.toString(getCount()); // _ID
							for (int i = 0; i < mNumberOfProjections; i++) {
								// Reset values to default (empty string)
								mCurrentValues[i + 1] = "";
								mActiveTextDepthMask[i].clear();
							}
						}
					}

					// This test has to be separated from the previous one as
					// currentNodeDepth can
					// be modified above (when a node matching selection is
					// found).
					if (currentNodeDepth >= 0) {
						final String localNodePath = path
								.substring(currentNodePathStartIndex);
						for (int i = 0; i < mNumberOfProjections; i++) {
							if (mProjectionPatterns[i].matcher(localNodePath)
									.matches()) {
								String attribute = mAttributeNames[i];
								if (attribute != null) {
									mCurrentValues[i + 1] = parser
											.getAttributeValue(null, attribute);
								} else {
									mActiveTextDepthMask[i].set(
											currentNodeDepth, true);
								}
							}
						}
					}

				} else if (eventType == XmlPullParser.END_TAG) {
					// Pop last node from path
					final int length = pathLengthStack.pop();
					path.setLength(length);

					if (currentNodeDepth >= 0) {
						if (currentNodeDepth == 0) {
							// Leaving a selection matching node: add a new row
							// with results
							addRow(mCurrentValues);
						} else {
							for (int i = 0; i < mNumberOfProjections; i++) {
								mActiveTextDepthMask[i].set(currentNodeDepth,
										false);
							}
						}
						currentNodeDepth--;
					}

				} else if ((eventType == XmlPullParser.TEXT)
						&& (!parser.isWhitespace())) {
					for (int i = 0; i < mNumberOfProjections; i++) {
						if ((currentNodeDepth >= 0)
								&& (mActiveTextDepthMask[i]
										.get(currentNodeDepth))) {
							mCurrentValues[i + 1] += parser.getText();
						}
					}
				}

				eventType = parser.next();
			}
		}
	}

	@Override
	public String getType(Uri arg0) {
		return null;
	}
}