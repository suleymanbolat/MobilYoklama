package com.suleymanaybuke.mobilyoklama.activities;

import java.io.File;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.suleymanaybuke.mobilyoklama.R;
import com.suleymanaybuke.mobilyoklama.api.IntentKeyString;
import com.suleymanaybuke.mobilyoklama.object.SerializableListClass;
import com.suleymanaybuke.mobilyoklama.object.Student;

public class ObjectDetail extends ActionBarActivity {
	private String sinavID;
	private String strOgrenciNo;
	private String strTcNo;
	private TextView ogrenciNo;
	private TextView tcNo;
	private TextView adiSoyadi;
	private TextView sinif;
	private TextView fakulte;
	private TextView bolum;
	private TextView aktifDonem;
	private ImageView fotograf;
	private List<Student> students;
	private Button sinaviBitir;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_object_detail);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
		sinavID = getIntent().getExtras().getString(IntentKeyString.SINAV_ID) != null ? getIntent().getExtras().getString(IntentKeyString.OGRENCI_NO) : "";
		strOgrenciNo = getIntent().getExtras().getString(IntentKeyString.OGRENCI_NO);
		students = ((SerializableListClass) getIntent().getExtras().getSerializable(IntentKeyString.OGRENCI_LIST)).getStudentList();
		strTcNo = getIntent().getExtras().getString(IntentKeyString.TC_NO) != null ? getIntent().getExtras().getString(IntentKeyString.TC_NO) : "";

		ogrenciNo = (TextView) findViewById(R.id.ogrenciNo);
		tcNo = (TextView) findViewById(R.id.tcNo);
		adiSoyadi = (TextView) findViewById(R.id.adiSoyadi);
		sinif = (TextView) findViewById(R.id.sinif);
		fakulte = (TextView) findViewById(R.id.fakulte);
		bolum = (TextView) findViewById(R.id.bolum);
		aktifDonem = (TextView) findViewById(R.id.aktifDonem);
		sinaviBitir = (Button) findViewById(R.id.sinaviBitir);
		fotograf = (ImageView) findViewById(R.id.imageView1);
		sinaviBitir.setOnClickListener(new OnClickListener() {
			// TODO öðrenci verilerini silip silmediðini test et.
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(ObjectDetail.this).setTitle("Uyarý").setMessage(R.string.sinaviBitirUyari).setIcon(android.R.drawable.ic_dialog_alert)
						.setPositiveButton(R.string.confirm_box_yes, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								File fotografKlasoru = new File("sdcard/altklasor");
								fotografKlasoru.delete();
//								if (fotografKlasoru.exists()) {
//									String data[] = fotografKlasoru.list();
//									for (int i = 0; i < data.length; i++) {
//										new File(fotografKlasoru, data[i]).delete();
//									}
//								}
								Toast.makeText(ObjectDetail.this, "Sinav Sonlandýrýldý.", Toast.LENGTH_LONG).show();
							}
						}).setNegativeButton(R.string.confirm_box_no, null).show();

			}
		});

		if (!(strOgrenciNo.equals(""))) {
			Student s = findStudentByStudentNumber(strOgrenciNo);
			if (s != null) {
				ogrenciNo.setText(s.getNo());
				tcNo.setText(s.getTc());
				adiSoyadi.setText(s.getAdi() + " " + s.getSoyadi());
				sinif.setText(s.getSinif());
				fakulte.setText(s.getFakulte());
				bolum.setText(s.getFakulte());
				aktifDonem.setText(s.getAktifDonem());
				Log.i("info", "obj 94");
				Bitmap bitmap = BitmapFactory.decodeFile(s.getFotograf());
				fotograf.setImageBitmap(bitmap);

			} else {
				Toast.makeText(ObjectDetail.this, "Ogrenci Bulunamadý", Toast.LENGTH_LONG).show();

			}
		} else if (!(strTcNo.equals(""))) {
			Student s = findStudentByTcNumber(strTcNo);
			if (s != null) {
				ogrenciNo.setText(s.getNo());
				tcNo.setText(s.getTc());
				adiSoyadi.setText(s.getAdi() + " " + s.getSoyadi());
				sinif.setText(s.getSinif());
				fakulte.setText(s.getFakulte());
				bolum.setText(s.getFakulte());
				aktifDonem.setText(s.getAktifDonem());
				Log.i("info", "obj 94");
				Bitmap bitmap = BitmapFactory.decodeFile(s.getFotograf());
				fotograf.setImageBitmap(bitmap);
			}else{
				Toast.makeText(ObjectDetail.this, "Ogrenci Bulunamadý", Toast.LENGTH_LONG).show();

			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.object_detail, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_object_detail, container, false);
			return rootView;
		}
	}

	private Student findStudentByStudentNumber(String ogrenciNo) {
		for (Student s : students) {
			if (s.getNo().trim().equals(ogrenciNo)) {
				return s;
			}
		}
		return null;

	}

	private Student findStudentByTcNumber(String tc) {
		for (Student s : students) {
			if (s.getTc().trim().equals(tc)) {
				return s;
			}
		}
		return null;

	}

}
