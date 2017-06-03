package ng.com.techdepo.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ng.com.techdepo.bakingapp.R;
import ng.com.techdepo.bakingapp.pojo.Recipie;

/**
 * Created by ESIDEM jnr on 6/3/2017.
 */

public class RecipieAdapter extends RecyclerView.Adapter<RecipieAdapter.RecipieViewHolder>{

    final private ListItemClickListener mOnClickListener;
    final private ArrayList<Recipie> recipie;

    public RecipieAdapter(ListItemClickListener listener, ArrayList<Recipie> recipie) {
        mOnClickListener = listener;
        this.recipie = recipie;
    }


    @Override
    public RecipieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.card_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        RecipieViewHolder viewHolder = new RecipieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipieViewHolder holder, int position) {

        holder.onBind(position);

    }

    @Override
    public int getItemCount() {
        return recipie.size();
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }


    class RecipieViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        ImageView icon;
        TextView name;
        TextView servings;


        public RecipieViewHolder(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.recipes_image);
            name = (TextView) itemView.findViewById(R.id.recipes_name);
            servings = (TextView) itemView.findViewById(R.id.recipes_servings);
            itemView.setOnClickListener(this);
        }

        void onBind(int position) {
            if (!recipie.isEmpty()) {
                if(recipie.get(position).getImage().isEmpty()){
                    icon.setImageResource(R.drawable.img_no_thumb);


                }else {
                    Picasso.with(itemView.getContext()).load(recipie.get(position).getImage()).into(icon);
                }
                name.setText(recipie.get(position).getName());
                servings.setText(itemView.getContext().getString(R.string.servings) + " " + recipie.get(position).getServings());
            }
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

}
