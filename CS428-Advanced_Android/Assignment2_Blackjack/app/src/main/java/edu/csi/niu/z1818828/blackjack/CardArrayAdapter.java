package edu.csi.niu.z1818828.blackjack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardArrayAdapter extends RecyclerView.Adapter<CardArrayAdapter.ViewHolder> {
    private Map<String, Bitmap> bitmaps = new HashMap<>();
    private LayoutInflater inflater;
    private Context context;
    private List<Card> cards;

    CardArrayAdapter(Context context, List<Card> cards) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.cards = cards;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Get the current card from the player/dealer deck
        Card card = cards.get(position);

        //Populate the bitmap with its images
        if (bitmaps.isEmpty())
            populateBitmap();

        //Set the image of the card to the picture of the card
        if (card == null) {
            holder.cardImageView.setImageResource(R.drawable.cardback);
        } else
            holder.cardImageView.setImageBitmap(bitmaps.get(card.cardPicture));
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImageView;

        ViewHolder(View itemView) {
            super(itemView);

            cardImageView = itemView.findViewById(R.id.imageViewCard);
        }
    }

    private void populateBitmap() {
        Bitmap bitmap = null;
        String id, value;
        int key;

        //Create bitmaps for all playable cards
        for (int suit = 0; suit < 4; suit++) {
            for (int rank = 0; rank < 13; rank++) {
                id = resolvePNG(suit, rank);
                value = "c" + id;
                key = context.getResources().getIdentifier(value, "drawable", context.getPackageName());
                bitmap = BitmapFactory.decodeResource(context.getResources(), key);
                bitmaps.put(id, bitmap);
            }
        }

        //Add a final blank card
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cardback);
        bitmaps.put(null, bitmap);
    }

    private String resolvePNG(int suit, int rank) {
        String cardName = null;
        String sut = null;
        int rnk = 1;

        //Search through each suit and rank
        for (int s = 0; s < 4; s++) {
            for (int r = 0; r < 13; r++) {
                //When the rank and suit are found, set the corresponding variables
                if ((suit == s) && (rank == r)) {
                    switch (s) {
                        case 0:
                            sut = "c";
                            break;
                        case 1:
                            sut = "d";
                            break;
                        case 2:
                            sut = "h";
                            break;
                        case 3:
                            sut = "s";
                            break;
                    }

                    rnk = r + 1;
                }
            }
        }

        //Assuming the suit is not empty, set it to the int, char pair
        if (sut != null)
            cardName = rnk + sut;

        return cardName;
    }

}