package ng.com.techdepo.bakingapp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ng.com.techdepo.bakingapp.pojo.Ingredient;
import ng.com.techdepo.bakingapp.pojo.Steps;

import static android.R.attr.id;

/**
 * Created by ESIDEM jnr on 6/3/2017.
 */

public class Recipie implements Parcelable{
    int id;
    private String name;
    private ArrayList<Ingredient> ingredients;
   private ArrayList<Steps> steps;
    private String servings;
    private String image;

    public Recipie(){}

    public Recipie(JSONObject bake_jason) {
        try {
            this.name = bake_jason.getString("name");
            this.ingredients = new ArrayList<>();
            JSONArray ingredientsJA = bake_jason.getJSONArray("ingredients");
            for (int i = 0; i < ingredientsJA.length(); i++) {
                ingredients.add(new Ingredient(ingredientsJA.getJSONObject(i)));
            }
            this.steps = new ArrayList<>();
            JSONArray stepsJA = bake_jason.getJSONArray("steps");
            for (int i = 0; i < stepsJA.length(); i++) {
                steps.add(new Steps(stepsJA.getJSONObject(i)));
            }
            this.servings = bake_jason.getString("servings");
            this.image = bake_jason.getString("image");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getServings() {
        return servings;
    }

    public ArrayList<Steps> getSteps() {
        return steps;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recipie> CREATOR = new Creator<Recipie>() {
        @Override
        public Recipie createFromParcel(Parcel in) {
            return new Recipie(in);
        }

        @Override
        public Recipie[] newArray(int size) {
            return new Recipie[size];
        }
    };

    protected Recipie(Parcel in) {
        id = in.readInt();
        servings = in.readString();
        name = in.readString();
        image = in.readString();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        steps = in.createTypedArrayList(Steps.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(servings);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
    }


}
