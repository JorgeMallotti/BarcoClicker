package com.example.pildoraclicker;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public final class LocaleManager {

    private LocaleManager() {
    }

    public static void applyLocale(Context context) {
        if (context == null) {
            return;
        }

        GameData gameData = GameData.getInstance();
        Locale locale = new Locale(gameData.getLanguageCode());
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration());
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}
