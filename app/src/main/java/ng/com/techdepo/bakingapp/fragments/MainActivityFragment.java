package ng.com.techdepo.bakingapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import ng.com.techdepo.bakingapp.R;
import ng.com.techdepo.bakingapp.pojo.Recipie;
import ng.com.techdepo.bakingapp.activities.StepsActivity;
import ng.com.techdepo.bakingapp.adapters.RecipieAdapter;


import static ng.com.techdepo.bakingapp.activities.MainActivity.isTablet;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements RecipieAdapter.ListItemClickListener,
        SwipeRefreshLayout.OnRefreshListener{

    public static ArrayList<Recipie> bakes = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecipieAdapter adapter;
    private TextView no_network;
    private SwipeRefreshLayout swipeRefreshLayout;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View view =  inflater.inflate(R.layout.fragment_main, container, false);

        no_network = (TextView) view.findViewById(R.id.no_network);
        no_network.setVisibility(View.GONE);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swip_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        networkUp();

        downloadRecipes();



        return view;
    }


    void loadViews(ArrayList<Recipie> bakes) {
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recipie_list);
        RecyclerView.LayoutManager layoutManager;
        if (isTablet) {
            layoutManager = new GridLayoutManager(getActivity(), 3);
        } else {

            if(this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT)
            {
            layoutManager = new LinearLayoutManager(getActivity());
        }else {

                layoutManager = new GridLayoutManager(getActivity(), 2);
        }
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

    @Override
    public void onRefresh() {

        downloadRecipes();

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
           swipeRefreshLayout.setRefreshing(false);

           
       }
   }

    private boolean networkUp() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }


  private void downloadRecipes(){

      if(networkUp()){

          swipeRefreshLayout.setRefreshing(true);

          new FetchRecipieTask().execute();
      }else {
          no_network.setVisibility(View.VISIBLE);
          swipeRefreshLayout.setRefreshing(false);
      }
  }

}
