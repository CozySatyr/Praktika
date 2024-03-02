package com.example.nikitascherbakov

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.btpit.nmedia.MainActivity2


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val btn1 = findViewById<Button>(R.id.button1)
        btn1.setOnClickListener{
            startActivity(Intent(this,MainActivity2::class.java))
            Toast.makeText(this,"Кнопка нажата ", Toast.LENGTH_LONG).show()
            finish()
        }

    }
}