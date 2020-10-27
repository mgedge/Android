/*********************************************************************
 CSCI 322/522  			  Assignment 7               		 FA2020

 Class Name: layoutAdapter.java

 Developer: Matthew Gedge
 Due Date: 6 November 2020

 Purpose: This java class is the adapter between the recycler view and
 its card items. A list is created to keep track of the individual cards.
 *********************************************************************/

package z1818828.cs.niu.assignment7_intents;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class layoutAdapter extends RecyclerView.Adapter<layoutAdapter.MyViewHolder> {
    public static final String EXTRA = "z1818828.cs.niu.edu.EXTRA";
    private List<ItemC> dataSet;
    private Context context;

    layoutAdapter(Context context, List<ItemC> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    /*
        Setup to use the cardview for each item in recycler
     */
    public layoutAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    /*
        Populate the card with its respective data and setup a listener for when each card
        is clicked.
     */
    public void onBindViewHolder(@NonNull layoutAdapter.MyViewHolder holder, int position) {
        final ItemC item = dataSet.get(position);

        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.image.setBackgroundResource(item.getImage());

        //Create on click listener for clicked card
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, item.getTitle() + " selected", Toast.LENGTH_LONG).show();

                //Setup the next activity
                Intent intent = new Intent(v.getContext(), DisplayCardActivity.class);

                //Store data for card
                intent.putExtra(EXTRA, item.getTitle());

                //Start new activity
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    /*
        Get the card u.i IDs
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView description;
        private ImageView image;
        private CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            image = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
