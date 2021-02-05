package com.muglzm.opengl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muglzm.opengl.util.Util

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Util.context = applicationContext

        val samplesList = findViewById<RecyclerView>(R.id.list)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        samplesList.layoutManager = layoutManager
        samplesList.adapter = MyAdapter()
    }

    inner class MyAdapter:RecyclerView.Adapter<VH>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            TODO("Not yet implemented")
        }

        override fun getItemCount(): Int {
            TODO("Not yet implemented")
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            TODO("Not yet implemented")
        }
    }

    inner class VH(itemView: View):RecyclerView.ViewHolder(itemView){
        var button:Button = itemView.findViewById(R.id.button)
    }
}