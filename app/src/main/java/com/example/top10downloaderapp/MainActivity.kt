package com.example.top10downloaderapp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var fab: ExtendedFloatingActionButton
    private lateinit var rvMain: RecyclerView
    private lateinit var adapter: TopTenRecyclerViewAdapter
    private lateinit var topTenList: MutableList<AppData>
    private lateinit var progress: ProgressBar
    private lateinit var title: TextView
    private lateinit var update: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab = findViewById(R.id.floatingActionButton)
        rvMain = findViewById(R.id.rvMain)
        topTenList = mutableListOf()
        progress = findViewById(R.id.progress)
        title = findViewById(R.id.title)
        update = findViewById(R.id.update)


        fab.setOnClickListener{
            rvMain.visibility = View.GONE
            fab.visibility = View.GONE
            progress.visibility = View.VISIBLE
            CoroutineScope(IO).launch {
                FetchData().execute()
            }
        }
    }

    inner class FetchData: AsyncTask<Void, Void, MutableList<AppData>>() {
        private val parser = XMLParser()
        override fun doInBackground(vararg p0: Void?): MutableList<AppData> {
            return try {
                val connection = URL("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml").openConnection() as HttpURLConnection
                topTenList = connection.inputStream?.let {
                    parser.parse(it)
                } as MutableList<AppData>
                topTenList
            }catch (e: Exception){
                Log.d("pqpqpq", e.toString())
                mutableListOf()
            }
        }

        override fun onPostExecute(result: MutableList<AppData>?) {
            super.onPostExecute(result)
            rvMain.visibility = View.VISIBLE
            fab.visibility = View.VISIBLE
            progress.visibility = View.GONE
            if (topTenList.size > 0){
                title.text = topTenList[0].title
                update.text = topTenList[0].releaseDate
                topTenList.removeFirst()
            }
            adapter = TopTenRecyclerViewAdapter(topTenList)
            rvMain.adapter = adapter
            rvMain.layoutManager = LinearLayoutManager(this@MainActivity)
        }

    }

}
