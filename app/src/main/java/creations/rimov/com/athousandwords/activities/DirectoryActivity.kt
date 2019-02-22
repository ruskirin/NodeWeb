package creations.rimov.com.athousandwords.activities

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import creations.rimov.com.athousandwords.R

class DirectoryActivity : AppCompatActivity() {

    private lateinit var cardList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_directory_view)

        cardList = findViewById(R.id.web_directory_card_list)
    }
}