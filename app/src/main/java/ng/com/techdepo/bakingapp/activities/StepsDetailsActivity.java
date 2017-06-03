package ng.com.techdepo.bakingapp.activities;

import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import ng.com.techdepo.bakingapp.R;
import ng.com.techdepo.bakingapp.fragments.StepsDetailsActivityFragment;


public class StepsDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_details);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            StepsDetailsActivityFragment stepsDetailsFragment = new StepsDetailsActivityFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.stepsdetailsframe, stepsDetailsFragment)
                    .commit();
        }


    }

}
