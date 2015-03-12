package com.amachan.vurb;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amachan.vurb.adapter.CardAdapter;
import com.amachan.vurb.card.Card;
import com.amachan.vurb.card.CardFactory;
import com.amachan.vurb.card.MovieCard;
import com.amachan.vurb.card.MusicCard;
import com.amachan.vurb.card.PlaceCard;
import com.amachan.vurb.util.AsyncJsonPostTask;
import com.amachan.vurb.util.Constants;
import com.amachan.vurb.util.SwipeDismissRecyclerTouchListener;
import com.amachan.vurb.util.TaskCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Used to display the location, card
 * uses recyclerView and Cardview as major components
 * Supports swipe to delete, undo delete functionalities
 */
public class MainActivity extends ActionBarActivity {
    private String LOGTAG = getClass().getName();

    // initialize views
    private TextView mLocationText;
    private RecyclerView mRecyclerView;
    private ArrayList<Card> mCard;
    private CardAdapter mCardAdapter;
    private LinearLayoutManager llm;
    private View mUndoBar;
    private Card mUndoCard;
    private Button mUndoBarButton;

    // for infinite loop
    private boolean loading;
    private int pastVisibleItems, visibleItemCount, totalItemCount;
    private int visibleThreshold = 5;
    private int previousTotal = 0;

    // for undo functionality
    private int mUndoPosition;
    private Activity mActivity;


    //location, json parameters
    private String mJson;
    private String mLatitude;
    private String mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        loading = true;
        //ui
        mLocationText = (TextView) findViewById(R.id.location_text);
        mRecyclerView = (RecyclerView) findViewById(R.id.card_list);
        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mCard = new ArrayList<Card>();
        mUndoBar = findViewById(R.id.undobar);
        mUndoBarButton = (Button)findViewById(R.id.undobar_button);
        // prefs json
        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        mJson= prefs.getString("cards",null);
        mLatitude = prefs.getString("latitude",null);
        mLongitude = prefs.getString("longitude",null);
        // additional null check
        if(mLatitude != null && mLongitude != null)
            mLocationText.setText(" Latitude : " + mLatitude + " Longitude : " + mLongitude);

        // it shouldnt be null
        if(mJson != null){
            populateUI(mJson);
        }
        createListeners();
    }

    private void createListeners(){
        mUndoBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUndoCard != null && mUndoPosition >= 0){
                    mCard.add(mUndoPosition,mUndoCard);
                    mCardAdapter.notifyDataSetChanged();
                    mUndoCard = null;
                    mUndoPosition = -1;

                }
                mUndoBar.setVisibility(View.GONE);
            }
        });

        // swipe to delete
        SwipeDismissRecyclerTouchListener touchListener =
                new SwipeDismissRecyclerTouchListener(
                        mRecyclerView,
                        new SwipeDismissRecyclerTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                mUndoCard = null;
                                mUndoPosition = -1;
                                for (int position : reverseSortedPositions) {
                                    mUndoCard = mCard.remove(position);
                                    mUndoPosition = position;
                                }
                                showUndo(mUndoBar);

                                // do not call notifyItemRemoved for every item, it will cause gaps on deleting items
                                mCardAdapter.notifyDataSetChanged();
                            }
                        });
        mRecyclerView.setOnTouchListener(touchListener);

        // implement infinite scrolling over here
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = llm.getItemCount();
                pastVisibleItems = llm.findFirstVisibleItemPosition();

                if (loading) {
                    if ( totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (pastVisibleItems + visibleThreshold)) {
                    // extend the list here to make it infinite list
                    Log.i(LOGTAG, "end called");
                    loading = true;
                }

            }
        });
    }

    /**
     * Populate the UI from JSON
     * @param json
     */
    void populateUI(String json){

        mCard = getCardsFromJson(json);
        mCardAdapter = new CardAdapter(this,mCard);
        mRecyclerView.setAdapter(mCardAdapter);

    }

    /**
     * Show the undo bar
     * @param viewContainer
     */
    public static void showUndo(final View viewContainer) {
        viewContainer.setVisibility(View.VISIBLE);
        viewContainer.setAlpha(1);
        viewContainer.animate().alpha(0.4f).setDuration(5000).withEndAction(new Runnable() {

            @Override
            public void run() {
                viewContainer.setVisibility(View.GONE);
            }
        });

    }

    /**
     * Generate cards from the json
     * Add more card options over here
     * @param json
     * @return
     */
    public ArrayList<Card> getCardsFromJson(String json){

        ArrayList<Card> cardList = new ArrayList<Card>();
        try{
            JSONObject jsonObject= new JSONObject(json);
            JSONArray cards = jsonObject.getJSONArray("cards");
            for(int i = 0 ; i < cards.length(); i++){
                JSONObject cardJson = cards.getJSONObject(i);
                Card card = CardFactory.getCard(cardJson);
                cardList.add(card);
            }
        }catch (JSONException je){
            Log.d(LOGTAG, "JSON incorrect : " + je.toString());
        }
        return cardList;
    }

}
