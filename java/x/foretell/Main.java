package x.foretell;

import android.app.Activity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

import java.lang.Void;
import java.lang.Exception;

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
	@Override protected Bitmap doInBackground(Void... v) { 
		Bitmap b = null;
        try {
            b = BitmapFactory.decodeStream(new java.net.URL("http://forecast.weather.gov/meteograms/Plotter.php?ahour=0&gdiff=10&gset=20&hrspan=24&lat=40.704&lg=en&lon=-73.946&pcmd=11011111111110111&tinfo=EY5&unit=0&wfo=OKX&zcode=NYZ075"
).openStream());
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return b;
	}

	@Override
 	protected void onPostExecute(Bitmap b) {
        		((ImageView)findViewById(R.id.image)).setImageBitmap(b);
    	}		

    }.execute();
  }
}
