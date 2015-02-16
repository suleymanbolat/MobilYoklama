package com.suleymanaybuke.mobilyoklama.activities;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.suleymanaybuke.mobilyoklama.R;
import com.suleymanaybuke.mobilyoklama.api.IntentKeyString;
import com.suleymanaybuke.mobilyoklama.object.SerializableListClass;


public class SimpleAndroidOCRActivity extends Activity {
	public static final String DATA_PATH = "sdcard/altklasor/";
	private String sinavID;
	private SerializableListClass listClass;
	private Intent intent;

	public static final String lang = "tur";

	protected Button _button;
	protected String _path;
	protected boolean _taken;

	protected static final String PHOTO_TAKEN = "photo_taken";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };
		sinavID = getIntent().getExtras().getString(IntentKeyString.SINAV_ID);
		listClass = (SerializableListClass) getIntent().getExtras().getSerializable(IntentKeyString.OGRENCI_LIST);
		for (String path : paths) {
			File dir = new File(path);
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					return;
				} else {
				}
			}

		}
		
		

		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		// _image = (ImageView) findViewById(R.id.image);
		_button = (Button) findViewById(R.id.button);
		_button.setOnClickListener(new ButtonClickHandler());

		_path = DATA_PATH + "/ocr.jpg";
	}

	public class ButtonClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			startCameraActivity();
		}
	}

	// Simple android photo capture:
	// http://labs.makemachine.net/2010/03/simple-android-photo-capture/

	protected void startCameraActivity() {
		File file = new File(_path);
		Uri outputFileUri = Uri.fromFile(file);

		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {


		if (resultCode == -1) {
			onPhotoTaken();
		} else {
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(SimpleAndroidOCRActivity.PHOTO_TAKEN, _taken);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState.getBoolean(SimpleAndroidOCRActivity.PHOTO_TAKEN)) {
			onPhotoTaken();
		}
	}

	protected void onPhotoTaken() {
		_taken = true;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;

		Bitmap bitmap = BitmapFactory.decodeFile(_path, options);

		try {
			ExifInterface exif = new ExifInterface(_path);
			int exifOrientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);


			int rotate = 0;

			switch (exifOrientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			}


			if (rotate != 0) {

				// Getting width & height of the given image.
				int w = bitmap.getWidth();
				int h = bitmap.getHeight();

				// Setting pre rotate
				Matrix mtx = new Matrix();
				mtx.preRotate(rotate);

				// Rotating Bitmap
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
			}

			// Convert to ARGB_8888, required by tess
			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

		} catch (IOException e) {
		}

		// _image.setImageBitmap( bitmap );
		

		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.setDebug(true);
		baseApi.init(DATA_PATH, lang);
		baseApi.setImage(bitmap);
		
		String recognizedText = baseApi.getUTF8Text();
		Pattern p = Pattern.compile("[1-9]{1}[0-9]{10}");
		Pattern p2 = Pattern.compile("[1-9]{1}[0-9]{7}");
		Matcher m = p.matcher(recognizedText);
		while (m.find()) {
			intent = new Intent(SimpleAndroidOCRActivity.this, ObjectDetail.class);
			intent.putExtra(IntentKeyString.TC_NO, m.group());
			intent.putExtra(IntentKeyString.SINAV_ID, sinavID);
			intent.putExtra(IntentKeyString.OGRENCI_LIST, listClass);
			setResult(RESULT_OK, intent);
			
			startActivity(intent);
			
		}
		m = p2.matcher(recognizedText);
		while (m.find()) {
			intent = new Intent(SimpleAndroidOCRActivity.this, ObjectDetail.class);
			intent.putExtra(IntentKeyString.OGRENCI_NO, m.group());
			intent.putExtra(IntentKeyString.SINAV_ID, sinavID);
			intent.putExtra(IntentKeyString.OGRENCI_LIST, listClass);
			setResult(RESULT_OK, intent);
			startActivity(intent);
		}
		baseApi.end();


	}
	
	// www.Gaut.am was here
	// Thanks for reading!
}
