package club.barnab2.quiedeville.myapplication

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import club.barnab2.quiedeville.myapplication.entity.Book
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NewBookActivity : AppCompatActivity() {

    private lateinit var editTitleView: EditText
    private lateinit var editIsbnView: EditText
    private lateinit var editPriceView: EditText
    private lateinit var coverView: ImageView
    private var coverUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_book)
        editTitleView = findViewById(R.id.edit_title)
        editIsbnView = findViewById(R.id.edit_isbn)
        editPriceView = findViewById(R.id.edit_price)
        coverView = findViewById(R.id.cover_preview)

        //Pick Cover
        val pickCoverButton = findViewById<FloatingActionButton>(R.id.take_picture)
        pickCoverButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                } else {
                    //permission already granted
                    pickImageFromGallery();
                }
            }
        }


        //Save
        val saveButton = findViewById<Button>(R.id.button_save)
        saveButton.setOnClickListener() {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editTitleView.text)
                || TextUtils.isEmpty(editIsbnView.text)
                || TextUtils.isEmpty(editPriceView.text)
                || coverUri == null
            ) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val newBook = Book(
                    editIsbnView.text.toString(),
                    editTitleView.text.toString(),
                    editPriceView.text.toString().toFloat(),
                    coverUri.toString(),
                    arrayOf("")
                )
                replyIntent.putExtra(EXTRA_REPLY, newBook)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }


    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            coverView.setImageURI(data?.data)
            coverUri = data?.data
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.example.com.example.android.newBook.REPLY"

        const val IMAGE_PICK_CODE = 1000;
        const val PERMISSION_CODE = 1001;
    }


}
