package com.suleymanaybuke.mobilyoklama.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.suleymanaybuke.mobilyoklama.R;
import com.suleymanaybuke.mobilyoklama.api.SpString;

public class SettingsActivity extends ActionBarActivity {

	static String databaseName;
	static String databaseUrl;
	static String userName;
	static String userPassword;
	static EditText editTextDatabaseName;
	static EditText editTextDatabaseUrl;
	static EditText editTextUserName;
	static EditText editTextUserPassword;
	static Button btnSave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
		editTextDatabaseName = (EditText) findViewById(R.id.edittext_database_name);
		editTextDatabaseUrl = (EditText) findViewById(R.id.edittext_database_url);
		editTextUserName = (EditText) findViewById(R.id.edittext_user_name);
		editTextUserPassword = (EditText) findViewById(R.id.edittext_user_password);
		btnSave = (Button) findViewById(R.id.btn_save);
		SharedPreferences sharedPreferences = getSharedPreferences(SpString.XML_FILE_NAME, MODE_MULTI_PROCESS);
		final SharedPreferences.Editor editor = sharedPreferences.edit();
		databaseName = sharedPreferences.getString(SpString.DATABASE_NAME, "Not Set");
		databaseUrl = sharedPreferences.getString(SpString.DATABASE_URL, "Not Set");
		userName = sharedPreferences.getString(SpString.USER_NAME, "Not Set");
		userPassword = sharedPreferences.getString(SpString.USER_PASSWORD, "Not Set");

		editTextDatabaseName.setText(databaseName, BufferType.EDITABLE);
		editTextDatabaseUrl.setText(databaseUrl, BufferType.EDITABLE);
		editTextUserName.setText(userName, BufferType.EDITABLE);
		editTextUserPassword.setText(userPassword, BufferType.EDITABLE);

		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new AlertDialog.Builder(SettingsActivity.this).setTitle("Uyarý").setMessage(R.string.confirm_box_alert).setIcon(android.R.drawable.ic_dialog_alert)
						.setPositiveButton(R.string.confirm_box_yes, new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int whichButton) {
								editor.putString(SpString.DATABASE_NAME, editTextDatabaseName.getText().toString());
								editor.putString(SpString.DATABASE_URL, editTextDatabaseUrl.getText().toString());
								editor.putString(SpString.USER_NAME, editTextUserName.getText().toString());
								editor.putString(SpString.USER_PASSWORD, editTextUserPassword.getText().toString());
								editor.commit();
								Toast.makeText(SettingsActivity.this, "Ayarlar Kaydedildi.", Toast.LENGTH_SHORT).show();
								SettingsActivity.this.finish();
							}
						}).setNegativeButton(R.string.confirm_box_no, null).show();
			}
		});

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
			return rootView;
		}
	}

}
