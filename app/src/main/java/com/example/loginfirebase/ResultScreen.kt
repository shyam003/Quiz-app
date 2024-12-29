package com.example.loginfirebase

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ResultScreen : AppCompatActivity() {
    lateinit var percent: TextView
    lateinit var quote: TextView
    lateinit var home: Button
    lateinit var showans: Button
    lateinit var progress:ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_result_screen)
        percent= findViewById(R.id.percentage)
        quote=findViewById(R.id.quote)
        home=findViewById(R.id.home)
        showans=findViewById(R.id.showanswer)
        progress=findViewById(R.id.progressBar)
        val score= (intent.getStringExtra("score").toString()).split("/")


        val result= score[0].toDouble()
        val OutOf= score[1].toDouble()
  val correct = findViewById<TextView>(R.id.textView)
  val wrong = findViewById<TextView>(R.id.textView2)
        correct.text="Correct answers: "+score[0]
          wrong.text="Out of: "+score[1]
        Toast.makeText(this,"result = $result out of $OutOf",Toast.LENGTH_SHORT).show()
        val scr= (result/OutOf*100).toInt()
        progress.progress =scr
        percent.text="$scr %"

        if (scr==100) {
            quote.text="You are a genius. \n Well Done!!"
        }
        else if (scr<40) {
            quote.text="Failure is the first step to success.\nAll the best!"
        }
        else if (scr<60) {
            quote.text="Not bad!\nYou can do it better."
        }
        else if (scr<80) {
            quote.text="Good \nYou are half way to genius."
        }
        else if (scr<90) {
            quote.text="Excellent! \n You are chosen one to become genius."
        }
        else {
            quote.text="Awesome!\nYou are almost genius"
        }
   home.setOnClickListener(View.OnClickListener {
       val int: Intent = Intent(this, MainActivity2::class.java)
       this.startActivity(int)
       finish()

   })

        showans.setOnClickListener(View.OnClickListener {
            finish()
        })

    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Navigate to next page")
        builder.setMessage("clic")
        val Alert: AlertDialog = builder.create()
          Alert.show()
        super.onBackPressed()
    }
}