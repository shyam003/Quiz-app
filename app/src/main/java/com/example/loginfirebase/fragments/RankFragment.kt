package com.example.loginfirebase.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginfirebase.R
import com.example.loginfirebase.RankAdapter
import com.example.loginfirebase.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

class RankFragment public constructor() : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_rank, container, false)
        var rank1name = v.findViewById<TextView>(R.id.rank1name)
        var rank1score = v.findViewById<TextView>(R.id.rank1score)
        var rank2Name = v.findViewById<TextView>(R.id.rank2name)
        var rank2score = v.findViewById<TextView>(R.id.rank2score)
        var rank3name = v.findViewById<TextView>(R.id.rank3name)
        var rank3score = v.findViewById<TextView>(R.id.rank3score)
    val Arraylist: ArrayList<User> = ArrayList()
        val adaptor = context?.let { RankAdapter(it,Arraylist) }
        val recyclerView: RecyclerView = v.findViewById(R.id.recycle)
        val Fdatabase = FirebaseFirestore.getInstance()
        recyclerView.adapter=adaptor
        recyclerView.layoutManager= GridLayoutManager(context,1)
        Fdatabase.collection("User")
            .orderBy("scr",Query.Direction.DESCENDING).get().addOnSuccessListener {
                for (v in it?.documents!!) {
                    val usr = v.toObject<User>()
                    if (usr != null) {
                        Arraylist.add(usr)
                        if(Arraylist.size>1)
                        {
                            rank1name.text=Arraylist[0].name
                            rank1score.text="Score: "+Arraylist[0].score


                            FirebaseFirestore.getInstance().collection("User")
                                .document(FirebaseAuth.getInstance().uid.toString())
                                .addSnapshotListener { value, error ->

                                    if (Arraylist[0].email.equals(value?.getString("email").toString())) {
                                       rank1name.text = Arraylist[0].name +"(You)"
                                    }
                                }



                            if(Arraylist.size>2) {
                                rank2Name.text = Arraylist[1].name
                                rank2score.text = "Score: "+Arraylist[1].score

                                FirebaseFirestore.getInstance().collection("User")
                                    .document(FirebaseAuth.getInstance().uid.toString())
                                    .addSnapshotListener { value, error ->

                                        if (Arraylist[1].email.equals(value?.getString("email").toString())) {
                                            rank2Name.text = Arraylist[1].name +"(You)"
                                        }
                                    }

                                if(Arraylist.size>3) {
                                    rank3name.text = Arraylist[2].name
                                    rank3score.text = "Score: "+Arraylist[2].score
                                    FirebaseFirestore.getInstance().collection("User")
                                        .document(FirebaseAuth.getInstance().uid.toString())
                                        .addSnapshotListener { value, error ->

                                            if (Arraylist[2].email.equals(value?.getString("email").toString())) {
                                                rank3name.text = Arraylist[2].name +"(You)"
                                            }
                                        }

                                }

                            }


                        }

                    }
                }
                adaptor?.notifyDataSetChanged()
            }



        // Inflate the layout for this fragment
        return v
    }


}