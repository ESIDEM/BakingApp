package ng.com.techdepo.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;



import java.util.ArrayList;

import ng.com.techdepo.bakingapp.pojo.Ingredient;
import ng.com.techdepo.bakingapp.R;

/**
 * Created by ESIDEM jnr on 6/3/2017.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngridientViewHolder>{

    final private ArrayList<Ingredient> ingredients;

    public IngredientsAdapter( ArrayList<Ingredient> ingredients) {

        this.ingredients = ingredients;
    }


    @Override
    public IngridientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.ingredient_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        IngridientViewHolder viewHolder = new IngridientViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(IngridientViewHolder holder, int position) {

        holder.onBind(position);

    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }




    class IngridientViewHolder extends RecyclerView.ViewHolder
            {

        TextView ingredient;
        TextView quantity;
        TextView measure;

        public IngridientViewHolder(View itemView) {
            super(itemView);

            ingredient = (TextView) itemView.findViewById(R.id.ingredient);
            quantity = (TextView) itemView.findViewById(R.id.quantity);
            measure = (TextView) itemView.findViewById(R.id.measure);

        }

        void onBind(int position) {
            if (!ingredients.isEmpty()) {

                ingredient.setText(ingredients.get(position).getIngredient());
                quantity.setText( String.valueOf(ingredients.get(position).getQuantity()));
                measure.setText( ingredients.get(position).getMeasure());
            }
        }


    }

}
