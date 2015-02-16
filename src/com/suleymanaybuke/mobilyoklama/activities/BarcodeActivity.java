package com.suleymanaybuke.mobilyoklama.activities;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import com.suleymanaybuke.mobilyoklama.R;
import com.suleymanaybuke.mobilyoklama.api.IntentKeyString;
import com.suleymanaybuke.mobilyoklama.object.CameraPreview;
import com.suleymanaybuke.mobilyoklama.object.SerializableListClass;

public class BarcodeActivity extends ActionBarActivity {
	
	static {
	    System.loadLibrary("iconv");
	}
	private Camera mCamera;
	private CameraPreview mPreview;
	private Handler autoFocusHandler;
	private String sinavID;
	private ImageScanner scanner;
	private Intent intent;
	private boolean barcodeScanned = false;
	private boolean previewing = true;
	private SerializableListClass listClass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barcode);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		sinavID = getIntent().getExtras().getString(IntentKeyString.SINAV_ID);
		listClass = (SerializableListClass) getIntent().getExtras().getSerializable(IntentKeyString.OGRENCI_LIST);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		autoFocusHandler = new Handler();
		mCamera = getCameraInstance();
		Log.i("info" , "60");
		/* Instance barcode scanner */
		scanner = new ImageScanner();
		Log.i("info" , "63");
		scanner.setConfig(0, Config.X_DENSITY, 3);
		scanner.setConfig(0, Config.Y_DENSITY, 3);

		mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
		FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
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
			Camera.Parameters parameters = camera.getParameters();
			Size size = parameters.getPreviewSize();

			Image barcode = new Image(size.width, size.height, "Y800");
			barcode.setData(data);
			int result = scanner.scanImage(barcode);

			if (result != 0) {

				SymbolSet syms = scanner.getResults();
				for (Symbol sym : syms) {
					setBarcodeScanned(true);
					releaseCamera();
					intent = new Intent(BarcodeActivity.this , ObjectDetail.class);
					intent.putExtra(IntentKeyString.OGRENCI_NO, sym.getData());
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
