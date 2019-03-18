package creations.rimov.com.athousandwords.directory

import creations.rimov.com.athousandwords.R

class DirectoryWebCard(var title: String?, var image: Int?) {

    init {
        if(title == null) title = R.string.web_directory_card_title_def.toString()
        if(image == null) image = R.drawable.ic_web_image
    }
}