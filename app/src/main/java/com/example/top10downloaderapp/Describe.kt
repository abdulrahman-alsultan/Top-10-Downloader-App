package com.example.top10downloaderapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Describe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_describe)

        val describe = findViewById<TextView>(R.id.tv_describe)

        this.title = intent.getStringExtra("name")
        describe.text = intent.getStringExtra("describe")

    }
}