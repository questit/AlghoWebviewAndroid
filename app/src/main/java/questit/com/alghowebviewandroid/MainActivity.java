package questit.com.alghowebviewandroid;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    public WebView myWebView;
    public String currentModeUrl;

    public final int REQUEST_MICROPHONE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentModeUrl = Commons.URL_FE_N_TEXT;

        // Check permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_MICROPHONE);
        }
        else {
            startWebView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_MICROPHONE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    startWebView();
                }  else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }

    public void startWebView() {

        // WebView settings
        myWebView = (WebView) findViewById(R.id.webview);

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);

        myWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        request.grant(request.getResources());
                    }
                });
            }
        });

        myWebView.loadUrl(currentModeUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_text) {

            if(currentModeUrl != Commons.URL_FE_N_TEXT) {
                currentModeUrl = Commons.URL_FE_N_TEXT;
                myWebView.loadUrl(currentModeUrl);
            }
            else {
                Toast toast = Toast.makeText(getApplicationContext(), "You are already viewing Algho text", Toast.LENGTH_LONG);
                toast.show();
            }

            return true;
        }
        else if (id == R.id.action_voice) {

            if(currentModeUrl != Commons.URL_FE_N_VOICE) {
                currentModeUrl = Commons.URL_FE_N_VOICE;
                myWebView.loadUrl(currentModeUrl);
            }
            else {
                Toast toast = Toast.makeText(getApplicationContext(), "You are already viewing Algho voice", Toast.LENGTH_LONG);
                toast.show();
            }

            return true;
        }
        else if (id == R.id.action_dhi) {

            if(currentModeUrl != Commons.URL_FE_N_DHI) {
                currentModeUrl = Commons.URL_FE_N_DHI;
                myWebView.loadUrl(currentModeUrl);
            }
            else {
                Toast toast = Toast.makeText(getApplicationContext(), "You're already viewing Algho DHI", Toast.LENGTH_LONG);
                toast.show();
            }

            return true;
        }
        else if (id == R.id.action_info) {

            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle(getResources().getString(R.string.dialog_title))
                    .setMessage(getResources().getString(R.string.dialog_message))
                    .setPositiveButton("Visit website", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String url = "http://www.quest-it.com";
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        }
                    })
                    .create();
            dialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
