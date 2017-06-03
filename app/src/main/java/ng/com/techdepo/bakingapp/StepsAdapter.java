package ng.com.techdepo.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;



/**
 * Created by ESIDEM jnr on 6/3/2017.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder>{

    final private ListItemClickListener mOnClickListener;
    final private ArrayList<Steps> steps;

    public StepsAdapter(ListItemClickListener listener, ArrayList<Steps> steps) {
        mOnClickListener = listener;
        this.steps = steps;
    }


    @Override
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.steps_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        StepsViewHolder viewHolder = new StepsViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder, int position) {

        holder.onBind(position);

    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }


    class StepsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        ImageView icon;
        TextView short_description;
        TextView full_description;


        public StepsViewHolder(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.step_image);
            short_description = (TextView) itemView.findViewById(R.id.short_desc);
            full_description = (TextView) itemView.findViewById(R.id.full_descc);
            itemView.setOnClickListener(this);
        }

        void onBind(int position) {
            if (!steps.isEmpty()) {
                if(steps.get(position).getThumbnailURL().isEmpty()){
                    icon.setImageResource(R.drawable.img_no_thumb);


                }else {
                    Picasso.with(itemView.getContext()).load(steps.get(position).getThumbnailURL()).into(icon);
                }
                short_description.setText(steps.get(position).getShortDescription());
                full_description.setText( steps.get(position).getDescription());
            }
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

}
