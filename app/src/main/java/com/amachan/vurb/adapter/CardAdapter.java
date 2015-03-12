package com.amachan.vurb.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amachan.vurb.R;
import com.amachan.vurb.card.Card;
import com.amachan.vurb.card.MovieCard;
import com.amachan.vurb.card.MusicCard;
import com.amachan.vurb.card.PlaceCard;
import com.amachan.vurb.util.AsyncImageDownloadTask;
import com.amachan.vurb.util.Constants;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by anoosh on 3/10/15.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder>{

    private ArrayList<Card> mCard;
    private Context mContext;

    public CardAdapter(Context context, ArrayList<Card> card){
        this.mCard = card;
        this.mContext = context;
    }

    /**
     * get the type of card
     * @param card
     * @return
     */
    public int getCardType(Card card){
        int type = 0;
        switch(card.getType()){
            case Constants.TYPE_MOVIE:
                type = 0;
                break;
            case Constants.TYPE_MUSIC:
                type = 1;
                break;
            case Constants.TYPE_PLACE:
                type = 2;
                break;
        }
        return type;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        CardViewHolder vh;
        View cardView;
        // create according to the itemType
        switch(i){
            case 0:
                cardView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.list_movie_item, viewGroup, false);
                vh = new MovieCardViewHolder(cardView);
                break;
            case 1:
                cardView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.list_music_item, viewGroup, false);
                vh = new MusicCardViewHolder(cardView);
                break;
            case 2:
                cardView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.list_place_item, viewGroup, false);
                vh = new PlaceCardViewHolder(cardView);
                break;
            default:
                cardView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.list_item, viewGroup, false);
                vh = new CardViewHolder(cardView);
        }
        return vh;
    }

    @Override
    public int getItemViewType(int position){
        Card card = mCard.get(position);
        return getCardType(card);
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int i) {

        Card card = mCard.get(i);
        String title = card.getTitle();
        String image_url = card.getImageUrl();

        CardViewHolder vh;
        switch(getItemViewType(i)){
            case 0:
                vh =  (MovieCardViewHolder) cardViewHolder;
                String extraImage = ((MovieCard)card).getExtraImageUrl();
                if(!extraImage.isEmpty()){
                    ((MovieCardViewHolder)vh).mImageViewExtra.setVisibility(View.VISIBLE);
                    AsyncImageDownloadTask asyncImageDownloadTask = new AsyncImageDownloadTask(
                            ((MovieCardViewHolder)vh).mImageViewExtra);
                    asyncImageDownloadTask.execute(extraImage);
                } else {
                    ((MovieCardViewHolder)vh).mImageViewExtra.setVisibility(View.GONE);
                }
                break;
            case 1:
                vh =  (MusicCardViewHolder) cardViewHolder;
                final String musicLink = ((MusicCard)card).getLink();
                if(!musicLink.isEmpty()){
                    ((MusicCardViewHolder)vh).mLink.setVisibility(View.VISIBLE);
                    ((MusicCardViewHolder)vh).mLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent browseIntent = new Intent();
                            browseIntent.setAction(Intent.ACTION_VIEW);
                            browseIntent.setData(Uri.parse(musicLink));
                            mContext.startActivity(browseIntent);

                        }
                    });
                } else {
                    ((MusicCardViewHolder)vh).mLink.setVisibility(View.GONE);
                }
                break;
            case 2:
                vh =  (PlaceCardViewHolder) cardViewHolder;
                String category = ((PlaceCard)card).getCategory();
                if(!category.isEmpty()) {
                    ((PlaceCardViewHolder) vh).mCategory.setVisibility(View.VISIBLE);
                    ((PlaceCardViewHolder) vh).mCategory.setText(category);
                }else {
                    ((PlaceCardViewHolder) vh).mCategory.setVisibility(View.GONE);
                }
                break;
            default:
                vh =  cardViewHolder;
        }

        vh.mTitle.setText(title);
        if(!image_url.isEmpty()){
            vh.mImageView.setVisibility(View.VISIBLE);
            AsyncImageDownloadTask asyncImageDownloadTask = new AsyncImageDownloadTask(
                    vh.mImageView);
            asyncImageDownloadTask.execute(card.getImageUrl());
        } else {
            vh.mImageView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mCard.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder{
        TextView mTitle;
        ImageView mImageView;

        CardViewHolder(View v){
            super(v);
            mTitle = (TextView) v.findViewById(R.id.title);
            mImageView = (ImageView) v.findViewById(R.id.card_image);
        }

    }

    class MovieCardViewHolder extends CardViewHolder{
        ImageView mImageViewExtra;

        MovieCardViewHolder(View v){
            super(v);
            mImageViewExtra = (ImageView) v.findViewById(R.id.actor_image);
        }

    }

    class MusicCardViewHolder extends CardViewHolder{
        Button mLink;

        MusicCardViewHolder(View v){
            super(v);
            mLink = (Button) v.findViewById(R.id.link_button);
        }

    }

    class PlaceCardViewHolder extends CardViewHolder{
        TextView mCategory;

        PlaceCardViewHolder(View v){
            super(v);
            mCategory = (TextView) v.findViewById(R.id.category);
        }

    }
}
