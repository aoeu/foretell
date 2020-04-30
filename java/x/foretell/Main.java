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
    "https://forecast.weather.gov/meteograms/Plotter.php?lat=40.7011&lon=-73.9173&wfo=OKX&zcode=NYZ075&gset=20&gdiff=10&unit=0&tinfo=EY5&ahour=0&pcmd=11011111100000000000000000000000000000000000000000000000000&lg=en&indu=1!1!1!&dd=&bw=&hrspan=12&pqpfhr=6&psnwhr=6";

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
          // TODO(aoeu): javac can be goaded into compiling by
          // • extracting the noaa.aar (produced by gomobile bind)
          // • pointing to the classes.jar
          // • changing the import here to `import noaa.Noaa` (*instead* of `import go.noaa.Noaa`)
          // Is that the right way to get javac to compile with the noaa.aar library?
          // How is the noaa.aar then correctly packaged and referenced?
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
                  String.format("Received HTTP status code %v when downloading image", respCode));
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
                  .setPackage("com.android.chrome"));
        } else {
          ((ImageView) findViewById(R.id.image)).setImageBitmap(b);
        }
      }
    }.execute();
  }
}
