package edu.csi.niu.z1818828.assignment8_sqlite;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class notesLayoutAdapter extends RecyclerView.Adapter<notesLayoutAdapter.MyViewHolder> {
    private Context context;
    private OnNoteClickListener listener;
    private ArrayList<Note> notes;
    final SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int currentSelectedPosition;


    public notesLayoutAdapter(Context context, ArrayList<Note> notes) {
        this.notes = notes;
        this.context = context;
    }

    public void setListener(OnNoteClickListener listener) {
        this.listener = listener;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    @NonNull
    @Override
    public notesLayoutAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_note, parent, false);
        return new MyViewHolder(v, listener);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull notesLayoutAdapter.MyViewHolder holder, int position) {
        final Note note = notes.get(position);
        holder.bind(note);

        holder.title.setText((note.getTitle()));
        holder.desc.setText((note.getNote()));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null)
                    listener.onNoteLongClick(position);
                return true;
            }
        });

        if (currentSelectedPosition == position)
            currentSelectedPosition = -1;
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void deleteNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        for(Note note : this.notes) {
            if(note.isSelected())
                notes.add(note);
        }

        this.notes.removeAll(notes);
        notifyDataSetChanged();
        currentSelectedPosition = -1;
    }

    public void selectAll() {
        int i = 0;

        for (Note note : this.notes) {
            selectedItems.put(i, true);
            note.setSelected(true);
        }

        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        currentSelectedPosition = position;
        if (selectedItems.get(position)) {
            selectedItems.delete(position);
            notes.get(position).setSelected(false);
        } else {
            selectedItems.put(position, true);
            notes.get(position).setSelected(true);
        }

        notifyItemChanged(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private TextView desc;
        private CardView cardView;
        OnNoteClickListener clickListener;

        public MyViewHolder(View v, OnNoteClickListener clickListener) {
            super(v);
            title = v.findViewById(R.id.textviewTitle);
            desc = v.findViewById(R.id.textViewNote);
            cardView = v.findViewById(R.id.cardView_note);
            this.clickListener = clickListener;

            v.setOnClickListener(this);
        }

        void bind(Note note) {
            Drawable background = itemView.getBackground();

            if(note.isSelected()) {
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setShape(GradientDrawable.RECTANGLE);
                gradientDrawable.setColor(Color.rgb(232, 240, 253));
                itemView.setBackground(gradientDrawable);
            } else {
                itemView.setBackground(background);
            }
        }

        @Override
        public void onClick(View v) {
            clickListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteClickListener {
        void onNoteClick(int position);
        void onNoteLongClick(int position);
    }
}
