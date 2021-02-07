package com.muglzm.opengl

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.muglzm.opengl.samples.ShaderFragment
import com.muglzm.opengl.samples.TextureArray

class MySampleActivity: AppCompatActivity() {
    private val samples =
        arrayOf(
            ShaderFragment(),
            TextureArray()
        //Add more sample functions
        )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        title = intent.getStringExtra(GlobalConstants.KEY_SAMPLE_NAME)
        val sampleIndex = intent.getIntExtra(GlobalConstants.KEY_SAMPLE_INDEX,-1)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content,samples[sampleIndex])
        transaction.commit()

    }

}