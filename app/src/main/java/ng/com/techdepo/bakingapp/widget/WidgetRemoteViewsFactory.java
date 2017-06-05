package ng.com.techdepo.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;


import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ng.com.techdepo.bakingapp.R;
import ng.com.techdepo.bakingapp.pojo.Recipie;

import static ng.com.techdepo.bakingapp.fragments.MainActivityFragment.bakes;

/**
 * Created by ESIDEM jnr on 6/5/2017.
 */

public class WidgetRemoteViewsFactory implements RemoteViewsFactory {

    private Context mContext;

    public WidgetRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }


    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        try {
            new FetchRecipieTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
        }
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return bakes.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.collection_widget_list_item);
        try {
            rv.setImageViewBitmap(R.id.icon, BitmapFactory.decodeStream(new URL(bakes.get(position).getImage()).openConnection().getInputStream()));
        } catch (IOException e) {
        }
        rv.setTextViewText(R.id.name, bakes.get(position).getName());
        rv.setTextViewText(R.id.servings, mContext.getString(R.string.servings) + " " + bakes.get(position).getServings());
        for (int i=0;i<bakes.get(position).getIngredients().size();i++){
            RemoteViews  ing= new RemoteViews(mContext.getPackageName(), R.layout.ingredient_item);
            ing.setTextViewText(R.id.ingredient,bakes.get(position).getIngredients().get(i).getIngredient());
            ing.setTextViewText(R.id.measure,bakes.get(position).getIngredients().get(i).getMeasure());
            ing.setTextViewText(R.id.quantity,bakes.get(position).getIngredients().get(i).getQuantity()+"");
            rv.addView(R.id.ingerdient_list,ing);
        }

        Intent intent = new Intent();
        intent.putExtra("item", position);
        rv.setOnClickFillInIntent(R.id.bacckground, intent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public class FetchRecipieTask extends AsyncTask<Void, Void, ArrayList<Recipie>> {

        @Override
        protected ArrayList<Recipie> doInBackground(Void... params) {

            //Udacity Recipes
            final String UDACITY_BASE_URL_MOVIE = "https://go.udacity.com/android-baking-app-json";

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

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
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        reader.close();
                    }
                } catch (Exception e) {
                }
                return bakes;
            }
        }
    }
}
