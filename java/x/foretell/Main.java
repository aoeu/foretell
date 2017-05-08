package x.foretell;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import go.noaa.Noaa;

public class Main extends Activity {

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
    view.loadUrl(Noaa.nycMeteogramURL());
  }
}
