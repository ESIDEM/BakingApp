package ng.com.techdepo.bakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    public static boolean isTablet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            if (findViewById(R.id.tablet_view) != null) {
                isTablet = true;
                FragmentManager fragmentManager = getSupportFragmentManager();
                MainActivityFragment bakesFragment = new MainActivityFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.tablet_view, bakesFragment)
                        .commit();
            } else {
                FragmentManager fragmentManager = getSupportFragmentManager();
                MainActivityFragment bakesFragment = new MainActivityFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.phone_view, bakesFragment)
                        .commit();
            }
        }


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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
