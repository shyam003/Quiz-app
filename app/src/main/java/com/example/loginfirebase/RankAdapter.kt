package com.example.loginfirebase

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RankAdapter: RecyclerView.Adapter<RankAdapter.RankViewholder> {
    lateinit var context:Context
    lateinit var users:ArrayList<User>
    constructor(context: Context, users: ArrayList<User>){
        this.context=context
        this.users=users
    }

    class RankViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rank = itemView.findViewById<TextView>(R.id.rank1)

        var name = itemView.findViewById<TextView>(R.id.name)
        var score = itemView.findViewById<TextView>(R.id.score)
        var card = itemView.findViewById<CardView>(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankViewholder {
        val v =LayoutInflater.from(context).inflate(R.layout.rank,null)
        return RankViewholder(v)
    }

    override fun onBindViewHolder(holder: RankViewholder, position: Int) {
       if(position>2){
           var user = users.get(position)
            holder.name.text = user.name
        holder.rank.text = String.format("#%d", position + 1)
        holder.score.text = "Score: " + user.score
        FirebaseFirestore.getInstance().collection("User")
            .document(FirebaseAuth.getInstance().uid.toString())
            .addSnapshotListener { value, error ->

                if (user.name.equals(value?.getString("name").toString())) {
                    holder.card.setBackgroundColor(Color.GREEN)
                    holder.name.text = user.name +"(You)"
                }
            }
    }
        else
       {
           holder.card.isGone=true
       }
    }

    override fun getItemCount(): Int {
        return  users.size
    }
}