package ng.com.techdepo.bakingapp.pojo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ESIDEM jnr on 6/3/2017.
 */

public class Ingredient {

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
}
