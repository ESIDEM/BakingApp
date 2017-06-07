package ng.com.techdepo.bakingapp.appwidget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import ng.com.techdepo.bakingapp.R;
import ng.com.techdepo.bakingapp.pojo.Ingredient;
import ng.com.techdepo.bakingapp.pojo.Recipie;

/**
 * Created by ESIDEM jnr on 6/7/2017.
 */

public class RecipeStackViewWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackViewsRemoteFactory(this.getApplicationContext(), intent);
    }
}

class StackViewsRemoteFactory implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<Recipie> mRecipes;
    private Context mContext;

    public StackViewsRemoteFactory(Context context, Intent intent) {
        mContext = context;
    }

    public void onCreate() {
    }

    public void onDestroy() {
        mRecipes.clear();
    }

    public int getCount() {
        if (mRecipes == null)
            return 0;

        return mRecipes.size();
    }

    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);

        Recipie recipe = mRecipes.get(position);

        rv.setTextViewText(R.id.widget_item_recipe_name, recipe.getName());

        String ingredients = "";
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredients += " - " + ingredient.getIngredient() + "\n";
        }

        rv.setTextViewText(R.id.widget_item_ingredients,ingredients);

        Bundle extras = new Bundle();
        extras.putParcelable(mContext.getString(R.string.extra_recipe),recipe);
        Intent fillIntent = new Intent();
        fillIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.recipe_widget_item,fillIntent);

        return rv;
    }

    public RemoteViews getLoadingView() {
        return null;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public long getItemId(int position) {
        return position;
    }

    public boolean hasStableIds() {
        return true;
    }

    public void onDataSetChanged() {
        if(mRecipes == null) {

            //Udacity Recipes
            final String UDACITY_BASE_URL_MOVIE = "https://go.udacity.com/android-baking-app-json";
            Uri builtUri = Uri.parse(UDACITY_BASE_URL_MOVIE);

            HttpURLConnection urlConnection = null;
            try {
                // get URL
                URL url = new URL(builtUri.toString());
                // open connection
                urlConnection = (HttpURLConnection) url.openConnection();

                // create an input stream reader
                InputStreamReader reader = new InputStreamReader(urlConnection.getInputStream());

                Recipie[] recipeArray = new Gson().fromJson(reader, Recipie[].class);
                mRecipes = new ArrayList<>(Arrays.asList(recipeArray));

            } catch (MalformedURLException e) {
                Log.e("MalformedURLException", e.getMessage());
            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }
    }
}
