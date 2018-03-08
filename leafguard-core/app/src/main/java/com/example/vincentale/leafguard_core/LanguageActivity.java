package com.example.vincentale.leafguard_core;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {
    private static final String FILE_NAME = "file_lang"; // preference file name
    private static final String KEY_LANG = "key_lang"; // preference key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguage();
        setContentView(R.layout.activity_language);
        Button englishButton = (Button) findViewById(R.id.englishButton);
        englishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveLanguage("en");
            }
        });
        Button frenchButton = (Button) findViewById(R.id.frenchButton);
        frenchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveLanguage("fr");
            }
        });
    }


    private void saveLanguage(String lang) {
        SharedPreferences preferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_LANG, lang);
        editor.apply();
        recreate();
        Toast.makeText(LanguageActivity.this, getText(R.string.successLanguage), Toast.LENGTH_SHORT).show();

    }

    private void loadLanguage() {
        Locale locale = new Locale(getLangCode());
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private String getLangCode() {
        SharedPreferences preferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        String langCode = preferences.getString(KEY_LANG, "en");
        // save english 'en' as the default language
        return langCode;
    }

}
