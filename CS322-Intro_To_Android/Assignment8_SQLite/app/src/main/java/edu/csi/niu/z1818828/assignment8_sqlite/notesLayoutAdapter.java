/************************************************************************
 *                                                                      *
 * CSCI 322/522  			  Assignment 8               		 FA2020 *
 *                                                            		    *
 * 	Class Name: notesLayoutAdapter.java									*
 * 																		*
 *  Developer: Matthew Gedge											*
 *   Due Date: 4 December 2020							    			*
 *   																	*
 *    Purpose: This java class the adapter between the MainActivity and *
 *    the recyclerView note objects. The adapter also does most of the  *
 *    heavy lifting for the long touch logic.                           *
 *																		*
 * *********************************************************************/

package edu.csi.niu.z1818828.assignment8_sqlite;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class notesLayoutAdapter extends RecyclerView.Adapter<notesLayoutAdapter.NoteViewHolder> {
    private static final String TAG = "notesLayoutAdapter";
    private OnNoteClickListener listener;
    private final ArrayList<Note> notes;
    final SparseBooleanArray selectedItems = new SparseBooleanArray();
    protected boolean bSelectionGroup = false;

    /**
     * Constructor for the layoutAdapter
     *
     * @param notes    the ArrayList containing the data objects
     * @param listener a listener for the interface
     */
    public notesLayoutAdapter(ArrayList<Note> notes, OnNoteClickListener listener) {
        this.notes = notes;
        this.listener = listener;
    }

    /**
     * Set for listener
     *
     * @param listener for the interface
     */
    public void setListener(OnNoteClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_note, parent, false);
        return new NoteViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.bind(notes.get(position), position);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    /**
     * Determine which notes are selected then return a list of them.
     *
     * @return the notes that are selected
     */
    public ArrayList<Note> getSelectedNotes() {
        ArrayList<Note> selectedNotes = new ArrayList<>();
        for (Note note : notes) {
            if (note.isSelected()) {
                selectedNotes.add(note);
            }
        }

        return selectedNotes;
    }


    /**
     * determine if all notes are selected
     *
     * @return true if all notes are selected
     */
    public boolean isAllSelected() {
        boolean all = true;
        for (Note note : notes) {
            if (!note.isSelected()) {
                all = false;
                break;
            }
        }

        return all;
    }

    /**
     * determine the num of notes that are selected (true)
     *
     * @return the num of selected notes
     */
    public int getSelectedSize() {
        int numSelected = 0;

        for (Note note : notes) {
            if (note.isSelected()) {
                numSelected++;
            }
        }

        return numSelected;
    }

    /**
     * delete all of the selected notes from the adapter
     */
    public void deleteSelectedNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        for (Note note : this.notes) {
            if (note.isSelected())
                notes.add(note);
        }

        this.notes.removeAll(notes);
        notifyDataSetChanged();
    }

    /**
     * sets all notes' selected boolean to true as well as the support boolean array
     */
    public void selectAll() {
        int i = 0;

        for (Note note : this.notes) {
            selectedItems.put(i, true);
            note.setSelected(true);
            i++;
        }

        bSelectionGroup = true;
        notifyDataSetChanged();
    }

    /**
     * sets all notes' selected boolean to false as well as the support boolean array
     */
    public void deselectAll() {
        int i = 0;

        for (Note note : this.notes) {
            selectedItems.put(i, false);
            note.setSelected(false);
            i++;
        }

        bSelectionGroup = false;
        notifyDataSetChanged();
    }

    /**
     * flip the selection boolean value for the note and boolean array
     *
     * @param position of the note that is flipped
     */
    public void toggleSelection(int position) {
        if (selectedItems.get(position)) {
            selectedItems.delete(position);
            notes.get(position).setSelected(false);
        } else {
            selectedItems.put(position, true);
            notes.get(position).setSelected(true);
            bSelectionGroup = true;
        }

        notifyItemChanged(position);
    }

    public interface OnNoteClickListener {
        void onNoteClick(int position);

        void onNoteLongClick(int position);

        void onSelection(boolean isSelected);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView title;
        private final TextView desc;
        private final CardView cardView;
        OnNoteClickListener clickListener;

        public NoteViewHolder(View v, OnNoteClickListener clickListener) {
            super(v);
            title = v.findViewById(R.id.textviewTitle);
            desc = v.findViewById(R.id.textViewNote);
            cardView = v.findViewById(R.id.cardView_note);
            this.clickListener = clickListener;
            v.setOnClickListener(this);
        }

        /**
         * Set the variables for the view holder
         *
         * @param note     the note that is set
         * @param position the position of the note in the adapter
         */
        void bind(Note note, int position) {
            title.setText((note.getTitle()));
            desc.setText((note.getNote()));

            //Set the selected/nonselected background
            if (note.isSelected()) {
                cardView.setBackgroundResource(R.drawable.note_background_selected);
            } else {
                cardView.setBackgroundResource(R.drawable.note_background);
            }

            //Set the boolean value for any selection enable to true
            bSelectionGroup = getSelectedSize() > 0;

            //Change view on long click
            itemView.setOnLongClickListener(v -> {
                Log.w(TAG, "onLongClick: pressed: " + position + " selected: " + note.isSelected() + " sparseBool: " + selectedItems.get(position));

                toggleSelection(position);

                if (note.isSelected()) {
                    cardView.setBackgroundResource(R.drawable.note_background);
                    if (getSelectedNotes().size() == 0) {
                        listener.onSelection(false);
                    }
                } else {
                    cardView.setBackgroundResource(R.drawable.note_background_selected);
                    listener.onSelection(true);
                }

                notifyDataSetChanged();

                if (listener != null) {
                    listener.onNoteLongClick(position);
                }

                return true;
            });
        }

        @Override
        public void onClick(View v) {
            clickListener.onNoteClick(getAdapterPosition());
        }
    }
}
