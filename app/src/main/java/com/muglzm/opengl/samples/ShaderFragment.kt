package com.muglzm.opengl.samples

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.muglzm.opengl.R
import com.muglzm.opengl.render.ShaderRender

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShaderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShaderFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //获取layout视图
        Log.d("LZM", "onCreateView: create shader fragment")
        val rootview = inflater.inflate(R.layout.fragment_shader,container,false);
        val glSurfaceView = rootview.findViewById<GLSurfaceView>(R.id.glsurfaceview);
        //Set EGL Version 3
        glSurfaceView.setEGLContextClientVersion(3)
        //Set RGBA:8888
        glSurfaceView.setEGLConfigChooser(8,8,8,8,0,0)
        glSurfaceView.setRenderer(ShaderRender())
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        return rootview
    }
}