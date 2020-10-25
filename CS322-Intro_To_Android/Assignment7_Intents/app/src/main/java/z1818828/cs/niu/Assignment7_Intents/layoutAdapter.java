/*********************************************************************
 CSCI 322/522  			  Assignment 7               		 FA2020

 Class Name: layoutAdapter.java

 Developer: Matthew Gedge
 Due Date: 11 September 2020

 Purpose: This java class starts the activity and runs the UI.
 When the calculate button is pressed, the inputs are checked for
 validity and the discriminate and x values are calculated.
 When the clear button is pressed, inputs and outputs are emptied.
 *********************************************************************/

package z1818828.cs.niu.assignment7_intents;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

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
    public layoutAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull layoutAdapter.MyViewHolder holder, int position) {
        final ItemC item = dataSet.get(position);

        holder.title.setText(item.getTitle());
        holder.description.setText(item.getImage());
        holder.image.setBackgroundResource(item.getImage());

        //Create on click listener for clicked card
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, item.getTitle() + " selected", Toast.LENGTH_LONG).show();

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
