package com.suleymanaybuke.mobilyoklama.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.suleymanaybuke.mobilyoklama.R;
import com.suleymanaybuke.mobilyoklama.api.SpString;
import com.suleymanaybuke.mobilyoklama.api.SqlQuery;
import com.suleymanaybuke.mobilyoklama.object.DatabaseConnection;
import com.suleymanaybuke.mobilyoklama.object.SerializableListClass;
import com.suleymanaybuke.mobilyoklama.object.Student;

public class LoadingActivity extends ActionBarActivity {

	private String sinavID;
	private List<Student> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}

		sinavID = getIntent().getExtras().getString("sinavID");
		DbTask dbTask = new DbTask();
		dbTask.execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.loading, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_loading, container, false);
			return rootView;
		}
	}

	private class DbTask extends AsyncTask<Void, Void, Void> {
		private String databaseName;
		private String databaseUrl;
		private String userName;
		private String userPassword;
		private String query;
		
		@Override
		protected Void doInBackground(Void... params) {
			data = new ArrayList<Student>();
			SharedPreferences sharedPreferences = getSharedPreferences(SpString.XML_FILE_NAME, MODE_MULTI_PROCESS);
			databaseName = sharedPreferences.getString(SpString.DATABASE_NAME, "");
			databaseUrl = sharedPreferences.getString(SpString.DATABASE_URL, "");
			userName = sharedPreferences.getString(SpString.USER_NAME, "");
			userPassword = sharedPreferences.getString(SpString.USER_PASSWORD, "");
			DatabaseConnection dbConnection;
			try {
				dbConnection = new DatabaseConnection(databaseUrl, databaseName, userName, userPassword);
				query = SqlQuery.SELECT_ALL_OGRENCI_SINAV.replace("%sinavID%", sinavID);
				ResultSet rs = dbConnection.executeQuery(query);
				while (rs.next()) {
					Student s = new Student();
					s.setAdi(rs.getString("adi"));
					//TODO LOG
					Log.i("info", s.getAdi());
					s.setSoyadi(rs.getString("soyadi"));
					s.setAktifDonem(rs.getString("aktifDonem"));
					s.setBolum(rs.getString("bolumAdi"));
					s.setFakulte(rs.getString("fakulteAdi"));
					s.setNo(rs.getString("no"));
					s.setSinif(rs.getString("sinif"));
					s.setTc(rs.getString("tc"));
					Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(rs.getString("fotograf")).getContent());
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
					byte[] byteArray = stream.toByteArray();
					fotografKaydet(byteArray, sinavID, s.getNo());
					s.setFotograf("sdcard/altklasor"+"/"+sinavID.trim()+"/"+s.getNo().trim()+".jpg");
					Log.i("info", s.getFotograf());
					data.add(s);
					Log.i("info", "data.add");
				}

			} catch ( Exception e) {
				Log.i("info" , e.toString()+" 134");
				Intent intent = new Intent(LoadingActivity.this, SettingsActivity.class);
				LoadingActivity.this.startActivity(intent);
			}

			return null;
		}
		protected void onPostExecute(Void result) {
			try{
			Log.i("info", "onpostexe");
			Intent intent = new Intent(LoadingActivity.this, ChooserActivity.class);
			intent.putExtra("sinavID", sinavID);
			SerializableListClass ser = new SerializableListClass();
			ser.setStudentList(data);
			intent.putExtra("data",ser );
			LoadingActivity.this.startActivity(intent);
			}catch(Exception e){
				Log.i("info" , e.toString()+" 153");
			}
		}

	}

	private void fotografKaydet(byte[] data, String fileName, String photoName) {

		File fotografKlasoru = new File("sdcard/altklasor", fileName.trim() ); 
		Log.i("info" , "klasör deneme");
		if (!fotografKlasoru.exists()) 
		{
			fotografKlasoru.mkdir();
			Log.i("info", "klasör oluþturduk");
		}

		File fotograf = new File(fotografKlasoru.getAbsoluteFile(), photoName.trim() + ".jpg");
		try {
			FileOutputStream fos = new FileOutputStream(fotograf); 
			fos.write(data);
			Log.i("info", "fotograf kaydedildi.");
			Log.i("info" , photoName);
			fos.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
