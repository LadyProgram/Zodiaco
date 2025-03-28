package com.ladyprogram.zodiaco

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.ViewCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_HOROSCOPE_ID = "HOROSCOPE_ID"
    }


    lateinit var nameTextView: TextView
    lateinit var dateTextView: TextView
    lateinit var iconImageView: ImageView
    lateinit var horoscopeLuckTextView: TextView
    lateinit var progressBar: LinearProgressIndicator
    lateinit var bottomNavigation: BottomNavigationView

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
        getHoroscopeLuck("daily")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_detail, menu)

        favoriteMenu = menu.findItem(R.id.action_favorite)
        setFavoriteIcon()

        return true
    }

    private fun initView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        nameTextView = findViewById(R.id.nameTextView)
        dateTextView = findViewById(R.id.dateTextView)
        iconImageView = findViewById(R.id.iconImageView)
        horoscopeLuckTextView = findViewById(R.id.horoscopeLuckTextView)
        progressBar = findViewById(R.id.progressBar)
        bottomNavigation = findViewById(R.id.bottomNavigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_daily -> {
                    getHoroscopeLuck("daily")
                    true
                }

                R.id.action_weekly -> {
                    getHoroscopeLuck("weekly")
                    true
                }

                R.id.action_monthly -> {
                    getHoroscopeLuck("monthly")
                    true
                }

                else -> false
            }
            //session = session.isFavorite(horoscope.id)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorite -> {
                isFavorite = !isFavorite
                session.setFavorite(horoscope.id)
                setFavoriteIcon()
                true
            }
            R.id.action_share -> {
                val sendIntent = Intent()
                sendIntent.setAction(Intent.ACTION_SEND)
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                sendIntent.setType("text/plain")

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                true
            }
            android.R.id.home -> {
                finish()
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


        private fun getHoroscopeLuck(period: String) {
            progressBar.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                var urlConnection: HttpsURLConnection? = null

                try {
                    val url =
                        URL("https://horoscope-app-api.vercel.app/api/v1/get-horoscope/$period?sign=${horoscope.id}&day=TODAY")
                    urlConnection = url.openConnection() as HttpsURLConnection


                    if (urlConnection.responseCode == 200) {
                        val rd = BufferedReader(InputStreamReader(urlConnection.inputStream))
                        var line: String?
                        val stringBuilder = StringBuilder()
                        while ((rd.readLine().also { line = it }) != null) {
                            stringBuilder.append(line)
                        }
                        val result = stringBuilder.toString()

                        // Instantiate a JSON object from the request response
                        val jsonObject = JSONObject(result)
                        val horoscopeLuck =
                            jsonObject.getJSONObject("data").getString("horoscope_data")

                        CoroutineScope(Dispatchers.Main).launch {
                            horoscopeLuckTextView.text = horoscopeLuck
                            progressBar.visibility = View.GONE
                        }
                        /*runOnUiThread {

                            }*/
                        //Log.i("HTTP", horoscopeLuck)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    urlConnection?.disconnect()
                }
            }
        }
    }



