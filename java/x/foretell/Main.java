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
        try {
          // TODO(aoeu): javac can be goaded into compiling by
          // • extracting the noaa.aar (produced by gomobile bind)
          // • pointing to the classes.jar
          // • changing the import here to `import noaa.Noaa` (*instead* of `import go.noaa.Noaa`)
          // Is that the right way to get javac to compile with the noaa.aar library?
          //  How is the noaa.aar then correctly packaged and referenced?
          b =
              BitmapFactory.decodeStream(
                  new java.net.URL(
                          "http://forecast.weather.gov/meteograms/Plotter.php?ahour=0&gdiff=10&gset=20&hrspan=24&lat=40.704&lg=en&lon=-73.946&pcmd=11011111111110111&tinfo=EY5&unit=0&wfo=OKX&zcode=NYZ075")
                      .openStream()); // TODO(aoeu): obtain string from gomobile package.
        } catch (Exception e) {
          Log.e("Error", e.getMessage());
          e.printStackTrace();
        }
        return b;
      }

      @Override
      protected void onPostExecute(Bitmap b) {
        ((ImageView) findViewById(R.id.image)).setImageBitmap(b);
      }
    }.execute();
  }
}
