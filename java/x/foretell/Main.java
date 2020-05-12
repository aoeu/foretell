package x.foretell;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import java.lang.Exception;
import java.lang.Void;

public class Main extends Activity {

  final String s =
    "https://forecast.weather.gov/meteograms/Plotter.php?lat=40.7011&lon=-73.9173&wfo=OKX&zcode=NYZ075&gset=20&gdiff=10&unit=0&tinfo=EY5&ahour=0&pcmd=11011111100000000000000000000000000000000000000000000000000&lg=en&indu=1!1!1!&dd=&bw=&hrspan=12&pqpfhr=6&psnwhr=6";

  final Handler h = new Handler();
  ImageView image;

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

  void loadImage() {
    new AsyncTask<Void, Void, Bitmap>() {
      @Override
      protected Bitmap doInBackground(Void... v) {
        return downloadImage(s);
      }

      @Override
      protected void onPostExecute(Bitmap b) {
        if (b == null) {
          startActivity(
              new android.content.Intent(android.content.Intent.ACTION_VIEW)
                  .setData(android.net.Uri.parse(s))
                  .setPackage("com.android.chrome")
              );
        } else {
          ((ImageView) findViewById(R.id.image)).setImageBitmap(b);
          long delayInMilliseconds = 1000 * 60 * 60;
          fetchNextImage(delayInMilliseconds);
        }
      }
    }.execute();
  }

  Bitmap downloadImage(String url) {
    Bitmap b = null;
    java.net.HttpURLConnection c = null;
    int respCode = 0;
    try {
      c = (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
      b = BitmapFactory.decodeStream(new java.io.BufferedInputStream(c.getInputStream()));
      respCode = c.getResponseCode();
    } catch (Exception e) {
      Log.e("Error", e.getMessage());
      e.printStackTrace();
    } finally {
      if (c != null) {
        c.disconnect();
        if (respCode != 200 && respCode != 0) {
          android.util.Log.w(
              Main.this.getClass().getSimpleName(),
              String.format("Received HTTP status code %v when downloading image", respCode)
          );
        }
      }
    }
    return b;
  }

  void fetchNextImage(long L) {
    h.removeCallbacksAndMessages(null);
    h.postDelayed(new Runnable() { public void run() { loadImage(); } }, L);
  }

}
