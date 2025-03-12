package com.ladyprogram.zodiaco

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.ViewCompat

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_HOROSCOPE_ID = "HOROSCOPE_ID"
    }


    lateinit var nameTextView: TextView
    lateinit var dateTextView: TextView
    lateinit var iconImageView: ImageView

    lateinit var horoscope: Horoscope

    var isFavorite = false
    lateinit var favoriteMenu: MenuItem

    lateinit var session: SessionManager





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_detail)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        session = SessionManager(this)
        val id = intent.getStringExtra(EXTRA_HOROSCOPE_ID)!!
        horoscope = Horoscope.findById(id)

        initView()

        loadData()
    }

    private fun loadData() {
        supportActionBar?.setTitle(horoscope.name)
        supportActionBar?.setSubtitle(horoscope.dates)

        nameTextView.text = getString(horoscope.name)
        dateTextView.setText(horoscope.dates)
        iconImageView.setImageResource(horoscope.icon)

        isFavorite = session.isFavorite(horoscope.id)
        //horoscope.id == session.getFavorite()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_detail, menu)

        menu.findItem(R.id.action_favorito)

        return true
    }

    private fun initView() {
        nameTextView = findViewById(R.id.nameTextView)
        dateTextView = findViewById(R.id.dateTextView)
        iconImageView = findViewById(R.id.iconImageView)

        session = session.isFavorite(horoscope.id)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorito -> {
                //println("Menu favorito")
                isFavorite = !isFavorite
                if (isFavorite) {
                    session.setFavorite(horoscope.id)

                }
                session.setFavorite()
                setFavoriteIcon()
                true
            }

            R.id.action_share -> {
                val sendIntent = Intent()
                sendIntent.setAction(Intent.ACTION_SEND)
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send")
                sendIntent.setType("text/plain")

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

                private fun setFavoriteIcon() {
                    if (isFavorite) {
                        favoriteMenu.setIcon(R.drawable.ic_favorite_selected)
                    } else {
                        favoriteMenu.setIcon(R.drawable.ic_favorite)
                    }
                }
    }
            //val horoscope = Horoscope.findById(id)
            //findViewById<TextView>(R.id.text).text = "id: ${getString(horoscope.name)}"



