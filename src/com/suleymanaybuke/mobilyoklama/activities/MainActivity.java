package com.suleymanaybuke.mobilyoklama.activities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.suleymanaybuke.mobilyoklama.R;
import com.suleymanaybuke.mobilyoklama.api.IntentKeyString;
import com.suleymanaybuke.mobilyoklama.api.SpString;
import com.suleymanaybuke.mobilyoklama.api.SqlQuery;
import com.suleymanaybuke.mobilyoklama.object.DatabaseConnection;

public class MainActivity extends ActionBarActivity {

	ListView listemiz;
	List<String> data = new ArrayList<>();

	// TODO
//	@Override
//	protected void onResume() {
//		super.onResume();
//		data.clear();
//		new DbTask().execute();
//	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
		setContentView(R.layout.activity_main);

		listemiz = (ListView) findViewById(R.id.listView1);
		new DbTask().execute();

		listemiz.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Intent intent = new Intent(MainActivity.this, LoadingActivity.class);
				String selection = data.get(position);
				String sinavID = selection.split("-")[0];
				intent.putExtra(IntentKeyString.SINAV_ID, sinavID);
				startActivity(intent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			return rootView;
		}
	}

	private class DbTask extends AsyncTask<Void, Void, Void> {
		private String databaseName;
		private String databaseUrl;
		private String userName;
		private String userPassword;
		private String sinavId;
		private String dersAdi;
		private String turu;
		private String sinavTarihi;
		private String sinifi;

		@Override
		protected Void doInBackground(Void... params) {
			//TODO LOG
			Log.d("info", "giris");
			SharedPreferences sharedPreferences = getSharedPreferences(SpString.XML_FILE_NAME, MODE_MULTI_PROCESS);
			// SharedPreferences.Editor editor = sharedPreferences.edit();
			// editor.putString(SpString.DATABASE_NAME, "");
			// editor.putString(SpString.DATABASE_URL, "");
			// editor.putString(SpString.USER_NAME, "");
			// editor.putString(SpString.USER_PASSWORD, "");
			// editor.commit();
			databaseName = sharedPreferences.getString(SpString.DATABASE_NAME, "");
			databaseUrl = sharedPreferences.getString(SpString.DATABASE_URL, "");
			userName = sharedPreferences.getString(SpString.USER_NAME, "");
			userPassword = sharedPreferences.getString(SpString.USER_PASSWORD, "");
			DatabaseConnection dbConnection;
			try {
				dbConnection = new DatabaseConnection(databaseUrl, databaseName, userName, userPassword);
				ResultSet rs = dbConnection.executeQuery(SqlQuery.SELECT_ALL_EXAM);
				while (rs.next()) {
					sinavId = rs.getString("id");
					dersAdi = rs.getString("DersAdi");
					turu = rs.getString("Turu");
					sinavTarihi = rs.getString("Tarihi");
					sinifi = rs.getString("Sinifi");

					data.add(sinavId + " - " + dersAdi + " - " + turu + " - " + sinavTarihi + " - " + sinifi);
				}

			} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
				MainActivity.this.startActivity(intent);
			}

			return null;
		}

		protected void onPostExecute(Void result) {
			ArrayAdapter<String> araAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, data);
			listemiz.setAdapter(araAdapter);
		}

	}

}
