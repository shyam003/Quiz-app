package com.example.loginfirebase

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class Quiz : AppCompatActivity() {
    var index =0
    var showAnswer=false
    var currentScore=0
    lateinit var fdatabase:FirebaseFirestore
    lateinit var fAuth: FirebaseAuth
lateinit var Alert:AlertDialog
    lateinit var questionsF : ArrayList<Question>
    lateinit var builder: AlertDialog.Builder
    lateinit var submission : ArrayList<String>
    lateinit var questiontext: TextView
    lateinit var questionno: TextView
    lateinit var quit: ImageView
    lateinit var Radiogroup: RadioGroup
    lateinit var RBoption1: RadioButton
    lateinit var RBoption2: RadioButton
    lateinit var RBoption3: RadioButton
    lateinit var RBoption4: RadioButton
    lateinit var next: Button
    lateinit var previous: Button
    lateinit var submit: Button
    lateinit var QScreen: ScrollView
    lateinit var NoData: RelativeLayout
    lateinit var btnNext:Button
    lateinit var btnsubmit:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
 //       supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#1b4b62")))
           supportActionBar?.hide()
        next= findViewById(R.id.btnnext)
        previous = findViewById(R.id.btnprev)
        submit = findViewById(R.id.btnSubmit)
        questiontext= findViewById(R.id.QuestionText)
        questionno= findViewById(R.id.question_no)
        Radiogroup = findViewById(R.id.optionGroup)
        RBoption1 = findViewById(R.id.Option1)
        RBoption2 = findViewById(R.id.Option2)
        RBoption3 = findViewById(R.id.Option3)
        RBoption4 = findViewById(R.id.Option4)
        questionsF = ArrayList()
        submission = ArrayList()
        fdatabase= FirebaseFirestore.getInstance()
        title = "Quiz"
        fAuth= FirebaseAuth.getInstance()
        getDataFromFirebase(intent.getStringExtra("catId").toString())
        btnNext=findViewById(R.id.btnnext)
        btnsubmit=findViewById(R.id.btnSubmit)
        quit=findViewById(R.id.quit)
        QScreen = findViewById(R.id.question_screen)
        NoData = findViewById(R.id.no_questions)

        quit.setOnClickListener{

            if(!showAnswer) {
                val build: AlertDialog.Builder = AlertDialog.Builder(this)
                build.setTitle("If you quit now Progress will not be saved.")
                build.setMessage("Do You really want to quit?.")
                build.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    val int: Intent = Intent(this, MainActivity2::class.java)
                    this.startActivity(int)
                    finish()

                })
                build.setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                })
                build.setCancelable(false)
                build.create()
                build.show()
            }
            else
            {
                val int: Intent = Intent(this, MainActivity2::class.java)
                this.startActivity(int)
                    finish()
            }

        }

    }

    private fun getDataFromFirebase(catid: String) {
        fdatabase.collection("Categories").document(catid).
        collection("Questions")
            .addSnapshotListener { value, error ->
                if(error!=null)
                {
                    Toast.makeText(this,error.message,Toast.LENGTH_SHORT).show()
                    //return@addSnapshotListener
                }

                for(data in value?.documentChanges!!)
                {

                    questionsF.add(data.document.toObject<Question>())


                }
                if (questionsF.size==0)
                {
                    QScreen.isGone=true
                    NoData.isVisible=true
                }
                else if(questionsF.size==1)
                {
                    QScreen.isVisible=true
                    NoData.isGone=true
                    btnNext.isGone=true
                    btnsubmit.isVisible=true

                    questiontext.text = questionsF[index].questionF
                    RBoption1.text = questionsF[index].option1
                    RBoption2.text = questionsF[index].option2
                    RBoption3.text = questionsF[index].option3
                    RBoption4.text = questionsF[index].option4

                    val noq=questionsF.size
                    questionno.text="Question: 1/$noq"
                }
                else
                {
                    QScreen.isVisible=true
                    NoData.isGone=true
                    questiontext.text = questionsF[index].questionF
                    RBoption1.text = questionsF[index].option1
                    RBoption2.text = questionsF[index].option2
                    RBoption3.text = questionsF[index].option3
                    RBoption4.text = questionsF[index].option4

                    val noq=questionsF.size
                    questionno.text="Question: 1/$noq"
                }

            }
    }

    override fun onBackPressed() {
    }



    fun button(view: View) {
        val button = view as Button
        when(button.id) {
            next.id -> {


                submition()
                index++
                questiontext.text = questionsF[index].questionF
                RBoption1.text = questionsF[index].option1
                RBoption2.text = questionsF[index].option2
                RBoption3.text = questionsF[index].option3
                RBoption4.text = questionsF[index].option4
                val qno = index+1
                 val noq=questionsF.size
                questionno.text="Question: $qno/$noq"
                selection()

            }
            previous.id -> {

                submition()
                index--
                questiontext.text = questionsF[index].questionF
                RBoption1.text = questionsF[index].option1
                RBoption2.text = questionsF[index].option2
                RBoption3.text = questionsF[index].option3
                RBoption4.text = questionsF[index].option4
                val qno = index+1
                val noq=questionsF.size
                questionno.text="Question: $qno/$noq"
                selection()

            }
            submit.id -> {

                if (!showAnswer){
                    submition()
                var i = 0
                var result = 0
                while (i < questionsF.size) {
                    if (submission[i] == questionsF[i].answer)
                        result++
                    i++
                }

                    val uid= fAuth.uid.toString()

                    fdatabase.collection("User").document(uid)
                        .addSnapshotListener { value, error ->
                            val c = value?.get("score").toString()
                            currentScore= c.toInt()
                            updateScore(currentScore,result)
                            result=0
                        }
                    val scr = "$result/$i"
                    Handler(Looper.getMainLooper()).postDelayed({
                        val int: Intent = Intent(this, ResultScreen::class.java)
                        int.putExtra("score",scr)
                        getResult.launch(int)
                       // this.startActivity(int)
                        Handler(Looper.getMainLooper()).postDelayed({
                            dismis()
                        },2000)

                    },7000)
                     alert()
                    RBoption1.isClickable = false
                    RBoption2.isClickable = false
                    RBoption3.isClickable = false
                    RBoption4.isClickable = false
                    selection()
                    if (showAnswer) {
                        RBoption1.setTextColor(Color.parseColor("#00599F"))
                        RBoption2.setTextColor(Color.parseColor("#00599F"))
                        RBoption3.setTextColor(Color.parseColor("#00599F"))
                        RBoption4.setTextColor(Color.parseColor("#00599F"))
                        RBoption1.setBackgroundResource(R.drawable.radio_normal);
                        RBoption2.setBackgroundResource(R.drawable.radio_normal);
                        RBoption3.setBackgroundResource(R.drawable.radio_normal);
                        RBoption4.setBackgroundResource(R.drawable.radio_normal);
                        when (questionsF[index].answer) {
                            RBoption1.text.toString() ->{
                                RBoption1.setTextColor(Color.WHITE)
                                RBoption1.setBackgroundResource(R.drawable.correct_answer);
                            }
                            RBoption2.text.toString() ->{ RBoption2.setTextColor(Color.WHITE)
                            RBoption2.setBackgroundResource(R.drawable.correct_answer);}
                            RBoption3.text.toString() ->{ RBoption3.setTextColor(Color.WHITE)
                            RBoption3.setBackgroundResource(R.drawable.correct_answer);}
                            RBoption4.text.toString() ->{ RBoption4.setTextColor(Color.WHITE)
                            RBoption4.setBackgroundResource(R.drawable.correct_answer);}
                        }
                        when (submission[index]) {
                            RBoption1.text.toString() -> {
                                if (submission[index] != questionsF[index].answer) {
                                    RBoption1.setTextColor(Color.WHITE)
                                    RBoption1.setBackgroundResource(R.drawable.wrong_answer)
                                }
                            }
                            RBoption2.text.toString() -> {
                                if (submission[index] != questionsF[index].answer) {
                                    RBoption2.setTextColor(Color.WHITE)
                                    RBoption2.setBackgroundResource(R.drawable.wrong_answer)
                                }
                            }
                            RBoption3.text.toString() -> {
                                if (submission[index] != questionsF[index].answer) {
                                    RBoption3.setTextColor(Color.WHITE)
                                    RBoption3.setBackgroundResource(R.drawable.wrong_answer)
                                }
                            }
                            RBoption4.text.toString() -> {
                                if (submission[index] != questionsF[index].answer) {

                                    RBoption4.setTextColor(Color.WHITE)
                                    RBoption4.setBackgroundResource(R.drawable.wrong_answer)
                                }
                            }

                        }
                    }

            }
                else
                {
                    finish()
                }
        }
        }

        next.isGone=index+1>=questionsF.size
        submit.isGone= index+1 < questionsF.size
        previous.isVisible = index >= 1

    }

    private fun alert() {
        builder = AlertDialog.Builder(this,android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen)
        val layout = LayoutInflater.from(this).inflate(R.layout.result_layout, null)
        builder.setView(layout)
        Alert = builder.create()
        Alert.show()
    }

    private fun updateScore(currentScore: Int, result: Int) {

        fdatabase.collection("User").document(fAuth.uid.toString())
            .update("score",(result+currentScore).toString())
        fdatabase.collection("User").document(fAuth.uid.toString())
            .update("scr",(result+currentScore))

    }

    private fun dismis() {
        Alert.dismiss()
    }

    private fun selection() {
        if(submission.size>index)
        {
            when(submission[index])
            {
                RBoption1.text.toString()->RBoption1.isChecked=true
                RBoption2.text.toString()->RBoption2.isChecked=true
                RBoption3.text.toString()->RBoption3.isChecked=true
                RBoption4.text.toString()->RBoption4.isChecked=true
                ""-> {
                    RBoption1.isChecked = false
                    RBoption2.isChecked = false
                    RBoption3.isChecked = false
                    RBoption4.isChecked = false

                }
            }


        }
        else
        {
            Radiogroup.clearCheck()
        }

        if(showAnswer)
        {
            RBoption1.setTextColor(Color.parseColor("#00599F"))
            RBoption2.setTextColor(Color.parseColor("#00599F"))
            RBoption3.setTextColor(Color.parseColor("#00599F"))
            RBoption4.setTextColor(Color.parseColor("#00599F"))
            RBoption1.setBackgroundResource(R.drawable.radio_normal);
            RBoption2.setBackgroundResource(R.drawable.radio_normal);
            RBoption3.setBackgroundResource(R.drawable.radio_normal);
            RBoption4.setBackgroundResource(R.drawable.radio_normal);
            when (questionsF[index].answer) {
                RBoption1.text.toString() ->{
                    RBoption1.setTextColor(Color.WHITE)
                    RBoption1.setBackgroundResource(R.drawable.correct_answer);
                }
                RBoption2.text.toString() ->{ RBoption2.setTextColor(Color.WHITE)
                    RBoption2.setBackgroundResource(R.drawable.correct_answer);}
                RBoption3.text.toString() ->{ RBoption3.setTextColor(Color.WHITE)
                    RBoption3.setBackgroundResource(R.drawable.correct_answer);}
                RBoption4.text.toString() ->{ RBoption4.setTextColor(Color.WHITE)
                    RBoption4.setBackgroundResource(R.drawable.correct_answer);}
            }
            when (submission[index]) {
                RBoption1.text.toString() -> {
                    if (submission[index] != questionsF[index].answer) {
                        RBoption1.setTextColor(Color.WHITE)
                        RBoption1.setBackgroundResource(R.drawable.wrong_answer)
                    }
                }
                RBoption2.text.toString() -> {
                    if (submission[index] != questionsF[index].answer) {
                        RBoption2.setTextColor(Color.WHITE)
                        RBoption2.setBackgroundResource(R.drawable.wrong_answer)
                    }
                }
                RBoption3.text.toString() -> {
                    if (submission[index] != questionsF[index].answer) {
                        RBoption3.setTextColor(Color.WHITE)
                        RBoption3.setBackgroundResource(R.drawable.wrong_answer)
                    }
                }
                RBoption4.text.toString() -> {
                    if (submission[index] != questionsF[index].answer) {

                        RBoption4.setTextColor(Color.WHITE)
                        RBoption4.setBackgroundResource(R.drawable.wrong_answer)
                    }
                }

            }

        }
    }
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val value = it.data?.getStringExtra("input")
            }
            showAnswer=true
            RBoption1.setTextColor(Color.parseColor("#00599F"))
            RBoption2.setTextColor(Color.parseColor("#00599F"))
            RBoption3.setTextColor(Color.parseColor("#00599F"))
            RBoption4.setTextColor(Color.parseColor("#00599F"))
            RBoption1.setBackgroundResource(R.drawable.radio_normal);
            RBoption2.setBackgroundResource(R.drawable.radio_normal);
            RBoption3.setBackgroundResource(R.drawable.radio_normal);
            RBoption4.setBackgroundResource(R.drawable.radio_normal);
            when (questionsF[index].answer) {
                RBoption1.text.toString() ->{
                    RBoption1.setTextColor(Color.WHITE)
                    RBoption1.setBackgroundResource(R.drawable.correct_answer);
                }
                RBoption2.text.toString() ->{ RBoption2.setTextColor(Color.WHITE)
                    RBoption2.setBackgroundResource(R.drawable.correct_answer);}
                RBoption3.text.toString() ->{ RBoption3.setTextColor(Color.WHITE)
                    RBoption3.setBackgroundResource(R.drawable.correct_answer);}
                RBoption4.text.toString() ->{ RBoption4.setTextColor(Color.WHITE)
                    RBoption4.setBackgroundResource(R.drawable.correct_answer);}
            }
            when (submission[index]) {
                RBoption1.text.toString() -> {
                    if (submission[index] != questionsF[index].answer) {
                        RBoption1.setTextColor(Color.WHITE)
                        RBoption1.setBackgroundResource(R.drawable.wrong_answer)
                    }
                }
                RBoption2.text.toString() -> {
                    if (submission[index] != questionsF[index].answer) {
                        RBoption2.setTextColor(Color.WHITE)
                        RBoption2.setBackgroundResource(R.drawable.wrong_answer)
                    }
                }
                RBoption3.text.toString() -> {
                    if (submission[index] != questionsF[index].answer) {
                        RBoption3.setTextColor(Color.WHITE)
                        RBoption3.setBackgroundResource(R.drawable.wrong_answer)
                    }
                }
                RBoption4.text.toString() -> {
                    if (submission[index] != questionsF[index].answer) {

                        RBoption4.setTextColor(Color.WHITE)
                        RBoption4.setBackgroundResource(R.drawable.wrong_answer)
                    }
                }

            }

        }
    private fun submition() {
        when (Radiogroup.checkedRadioButtonId) {
            RBoption1.id ->{
                if(submission.size>index)
                    submission[index] = RBoption1.text.toString()
                else
                    submission.add(RBoption1.text.toString())

            }
            RBoption2.id ->{
                if(submission.size>index)
                    submission[index] = RBoption2.text.toString()
                else
                    submission.add(RBoption2.text.toString())
            }
            RBoption3.id ->{
                if(submission.size>index)
                    submission[index] = RBoption3.text.toString()
                else
                    submission.add(RBoption3.text.toString())
            }
            RBoption4.id -> {
                if(submission.size>index)
                    submission[index] = RBoption4.text.toString()
                else
                    submission.add(RBoption4.text.toString())
            }
            else ->{
                if(submission.size>index)
                    submission[index] = ""
                else
                    submission.add("")
            }
        }
    }

    fun home(view: android.view.View) {
        val int: Intent = Intent(this, MainActivity2::class.java)
        this.startActivity(int)
        finish()
    }
}