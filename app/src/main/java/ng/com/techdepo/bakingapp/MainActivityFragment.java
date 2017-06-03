package ng.com.techdepo.bakingapp;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static ng.com.techdepo.bakingapp.MainActivity.isTablet;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements RecipieAdapter.ListItemClickListener{

    public static ArrayList<Recipie> bakes = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecipieAdapter adapter;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View view =  inflater.inflate(R.layout.fragment_main, container, false);

        new FetchRecipieTask().execute();

        return view;
    }


    void loadViews(ArrayList<Recipie> bakes) {
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recipie_list);
        RecyclerView.LayoutManager layoutManager;
        if (isTablet) {
            layoutManager = new GridLayoutManager(getActivity(), 3);
        } else {
            layoutManager = new LinearLayoutManager(getActivity());
        }
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecipieAdapter(this, bakes);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Intent intent = new Intent(getActivity(), StepsActivity.class);
        intent.putExtra("item", clickedItemIndex);
        startActivity(intent);

    }

   public class FetchRecipieTask extends AsyncTask<Void,Void,ArrayList<Recipie>>{

       @Override
       protected ArrayList<Recipie> doInBackground(Void... params) {
           HttpURLConnection urlConnection = null;
           BufferedReader reader = null;

           //Udacity Recipes
           final String UDACITY_BASE_URL_MOVIE = "https://go.udacity.com/android-baking-app-json";


           try {
               Uri builtUri = Uri.parse(UDACITY_BASE_URL_MOVIE)
                       .buildUpon()
                       .build();

               URL url = new URL(builtUri.toString());

               urlConnection = (HttpURLConnection) url.openConnection();
               urlConnection.setRequestMethod("GET");
               urlConnection.connect();

               InputStream inputStream = urlConnection.getInputStream();
               StringBuffer buffer = new StringBuffer();
               if (inputStream == null) {
                   return null;
               }
               reader = new BufferedReader(new InputStreamReader(inputStream));

               String line;
               while ((line = reader.readLine()) != null) {
                   buffer.append(line + "\n");
               }
               if (buffer.length() == 0) {
                   return null;
               }
               JSONArray movieArray = new JSONArray(buffer.toString());
               bakes = new ArrayList<>();
               for (int i = 0; i < movieArray.length(); i++) {
                   bakes.add(new Recipie(movieArray.getJSONObject(i)));
                   Log.e("name: ", bakes.get(i).getName());
               }
               return bakes;
           } catch (Exception e) {
               e.printStackTrace();
               return bakes;
           } finally {
               try {
                   if (urlConnection != null) {
                       urlConnection.disconnect();
                   }
                   if (reader != null) {
                       reader.close();
                   }
               } catch (Exception e) {

                   Log.d("MainActivityFragment", e.getMessage());
               }
           }
       }

       @Override
       protected void onPostExecute(ArrayList<Recipie> recipies) {

           loadViews(recipies);
           
       }
   }


}
