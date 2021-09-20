package com.example.memeapp

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.button.MaterialButton


class MainActivity : AppCompatActivity() {

    private var mUrl = ""
    private lateinit var mNextButton:MaterialButton
    private lateinit var mShareButton:MaterialButton
    private lateinit var mMemeImage:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mNextButton = findViewById(R.id.uiNextButton)
        mShareButton = findViewById(R.id.uiShareButton)
        mMemeImage = findViewById(R.id.uiMemeImageView)

        loadMeme(mMemeImage)

        mNextButton.setOnClickListener{
            loadMeme(mMemeImage)
        }
        mShareButton.setOnClickListener {
            shareMeme()
        }


    }

    private fun shareMeme() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey check this meme.. $mUrl")
        val chooser = Intent.createChooser(intent,"Send meme through.. ")
        startActivity(chooser)
    }

    private fun loadMeme(mMemeImage : ImageView) {

        val mProgressBar: ProgressBar = findViewById(R.id.progressBar)
        mProgressBar.visibility = View.VISIBLE
        // Get a RequestQueue
        val queue = VolleySingleton.getInstance(this.applicationContext).requestQueue
        val url = "https://meme-api.herokuapp.com/gimme"


        // Formulate the request and handle the response.

        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            { response ->
                // Do something with the response
                mUrl = response.getString("url")
                Glide.with(this).load(mUrl).listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        mProgressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        mProgressBar.visibility = View.GONE
                        return false
                    }

                }).into(mMemeImage)
            },
            { // Handle error
                Toast.makeText(this,"Problem",Toast.LENGTH_LONG).show()
            })


        queue.add(jsonRequest)

        // Add a request (in this example, called stringRequest) to your RequestQueue.
        VolleySingleton.getInstance(this).addToRequestQueue(jsonRequest)

    }

}