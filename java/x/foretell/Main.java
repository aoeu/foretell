package x.foretell;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
    new AsyncTask<Void, Void, Bitmap>() {
      @Override
      protected Bitmap doInBackground(Void... v) {
        Bitmap b = null;
        java.net.HttpURLConnection c = null;
        int respCode = 0;
        try {
          c = (java.net.HttpURLConnection) new java.net.URL(s).openConnection();
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
        }
      }
    }.execute();
  }
}
