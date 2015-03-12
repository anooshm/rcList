package com.amachan.vurb.card;

import com.amachan.vurb.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by anoosh on 3/11/15.
 */
public class CardFactory {

    public static Card getCard(JSONObject cardJson) throws JSONException {
        Card card = null;
        String type = cardJson.getString("type");
        String title = cardJson.getString("title");
        String url = cardJson.getString("imageURL");
        switch (type) {
            case Constants.TYPE_PLACE:
                card = new PlaceCard();
                String category = cardJson.getString("placeCategory");
                ((PlaceCard)card).setCategory(category);
                break;
            case Constants.TYPE_MOVIE:
                card = new MovieCard();
                String extraUrl = cardJson.getString("movieExtraImageURL");
                ((MovieCard) card).setExtraImageUrl(extraUrl);
                break;
            case Constants.TYPE_MUSIC:
                card = new MusicCard();
                String musicVideoUrl = cardJson.getString("musicVideoURL");
                ((MusicCard)card).setLink(musicVideoUrl);
                break;
            default:
                card = new GeneralCard();
        }
        card.setType(type);
        card.setTitle(title);
        if(!url.isEmpty()) {
            card.setImageUrl(url);
        }
        return card;
    }
}
