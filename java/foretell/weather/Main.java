package foretell.weather;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrixColorFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import java.lang.Exception;
import java.lang.Void;

public class Main extends Activity {

  final String s =
      "https://forecast.weather.gov/meteograms/Plotter.php?ahour=0&gdiff=10&gset=20&hrspan=24&lat=40.704&lg=en&lon=-73.946&pcmd=11011111111110111&tinfo=EY5&unit=0&wfo=OKX&zcode=NYZ075"; // TODO(aoeu): obtain string from gomobile package.

  ImageView image;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
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
          loadImageInBrowser();
        } else {
          ((ImageView) findViewById(R.id.image)).setImageBitmap(b);
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
        "error: " +  e.getMessage()
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

}
