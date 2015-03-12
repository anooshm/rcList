# rcList
<b>Read JSON, Location and Display it in list of cards</b><br/>
<br/>
Completely scalable to add more cards, more apis <br/>
Uses RecyclerView with ViewHolders for optimum performance<br/>
<b>Extras:</b><br/>
     a) Swipe to delete card like google now<br/>
     b) Undo delete like google now<br/>
     c) Has infite scrolling but it is not activated.<br/>
<br/>
<b>i) To add a new type of card:</b><br/>
     a) ADD the Card class to com/amachan/card/ by extending the default CARD class<br/>
     b) ADD the card to the CardFactory at com/amachan/card<br/>
     c) ADD the card UI to res/list_item_<card_type><br/>
     d) Handle the CardAdapter for the new type<br/>
<br/>
<b>ii) To add more api support:</b><br/>
     a) Modify com/amachan/utils/ConnectionHelper to include more api's<br/>
<br/>
<b>If the same card need to displayed in different places:</b><br/>
    i) Dynamically create the card using Card class (which is a light weight object) and card UI in res/list_item_<>.<br/>
    ii) Use the same ViewHolder with RecyclerView to display the cards.<br/>
