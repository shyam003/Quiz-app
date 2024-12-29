package com.example.loginfirebase

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pl.droidsonroids.gif.GifImageView

class CategoryAdaptor : RecyclerView.Adapter<CategoryAdaptor.CatogoryViewHolder>
{
    lateinit var builder: AlertDialog.Builder
    lateinit var Alert:AlertDialog
     var context: Context
     var categoryModels: ArrayList<Category>
    constructor(context: Context,categoryModels: ArrayList<Category>)
    {
        this.context=context
        this.categoryModels=categoryModels

    }

    public class CatogoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView = itemView.findViewById<ImageView>(R.id.image)
        var textView = itemView.findViewById<TextView>(R.id.category)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatogoryViewHolder {
       var view= LayoutInflater.from(context).inflate(R.layout.item_category,null)
        return CatogoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatogoryViewHolder, position: Int) {
        var model= categoryModels.get(position)
        holder.textView.text=model.CategoryName
      Glide.with(context)
          .load(model.CategoryImage).into(holder.imageView)
        holder.itemView.setOnClickListener(View.OnClickListener {
            alert(model)

        })
    }

    private fun alert(model: Category) {

          Handler(Looper.getMainLooper()).postDelayed({
              val int: Intent = Intent(context, Quiz::class.java)
              int.putExtra("catId",model.CategoryId)
              context.startActivity(int)
              Handler(Looper.getMainLooper()).postDelayed({
                  dismis()
              },1000)
          },9000)
            builder = AlertDialog.Builder(context,android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen)
            val layout = LayoutInflater.from(context).inflate(R.layout.result_layout, null)
            val v1: GifImageView = layout.findViewById(R.id.gifimg)
            v1.setImageResource(R.drawable.toquiz);

            builder.setView(layout)
        builder.setCancelable(false)
            Alert = builder.create()
            Alert.show()
    }
    private fun dismis() {
        Alert.dismiss()
    }
    override fun getItemCount(): Int {
        return categoryModels.size
    }

}

