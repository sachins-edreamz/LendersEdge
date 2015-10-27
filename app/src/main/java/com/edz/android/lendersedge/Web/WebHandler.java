package com.edz.android.lendersedge.Web;

import java.io.IOException;

import android.net.Uri;
import android.util.Log;

public class WebHandler {

	Web mWeb;

	public WebHandler() {
		// TODO Auto-generated constructor stub
		mWeb = new Web();
	}

	public String getRateIndex(String strTerm, String strAmort, String strRes)
			throws IOException {
		// TODO Auto-generated method stub

		String url = Uri
				.parse("http://lendersedge.net/ledgeadmin123/lendersedge/rates.php/?")
				.buildUpon().appendQueryParameter("Term", strTerm)
				.appendQueryParameter("Amort", strAmort)
				.appendQueryParameter("res", strRes).build().toString();

		Log.i("Url Response", url);
		return mWeb.downloadUrl(url);

	}
	public String getSecondCalsi(String strTerm, String strAmort, String strRes)
			throws IOException {
		// TODO Auto-generated method stub

		String url = Uri
				.parse("http://lendersedge.net/ledgeadmin123/lendersedge/get_dv01.php/?")
				.buildUpon().appendQueryParameter("Term", strTerm)
				.appendQueryParameter("Amort", strAmort)
				.appendQueryParameter("res", strRes).build().toString();

		Log.i("Url Response", url);
		return mWeb.downloadUrl(url);

	}
	
}
