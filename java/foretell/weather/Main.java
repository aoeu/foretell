package foretell.weather;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrixColorFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import java.lang.Exception;
import java.lang.Void;

public class Main extends Activity {

	final String s =
		"https://forecast.weather.gov/meteograms/Plotter.php?lat=40.7011&lon=-73.9173&wfo=OKX&zcode=NYZ075&gset=20&gdiff=10&unit=0&tinfo=EY5&ahour=0&pcmd=11011111100000000000000000000000000000000000000000000000000&lg=en&indu=1!1!1!&dd=&bw=&hrspan=12&pqpfhr=6&psnwhr=6";

	final Handler h = new Handler();
	ImageView image;
	boolean shouldInvertImage = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		hideNavBar();
	}

	void hideNavBar() {
		getWindow().getDecorView().setSystemUiVisibility(
			android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
		);
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadImage();
	}

	@Override
	protected void onPause() {
		super.onPause();
		forceNavBarToHideOnResume();
	}

	void forceNavBarToHideOnResume() {
		finish();
	}

	void loadImage() {
		new AsyncTask<Void, Void, Bitmap>() {
			@Override
			protected Bitmap doInBackground(Void... v) {
				return downloadImage(s);
			}

			@Override
			protected void onPostExecute(Bitmap b) {
				if (b == null) {
					loadImageInBrowser();
				} else {
					ImageView i = ((ImageView) findViewById(R.id.image));
					i.setImageBitmap(b);
					setImageColors(i);
					long delayInMilliseconds = 1000 * 60 * 60;
					fetchNextImage(delayInMilliseconds);
				}
			}
		}.execute();
	}

	void loadImageInBrowser() {
		startActivity(
			new android.content.Intent(android.content.Intent.ACTION_VIEW)
				.setData(android.net.Uri.parse(s))
				.setPackage("com.android.chrome")
		);
	}

	Bitmap downloadImage(String url) {
		Bitmap b = null;
		java.net.HttpURLConnection c = null;
		int respCode = 0;
		try {
			c = (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
			c.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (X11; Linux x86_64; rv:81.0) Gecko/20100101 Firefox/81.0"
			);
			respCode = c.getResponseCode();
			b = BitmapFactory.decodeStream(new java.io.BufferedInputStream(c.getInputStream()));
		} catch (Exception e) {
			android.util.Log.e(
				Main.this.getClass().getSimpleName(),
				"error: " +	e.getMessage()
			);
			e.printStackTrace();
		} finally {
			if (respCode != 200 && respCode != 0) {
				android.util.Log.e(
					Main.this.getClass().getSimpleName(),
					String.format("Received HTTP status code %d when downloading image", respCode)
				);
			}
			if (c != null) {
				c.disconnect();
			}
		}
		return b;
	}

	void setImageColors(ImageView i) {
		if (shouldInvertImage) {
			invertImage(i);
		} else {
			resetColorFilter(i);
		}
		shouldInvertImage = !shouldInvertImage;
	}

	void resetColorFilter(ImageView i) {
		float[] normalColors = {
			1.0f,	   0,	   0,	   0,	0,	// red
			   0,	1.0f,	   0,	   0,	0,	// green
			   0,	   0,	1.0f,	   0,	0,	// blue
			   0,	   0,	   0,	1.0f,	0  	// alpha
		};
		i.setColorFilter(new ColorMatrixColorFilter(normalColors));
	}

	void invertImage(ImageView i) {
		float[] invertedColors = {
			-1.0f,	    0,	    0,	   0,	255,	// red
			    0,	-1.0f,	    0,	   0,	255,	// green
			    0,	    0,	-1.0f,	   0,	255,	// blue
			    0,	    0,	    0,	1.0f,	  0 	// alpha
		};
		i.setColorFilter(new ColorMatrixColorFilter(invertedColors));
	}

	void fetchNextImage(long L) {
		h.removeCallbacksAndMessages(null);
		h.postDelayed(new Runnable() { public void run() { loadImage(); } }, L);
	}

}
