package com.example.loginfirebase.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.loginfirebase.MainActivity
import com.example.loginfirebase.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileFragment : Fragment() {
    lateinit var fdatabase: FirebaseFirestore
    lateinit var FAuth: FirebaseAuth
    lateinit var Email:TextView
    lateinit var phone:TextView
    lateinit var score:TextView
    lateinit var name:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_profile, container, false)
   name = v.findViewById(R.id.name)
   phone = v.findViewById(R.id.phone)
   Email = v.findViewById(R.id.email)
   score = v.findViewById(R.id.score)
        fdatabase= FirebaseFirestore.getInstance()
        FAuth= FirebaseAuth.getInstance()

        val logout = v.findViewById<Button>(R.id.logout)


       fdatabase.collection("User").document(FAuth.uid.toString())
            .addSnapshotListener { value, error ->
                name.text= value?.getString("name").toString()
                Email.text= value?.getString("email").toString()
                phone.text= value?.getString("phone").toString()
                score.text = value?.getString("score").toString()

            }
        logout.setOnClickListener(View.OnClickListener {
            val v = LayoutInflater.from(context).inflate(R.layout.actionbar,null)

            val  update:Button = v.findViewById(R.id.logout)
            val  cancel:Button = v.findViewById(R.id.cancel)
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Alert")
            builder.setView(v)
            update.setOnClickListener{
                    FAuth.signOut()
                    val intent: Intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
            }
            val Alert = builder.create()
            Alert.show()
            cancel.setOnClickListener{
                dismis(Alert)
            }



        })


        val resetPass:TextView=v.findViewById(R.id.password)


        resetPass.setOnClickListener{
            resetPassword()
        }
        return v
    }

    private fun resetPassword() {
        val v = LayoutInflater.from(context).inflate(R.layout.update_profile,null)
        val pass:EditText=v.findViewById(R.id.updateName)
        val confpass:EditText=v.findViewById(R.id.updateNumber)
        val  update:Button = v.findViewById(R.id.update)
        val  cancel:Button = v.findViewById(R.id.cancel)
        val builder = AlertDialog.Builder(context)
        builder.setView(v)
        val Alert = builder.create()
        Alert.show()
        update.setOnClickListener{
            if((pass.text.toString() == confpass.text.toString())&&!pass.text.toString().isEmpty())
            {
                val user1  = FAuth.currentUser
                user1?.updatePassword(pass.text.toString())?.addOnSuccessListener{
                    Toast.makeText(context,"Your Password is reset successfully", Toast.LENGTH_SHORT).show()
                    dismis(Alert)
                }?.addOnFailureListener{
                    Toast.makeText(context,it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
            else
                Toast.makeText(context,"Password and confirm password should be same and not empty", Toast.LENGTH_SHORT).show()
        }

        cancel.setOnClickListener{
            dismis(Alert)
        }

    }

    private fun dismis(builder: AlertDialog) {
        builder.dismiss()
    }

}