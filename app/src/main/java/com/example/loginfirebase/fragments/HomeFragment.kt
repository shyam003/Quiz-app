package com.example.loginfirebase.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginfirebase.Category
import com.example.loginfirebase.CategoryAdaptor
import com.example.loginfirebase.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject


class HomeFragment : Fragment() {

lateinit var fDatabase: FirebaseFirestore
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

        val v= inflater.inflate(R.layout.fragment_home, container, false)
        initrecycle(v)
        return  v
    }

    private fun initrecycle(v: View) {
        fDatabase= FirebaseFirestore.getInstance()
        val arrayList = ArrayList<Category>()
        val adaptor = context?.let { CategoryAdaptor(it,arrayList) }
        val rowcat1 = v.findViewById<RecyclerView>(R.id.rwcatogories)
        rowcat1?.adapter = adaptor
        rowcat1?.layoutManager = GridLayoutManager(context,1)

        fDatabase.collection("Categories").addSnapshotListener { value, error ->
            // val v : DocumentSnapshot
            for(v in value?.documents!!)
            {
                val v1= v.toObject<Category>()
                v1?.CategoryId = v.id
                v1?.let { arrayList.add(it) }
              //  Toast.makeText(context,v1?.CategoryId.toString(), Toast.LENGTH_SHORT).show()
            }
            adaptor?.notifyDataSetChanged()
        }


    }


}