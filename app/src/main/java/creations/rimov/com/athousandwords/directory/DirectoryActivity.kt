package creations.rimov.com.athousandwords.directory

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import creations.rimov.com.athousandwords.R
import creations.rimov.com.athousandwords.web.WebActivity

class DirectoryActivity : AppCompatActivity(), CardClickListener {

    private lateinit var cardRecyclerView: RecyclerView
    private lateinit var cardRecyclerAdapter: CardRecyclerAdapter
    private lateinit var cardRecyclerLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_directory_view)

        cardRecyclerAdapter = CardRecyclerAdapter(this, this)
        cardRecyclerLayoutManager = LinearLayoutManager(this)
        cardRecyclerView = findViewById<RecyclerView>(R.id.web_directory_card_recycler).apply {
            adapter = cardRecyclerAdapter
            layoutManager = cardRecyclerLayoutManager
            setHasFixedSize(true)
        }
    }

    override fun onClick(position: Int) {
        Toast.makeText(this, "Clicked: $position", Toast.LENGTH_SHORT).show()

        val toWeb = Intent(this, WebActivity::class.java)
        startActivity(toWeb)
    }
}

interface CardClickListener {

    fun onClick(position: Int)
}