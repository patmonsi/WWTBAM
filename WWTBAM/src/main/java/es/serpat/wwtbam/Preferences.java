package es.serpat.wwtbam;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by SergiuDaniel on 2/07/13.
 */
public class Preferences extends PreferenceActivity implements OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new PrefsFragment()).commit();

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //TODO Hay que implementar un AlertDialog o algo que recuerde al usuario que si
        // cambia preferencias, perdera la partida actual, si es que hay alguna guardada.
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Let's do something when my counter preference value changes
        Log.v("asdlkjh",getResources().getString(R.string.SHARED_PREF_GAME));
        if (sharedPreferences.getBoolean(getResources().getString(R.string.SHARED_PREF_GAME),true) &&
                (key.equals(getResources().getString(R.string.SHARED_PREF_NUM_JOKERS)) ||
                key.equals(getResources().getString(R.string.SHARED_PREF_NAME_KEY)))) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(getResources().getString(R.string.SHARED_PREF_GAME), false);
            Toast.makeText(this, getResources().getString(R.string.ad_new_game), Toast.LENGTH_LONG).show();
        }
    }

    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
    }


}
