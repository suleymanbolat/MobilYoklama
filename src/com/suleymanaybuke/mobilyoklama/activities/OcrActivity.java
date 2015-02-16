package com.suleymanaybuke.mobilyoklama.activities;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.suleymanaybuke.mobilyoklama.R;
import com.suleymanaybuke.mobilyoklama.api.IntentKeyString;
import com.suleymanaybuke.mobilyoklama.object.CameraPreview;
import com.suleymanaybuke.mobilyoklama.object.SerializableListClass;

public class OcrActivity extends ActionBarActivity {

	static {
		System.loadLibrary("iconv");
	}
	private Camera mCamera;
	private CameraPreview mPreview;
	private Handler autoFocusHandler;
	private String sinavID;
	private SerializableListClass listClass;
	private Intent intent;
	private boolean barcodeScanned = false;
	private boolean previewing = true;
	Bitmap bitmap;
	String recognizedText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ocr);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}

		sinavID = getIntent().getExtras().getString(IntentKeyString.SINAV_ID);
		listClass = (SerializableListClass) getIntent().getExtras().getSerializable(IntentKeyString.OGRENCI_LIST);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		autoFocusHandler = new Handler();
		mCamera = getCameraInstance();
		Log.i("info", "60");

		mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
		FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview1);
		preview.addView(mPreview);

	}

	public void onPause() {
		super.onPause();
		releaseCamera();
	}

	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
		} catch (Exception e) {
		}
		return c;
	}

	private void releaseCamera() {
		if (mCamera != null) {
			previewing = false;
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}

	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (previewing)
				mCamera.autoFocus(autoFocusCB);
		}
	};

	PreviewCallback previewCb = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			Size previewSize = camera.getParameters().getPreviewSize(); 
			YuvImage yuvimage=new YuvImage(data, ImageFormat.NV21, previewSize.width, previewSize.height, null);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 80, baos);
			byte[] jdata = baos.toByteArray();
			bitmap = BitmapFactory.decodeByteArray(jdata, 0, jdata.length);
//			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			if(bitmap != null){
				Log.i("info", "117");
				Matrix mtx = new Matrix();
				mtx.preRotate(180);
				Log.i("info", "prewi");
				TessBaseAPI baseApi = new TessBaseAPI();
				Log.i("info" , "base api olustu");
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mtx, false);
				bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
				baseApi.init("sdcard/altklasor/tur.traineddata", "tur");
				Log.i("info" , "init base");
				baseApi.setImage(bitmap);
				recognizedText = baseApi.getUTF8Text();
				Log.i("info", " text"+ recognizedText);

				baseApi.end();
				Pattern p = Pattern.compile("[1-9]{1}[0-9]{10}");
				Pattern p2 = Pattern.compile("[1-9]{1}[0-9]{7}");
				Matcher m = p.matcher(recognizedText);
				while (m.find()) {
					intent.putExtra(IntentKeyString.TC_NO, m.group());
					releaseCamera();
					intent = new Intent(OcrActivity.this, ObjectDetail.class);
					intent.putExtra(IntentKeyString.SINAV_ID, sinavID);
					intent.putExtra(IntentKeyString.OGRENCI_LIST, listClass);
					setResult(RESULT_OK, intent);
					
					startActivity(intent);
					
				}
				m = p2.matcher(recognizedText);
				while (m.find()) {
					intent.putExtra(IntentKeyString.OGRENCI_NO, m.group());
					releaseCamera();
					intent = new Intent(OcrActivity.this, ObjectDetail.class);
					intent.putExtra(IntentKeyString.SINAV_ID, sinavID);
					intent.putExtra(IntentKeyString.OGRENCI_LIST, listClass);
					setResult(RESULT_OK, intent);
					startActivity(intent);
				}
			
			}
		}
	};

	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.barcode, menu);
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

	public boolean isBarcodeScanned() {
		return barcodeScanned;
	}

	public void setBarcodeScanned(boolean barcodeScanned) {
		this.barcodeScanned = barcodeScanned;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_barcode, container, false);
			return rootView;
		}
	}
	

}
