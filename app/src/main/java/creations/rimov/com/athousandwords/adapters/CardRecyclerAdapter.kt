package creations.rimov.com.athousandwords.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import creations.rimov.com.athousandwords.R
import creations.rimov.com.athousandwords.WebCard

class CardRecyclerAdapter(private val context: Context) : RecyclerView.Adapter<CardRecyclerAdapter.CardViewHolder>() {
    private lateinit var cards: List<WebCard>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val cardHolder = LayoutInflater.from(context)
            .inflate(R.layout.web_directory_card_layout, parent, false)

        return CardViewHolder(cardHolder)
    }

    override fun getItemCount(): Int {

    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {

    }


    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            val cardImage: ImageView = itemView.findViewById(R.id.web_directory_card_Vbackground)
            val cardTitle: TextView = itemView.findViewById(R.id.web_directory_card_Vtitle)
        }
    }
}