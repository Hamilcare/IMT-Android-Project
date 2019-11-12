package club.barnab2.vq.myapplication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class NewBookActivity : AppCompatActivity() {

    private lateinit var editTitleView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_book)
        editTitleView = findViewById(R.id.edit_title)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener(){
            val replyIntent = Intent()
            if(TextUtils.isEmpty(editTitleView.text)){
                setResult(Activity.RESULT_CANCELED,replyIntent)
            } else {
                val title = editTitleView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, title)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }




    }

    companion object {
        const val EXTRA_REPLY = "com.example.com.example.android.wordlistsql.REPLY"
    }


}
