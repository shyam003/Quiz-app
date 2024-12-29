package com.example.loginfirebase

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class TempHome : AppCompatActivity() {
    lateinit var fDatabase: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp_home)
       fDatabase= FirebaseFirestore.getInstance()
        val arrayList = ArrayList<Category>()
       val adaptor1 = CategoryAdaptor(this,arrayList)
        val rowcat = findViewById<RecyclerView>(R.id.rwcatogories1)
         fDatabase.collection("Categories").addSnapshotListener { value, error ->
           // val v : DocumentSnapshot
             for(v in value?.documents!!)
             {
                 val v1= v.toObject<Category>()
                 v1?.CategoryId = v.id
                 v1?.let { arrayList.add(it) }
                 Toast.makeText(this,v1?.CategoryId.toString(),Toast.LENGTH_SHORT).show()
             }
             adaptor1.notifyDataSetChanged()
         }
        rowcat.layoutManager = GridLayoutManager(this,2)
        rowcat.adapter = adaptor1


    }
}