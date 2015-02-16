package com.suleymanaybuke.mobilyoklama.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.suleymanaybuke.mobilyoklama.R;
import com.suleymanaybuke.mobilyoklama.object.SerializableListClass;

public class ChooserActivity extends ActionBarActivity {
	private Button btnOcr , btnQr;
	private String sinavID;
	private SerializableListClass listClass;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chooser);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
		sinavID = getIntent().getExtras().getString("sinavID");
		listClass = (SerializableListClass) getIntent().getExtras().getSerializable("data");
		
		btnOcr = (Button) findViewById(R.id.buttonOcr);
		btnQr = (Button) findViewById(R.id.buttonQr);
		
		btnOcr.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ChooserActivity.this, SimpleAndroidOCRActivity.class);
				intent.putExtra("sinavID", sinavID );
				intent.putExtra("data", listClass);
				startActivity(intent);
			}
		});
		
		btnQr.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ChooserActivity.this, BarcodeActivity.class);
				intent.putExtra("sinavID", sinavID );
				intent.putExtra("data", listClass);
				startActivity(intent);

			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chooser, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_chooser, container, false);
			return rootView;
		}
	}

}
