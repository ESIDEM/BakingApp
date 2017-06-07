package ng.com.techdepo.bakingapp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ESIDEM jnr on 6/3/2017.
 */

public class Ingredient implements Parcelable {

    private double quantity;
    private String measure;
    private String ingredient;

    public Ingredient(JSONObject ingredient_jason) {
        try {
            this.quantity = ingredient_jason.getDouble("quantity");
            this.measure = ingredient_jason.optString("measure");
            this.ingredient = ingredient_jason.optString("ingredient");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public double getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    protected Ingredient(Parcel in) {
        quantity = in.readDouble();
        measure = in.readString();
        ingredient = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
