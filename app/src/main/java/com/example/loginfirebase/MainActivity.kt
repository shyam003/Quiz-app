package com.example.loginfirebase

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import pl.droidsonroids.gif.GifImageView
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    private var textInputFullName: TextInputLayout? = null
    private var textInputEmail: TextInputLayout? = null
    private var textInputPhone: TextInputLayout? = null
    private var textInputPassword: TextInputLayout? = null
    private var textInputConfirmPassword: TextInputLayout? = null
    lateinit var Alert:AlertDialog
    lateinit var builder: AlertDialog.Builder
    lateinit var email: EditText
    lateinit var emaillogin: EditText
    lateinit var passwordlogin: EditText
    lateinit var phone: EditText
    lateinit var name: EditText
    lateinit var password: EditText
    lateinit var confirmPassword: EditText
    lateinit var Register: Button
    lateinit var fAuth: FirebaseAuth
    lateinit var fData: FirebaseFirestore
    lateinit var name1 : String
    lateinit var phone1 : String
    lateinit var email1 : String
    lateinit var pass1 : String
    lateinit var registerscreen: ConstraintLayout
    lateinit var loginscreen: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        name=findViewById(R.id.fullname)
        email=findViewById(R.id.email)
        phone=findViewById(R.id.phone)
        password=findViewById(R.id.password)
        confirmPassword =findViewById(R.id.edit_user_confirm_pass)
        Register= findViewById(R.id.Register)
        supportActionBar?.hide()

        fAuth= FirebaseAuth.getInstance()
        fData= FirebaseFirestore.getInstance()
        emaillogin=findViewById(R.id.emaillogin)
        passwordlogin=findViewById(R.id.passwordlogin)
        registerscreen=findViewById(R.id.registerscreen)
        loginscreen=findViewById(R.id.loginscreen)

         textInputFullName = findViewById(R.id.Fullname1)
         textInputEmail= findViewById(R.id.Email)
         textInputPassword= findViewById(R.id.password1)
        textInputConfirmPassword= findViewById(R.id.confirm_pass)
        textInputPhone= findViewById(R.id.phone1)
         val usr1 = fAuth.uid.toString()
        if(!usr1.equals("null") or usr1.isNotEmpty())
        {
            val intent: Intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
            finish()
        }
        else
            Toast.makeText(this,"Please login to continue",Toast.LENGTH_SHORT).show()

     name.addTextChangedListener(object : TextWatcher{
         override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

         }

         override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
             textInputFullName?.error=null
         }

         override fun afterTextChanged(s: Editable?) {

         }

     })
        email.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
              textInputEmail?.error=null
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        phone.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               textInputPhone?.error=null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        password.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               textInputPassword?.error=null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        confirmPassword.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               textInputConfirmPassword?.error=null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
          }
    fun Register(view: android.view.View) {
        val check: CheckBox = findViewById(R.id.checkBox)
        if(check.isChecked) {
            validate_details()
        }
        else
        {
            val build:AlertDialog.Builder = AlertDialog.Builder(this)
            build.setTitle("Important")
            build.setMessage("You should accept terms and conditions inorder to use this app.")
            build.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                check.isChecked = true
                validate_details()
            })
            build.setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
            })
            build.setCancelable(false)
            build.create()
            build.show()
        }

    }
    private fun validate_details() {
       if(!validate_Full_Name() or !validate_Email() or !validate_Password() or !validate_Confirm_Password() or !validate_Phone())
           return
        else
        {
            name1=name.text.toString().trim()
            email1=email.text.toString().trim()
            phone1=phone.text.toString().trim()
            pass1=password.text.toString().trim()
            val score ="0"
            val score1 =0
            val user = User(name1,phone1, email1, pass1, score, score1)
            fAuth.createUserWithEmailAndPassword(email1,pass1).addOnCompleteListener(this) {task->
                if(task.isSuccessful) {
                    Toast.makeText(this, "User Registered ", Toast.LENGTH_SHORT).show()
                    val uname=fAuth.uid
                    fData.collection("User").document(uname.toString()).set(user).addOnCompleteListener { task->
                        if(task.isSuccessful)
                        { Toast.makeText(this,"Created data",Toast.LENGTH_SHORT).show()
                            fAuth.signInWithEmailAndPassword(email1,pass1).addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    alert()
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        val intent: Intent = Intent(this, MainActivity2::class.java)
                                        startActivity(intent)
                                        finish()
                                    },8000)
                                } else
                                    Toast.makeText(
                                        this,
                                        task.exception?.localizedMessage,
                                        Toast.LENGTH_SHORT
                                    ).show()
                            }
                        }
                        else
                            Toast.makeText(this, task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
                    }
                }
                else
                    Toast.makeText(this,task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun alert() {
        builder = AlertDialog.Builder(this,android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen)
        val layout = LayoutInflater.from(this).inflate(R.layout.result_layout, null)
        val v1: GifImageView = layout.findViewById(R.id.gifimg)
        v1.setImageResource(R.drawable.login);
        builder.setCancelable(false)
        builder.setView(layout)
        Alert = builder.create()
        Alert.show()
    }

    fun Login(view: android.view.View) {
        registerscreen.isGone=true
        loginscreen.isVisible=true

    }
    fun RegisterLogin(view: android.view.View) {
        loginscreen.isGone=true
        registerscreen.isVisible=true

    }
    fun LoginLogin(view: android.view.View) {
        val emaillogin1 = emaillogin.text.toString().trim()
        val passwordlogin1 = passwordlogin.text.toString().trim()
        if (emaillogin1.isEmpty() || passwordlogin1.isEmpty()) {
          val t=   Toast.makeText(this, "Email or password cannot be null", Toast.LENGTH_SHORT)
        t.show()
        } else
        {  fAuth.signInWithEmailAndPassword(emaillogin1, passwordlogin1)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        alert()
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent: Intent = Intent(this, MainActivity2::class.java)
                            startActivity(intent)
                            finish()
                        },8000)

                    } else
                        Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_SHORT)
                            .show()

                }
    }

    }
    private fun validate_Full_Name(): Boolean {
        val Fullname: String =
            name.text.toString().trim { it <= ' ' }
        return if (Fullname.isEmpty()) {
            textInputFullName?.error = getString(R.string.First_name_err)
            false
        } else {
            textInputFullName?.error = null
            true
        }
    }
    private fun validate_Email(): Boolean {
        val email: String = email.text.toString().trim { it <= ' ' }
        val emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+", Pattern.CASE_INSENSITIVE)
        val m3 = emailPattern.matcher(email)
        val check = m3.find()
        return if (!email.isEmpty() && check) {
            textInputEmail?.error = null
            true
        } else if (email.isEmpty()) {
            textInputEmail?.error = getString(R.string.Email_err)
            false
        } else {
            textInputEmail?.error = getString(R.string.Email_Validation_err)
            false
        }
    }
    private fun validate_Password(): Boolean {
        val password: String =
            password.text.toString().trim { it <= ' ' }
        val p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE)
        val p1 = Pattern.compile("[a-z ]", Pattern.UNICODE_CASE)
        val p3 = Pattern.compile("[A-Z ]", Pattern.UNICODE_CASE)
        val p2 = Pattern.compile("[0-9 ]", Pattern.CASE_INSENSITIVE)
        val m = p.matcher(password)
        val m1 = p1.matcher(password)
        val m2 = p2.matcher(password)
        val m3 = p3.matcher(password)
        val check = m.find()
        val check1 = m1.find()
        val check2 = m2.find()
        val check3 = m3.find()
        return if (password.length < 8) {
            textInputPassword?.error = getString(R.string.Password_8char_Less_err)
            false
        } else if (!(check && check1 && check2 && check3)) {
            textInputPassword?.error = getString(R.string.Password_err)
            false
        } else {
            textInputPassword?.error = null
            true
        }
    }
    private fun validate_Confirm_Password(): Boolean {
        val confirm_password =
            confirmPassword.text.toString().trim { it <= ' ' }
        val password = password.text.toString().trim { it <= ' ' }
        return if (confirm_password != password) {
            textInputConfirmPassword?.error = getString(R.string.Confirm_Password_err)
            false
        } else {
            textInputConfirmPassword?.error = null
            true
        }
    }
    private fun validate_Phone(): Boolean {
        val phoneno =
            phone.text.toString().trim()
        return if(phoneno.length==10)
            true
        else {
            textInputPhone?.error="Phone number should be 10 digits"
            false
        }
    }
    override fun onBackPressed() {

    }
}