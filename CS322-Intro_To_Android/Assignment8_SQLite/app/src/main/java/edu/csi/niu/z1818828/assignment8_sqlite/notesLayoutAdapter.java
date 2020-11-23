package edu.csi.niu.z1818828.assignment8_sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class notesLayoutAdapter extends RecyclerView.Adapter<notesLayoutAdapter.MyViewHolder> {
    private SQLiteDatabase dbManager;
    private OnNoteListener listener;
    List<Note> notes;
    Context context;

    public notesLayoutAdapter(Context context, List<Note> notes, OnNoteListener listener) {
        this.notes = notes;
        this.context = context;
        this.listener = listener;
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

        holder.title.setText((note.getTitle()));
        holder.desc.setText((note.getNote()));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private TextView desc;
        private CardView cardView;
        OnNoteListener listener;

        public MyViewHolder(View v, OnNoteListener listener) {
            super(v);
            title = v.findViewById(R.id.textviewTitle);
            desc = v.findViewById(R.id.textViewNote);
            cardView = v.findViewById(R.id.cardView_note);
            this.listener = listener;

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);

    }
}
