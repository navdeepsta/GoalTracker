package com.navdeep.goaltracker.Adapters;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    private CardView cardView;
    private ViewHolder(@NonNull CardView itemView) {
        super(itemView);
        cardView = itemView;
    }
    public static ViewHolder getViewHolder(CardView cv) {
        return new ViewHolder(cv);
    }
    public CardView getCardView(){
        return cardView;
    }
}