package com.example.translationapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

import static android.widget.Toast.makeText;

public class MainActivity extends Activity implements RecognitionListener {

    private TextView txvResult;



    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    private SpeechRecognizer recognizer;

    private static final String KWS_SEARCH = "wakeup";
    private static final String MENU_SEARCH = "menu";
    private static final String FORECAST_SEARCH = "forecast";

    private static final String KEYPHRASE = "translate";

    private static String tel_number = "05394961807";

    private HashMap<String, Integer> captions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txvResult = (TextView) findViewById(R.id.txvResult);

        captions = new HashMap<>();
        captions.put(KWS_SEARCH, R.string.kws_caption);
        captions.put(MENU_SEARCH, R.string.menu_caption);
        captions.put(FORECAST_SEARCH, R.string.forecast_caption);
        ((TextView) findViewById(R.id.txvResult))
                .setText("Preparing the recognizer");


        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
            return;
        }


        new SetupTask(this).execute();
    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {
        if (!recognizer.getSearchName().equals(KWS_SEARCH))
            switchSearch(KWS_SEARCH);
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;

        String text = hypothesis.getHypstr();
        if (text.equals(KEYPHRASE))
            switchSearch(FORECAST_SEARCH);
        else if (text.equals(FORECAST_SEARCH)) {
            switchSearch(FORECAST_SEARCH);
        }
        else
            ((TextView) findViewById(R.id.result_text)).setText(text);
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        ((TextView) findViewById(R.id.result_text)).setText("");
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            if(text.equals("number")) {
                makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            } else if (text.equals("north")) {
                makeText(getApplicationContext(), "kuzey", Toast.LENGTH_SHORT).show();
            } else if (text.equals("east")) {
                makeText(getApplicationContext(), "doğu", Toast.LENGTH_SHORT).show();
            } else if (text.equals("west")) {
                makeText(getApplicationContext(), "batı", Toast.LENGTH_SHORT).show();
            } else if (text.equals("south")) {
                makeText(getApplicationContext(), "güney", Toast.LENGTH_SHORT).show();
            } else if (text.equals("place")) {
                makeText(getApplicationContext(), "mekan", Toast.LENGTH_SHORT).show();
            } else if (text.equals("church")) {
                makeText(getApplicationContext(), "kilise", Toast.LENGTH_SHORT).show();
            } else if (text.equals("mosque")) {
                makeText(getApplicationContext(), "cami", Toast.LENGTH_SHORT).show();
            } else if (text.equals("file")) {
                makeText(getApplicationContext(), "dosya", Toast.LENGTH_SHORT).show();
            } else if (text.equals("bus")) {
                makeText(getApplicationContext(), "otobüs", Toast.LENGTH_SHORT).show();
            } else if (text.equals("hello")) {
                makeText(getApplicationContext(), "merhaba", Toast.LENGTH_SHORT).show();
            } else if (text.equals("language")) {
                makeText(getApplicationContext(), "dil", Toast.LENGTH_SHORT).show();
            }else {
                makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onError(Exception e) {
        ((TextView) findViewById(R.id.txvResult)).setText(e.getMessage());
    }

    @Override
    public void onTimeout() {
        switchSearch(KWS_SEARCH);
    }

    private static class SetupTask extends AsyncTask<Void, Void, Exception> {
        WeakReference<MainActivity> activityReference;
        SetupTask(MainActivity activity) {
            this.activityReference = new WeakReference<>(activity);
        }
        @Override
        protected Exception doInBackground(Void... params) {
            try {
                Assets assets = new Assets(activityReference.get());
                File assetDir = assets.syncAssets();
                activityReference.get().setupRecognizer(assetDir);
            } catch (IOException e) {
                return e;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Exception result) {
            if (result != null) {
                ((TextView) activityReference.get().findViewById(R.id.txvResult))
                        .setText("Failed to init recognizer " + result);
            } else {
                activityReference.get().switchSearch(KWS_SEARCH);
            }
        }
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them

        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
                .getRecognizer();
        recognizer.addListener(this);

        /* In your application you might not need to add all those searches.
          They are added here for demonstration. You can leave just one.
         */

        // Create keyword-activation search.
        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);

        //File languageModel = new File(assetsDir, "weather.dmp");
        //recognizer.addNgramSearch(FORECAST_SEARCH, languageModel);


        // Create grammar-based search for selection between demos
        File menuGrammar = new File(assetsDir, "menu.gram");
        recognizer.addGrammarSearch(FORECAST_SEARCH, menuGrammar);

        //en-70k-0.1-pruned.lm
        //File languageModel = new File(assetsDir, "5688.lm");
        //recognizer.addNgramSearch(FORECAST_SEARCH, languageModel);



    }

    private void switchSearch(String searchName) {
        recognizer.stop();

        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        if (searchName.equals(KWS_SEARCH))
            recognizer.startListening(searchName);
        else
            recognizer.startListening(searchName, 10000);

        String caption = getResources().getString(captions.get(searchName));
        ((TextView) findViewById(R.id.txvResult)).setText(caption);
    }

    public void onCall() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    Integer.parseInt("123"));
        } else {
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + tel_number)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 123:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    onCall();
                } else {
                    Log.d("TAG", "Call Permission Not Granted");
                }
                break;

            default:
                break;
        }
    }
}
