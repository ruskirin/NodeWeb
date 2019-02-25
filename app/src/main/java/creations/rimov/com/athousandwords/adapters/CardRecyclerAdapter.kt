package creations.rimov.com.athousandwords.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import creations.rimov.com.athousandwords.DirectoryWebCard
import creations.rimov.com.athousandwords.R
import creations.rimov.com.athousandwords.activities.CardClickListener

class CardRecyclerAdapter(private val context: Context,
                          private val clickListener: CardClickListener
)
    : RecyclerView.Adapter<CardRecyclerAdapter.CardViewHolder>() {

    private val cardList: List<DirectoryWebCard> = arrayListOf(
        DirectoryWebCard(null, null), DirectoryWebCard("Sample Web", null)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val cardHolder = LayoutInflater.from(context)
            .inflate(R.layout.web_directory_card_layout, parent, false)

        return CardViewHolder(cardHolder, clickListener)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {

        if(cardList.size > position) {
            holder.cardImage.setImageResource(cardList[position].image!!)
            holder.cardTitle.text = cardList[position].title
        }
    }

    override fun getItemCount() = cardList.size

    fun addCardList(card: DirectoryWebCard) {
        this.cardList.plus(card)
    }

    /**
     * VIEW HOLDER
     **/
    class CardViewHolder(itemView: View, private val clickListener: CardClickListener)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val cardImage: ImageView = itemView.findViewById(R.id.web_directory_card_Vbackground)
        val cardTitle: TextView = itemView.findViewById(R.id.web_directory_card_Vtitle)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener.onClick(adapterPosition)
        }
    }
}