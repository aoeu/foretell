package aoeu.foretell;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;

public class MainActivity extends Activity {

  final Uri weatherUri = Uri.parse("http://forecast.weather.gov/meteograms/Plotter.php?lat=40.6447&lon=-73.9472&wfo=OKX&zcode=NYZ075&gset=20&gdiff=10&unit=0&tinfo=EY5&ahour=0&pcmd=11010111111110100000000000000000000000000000000000000000000&lg=en&indu=1!1!1!&dd=&bw=&hrspan=24&pqpfhr=6&psnwhr=6");
  WebView view;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    view = setWebSettings(new WebView(this));
    setContentView(view);
  }

  WebView  setWebSettings(WebView view) {
    view.getSettings().setUseWideViewPort(true);
    view.getSettings().setLoadWithOverviewMode(true);
    return view;
  }

  @Override
  protected void onResume() {
    super.onResume();
    view.loadUrl(weatherUri.toString());
  }
}
