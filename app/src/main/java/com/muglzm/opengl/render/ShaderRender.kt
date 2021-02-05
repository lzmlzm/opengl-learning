package com.muglzm.opengl.render

import android.opengl.GLES30
import android.opengl.GLES32
import android.opengl.GLSurfaceView
import com.muglzm.opengl.util.Util
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class ShaderRender:GLSurfaceView.Renderer {

    private val vertexShaderCode =
        "#version 300 es\n" +
                "precision mediump float;\n" +
                "layout(location = 0) in vec4 a_position;\n" +
                "layout(location = 1) in vec2 a_textureCoordinate;\n" +
                "out vec2 v_textureCoordinate;\n" +
                "void main() {\n" +
                "    v_textureCoordinate = a_textureCoordinate;\n" +
                "    gl_Position = a_position;\n" +
                "}"
    private val fragmentShaderCode =
        "#version 300 es\n" +
                "precision mediump float;\n" +
                "precision mediump sampler2D;\n" +
                "layout(location = 0) out vec4 fragColor;\n" +
                "layout(location = 0) uniform sampler2D u_texture;\n" +
                "in vec2 v_textureCoordinate;\n" +
                "void main() {\n" +
                "    fragColor = texture(u_texture, v_textureCoordinate);\n" +
                "}"

    private var glSurfaceViewWidth = 0
    private var glSurfaceViewHeight = 0


    //Vertex data of triangle
    private val vertexData = floatArrayOf(-1f,-1f,
                                            -1f,1f,
                                            1f,1f

                                            -1f,-1f,
                                            1f,1f,
                                            1f,-1f)

    private val VERTEX_COMPONENT_COUNT = 2;
    private lateinit var vertexDataBuffer : FloatBuffer


    // The texture coordinate
    private val textureCoordinateData = floatArrayOf(0f, 1f,
                                                        0f, 0f,
                                                        1f, 0f,

                                                        0f, 1f,
                                                        1f, 0f,
                                                        1f, 1f)
    private val TEXTURE_COORDINATE_COMPONENT_COUNT = 2
    private lateinit var textureCoordinateDataBuffer : FloatBuffer


    // 要渲染的图片纹理
    private var imageTexture = 0


    // a_position、a_textureCoordinate和u_texture的layout位置，与shader中写的对应
    private val LOCATION_ATTRIBUTE_POSITION = 0
    private val LOCATION_ATTRIBUTE_TEXTURE_COORDINATE = 1
    private val LOCATION_UNIFORM_TEXTURE = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

        //Create GL program
        val programId = GLES32.glCreateProgram()

        //Load,compile vertex && fragment shader
        val vertexShader = GLES32.glCreateShader(GLES32.GL_VERTEX_SHADER)
        val fragmentShader = GLES32.glCreateShader(GLES32.GL_FRAGMENT_SHADER)

        GLES32.glShaderSource(vertexShader,vertexShaderCode)
        GLES32.glShaderSource(fragmentShader,fragmentShaderCode)

        //Attach ths shader to program
        GLES32.glAttachShader(programId,vertexShader)
        GLES32.glAttachShader(programId,fragmentShader)

        //Link program
        GLES32.glLinkProgram(programId)

        //Apply GL program
        GLES32.glUseProgram(programId)

        //Put triangle vertex data into the vertexDataBuffer
        vertexDataBuffer = ByteBuffer.allocateDirect(vertexData.size * java.lang.Float.SIZE / 8)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        vertexDataBuffer.put(vertexData)
        vertexDataBuffer.position(0)

        // 启动对应位置的参数，这里直接使用LOCATION_ATTRIBUTE_POSITION，而无需像OpenGL 2.0那样需要先获取参数的location
        GLES32.glEnableVertexAttribArray(LOCATION_ATTRIBUTE_POSITION)

        // 指定a_position所使用的顶点数据
        // Specify the data of a_position
        GLES32.glVertexAttribPointer(LOCATION_ATTRIBUTE_POSITION, VERTEX_COMPONENT_COUNT, GLES30.GL_FLOAT, false,0, vertexDataBuffer)

        //Put texture data into the vertexDataBuffer
        textureCoordinateDataBuffer = ByteBuffer.allocateDirect(textureCoordinateData.size * java.lang.Float.SIZE / 8)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        textureCoordinateDataBuffer.put(textureCoordinateData)
        textureCoordinateDataBuffer.position(0)

        // 启动对应位置的参数，这里直接使用LOCATION_ATTRIBUTE_TEXTURE_COORDINATE，而无需像OpenGL 2.0那样需要先获取参数的location
        GLES30.glEnableVertexAttribArray(LOCATION_ATTRIBUTE_TEXTURE_COORDINATE)

        // 指定a_textureCoordinate所使用的顶点数据
        GLES30.glVertexAttribPointer(LOCATION_ATTRIBUTE_TEXTURE_COORDINATE, TEXTURE_COORDINATE_COMPONENT_COUNT, GLES30.GL_FLOAT, false,0, textureCoordinateDataBuffer)

        //创建图片纹理
        val textures = IntArray(1)
        GLES30.glGenTextures(textures.size, textures, 0)
        imageTexture = textures[0]

        // 将图片解码并加载到纹理中
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        val bitmap = Util.decodeBitmapFromAssets("test.png")
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, imageTexture)
        val b = ByteBuffer.allocate(bitmap.width * bitmap.height * 4)
        bitmap.copyPixelsToBuffer(b)
        b.position(0)

        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE)

        GLES30.glTexImage2D(
            GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGBA, bitmap.width,
            bitmap.height, 0, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, b)
        bitmap.recycle()

        // 启动对应位置的参数，这里直接使用LOCATION_UNIFORM_TEXTURE，而无需像OpenGL 2.0那样需要先获取参数的location
        // Enable the parameter of the location. Here we can simply use LOCATION_UNIFORM_TEXTURE, while in OpenGL 2.0 we have to query the location of the parameter
        GLES30.glUniform1i(LOCATION_UNIFORM_TEXTURE, 0)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glSurfaceViewHeight = height
        glSurfaceViewWidth = width
    }

    override fun onDrawFrame(gl: GL10?) {
        //Clear
        GLES32.glClearColor(0.9f,0.9f,0.9f,1f)
        GLES32.glClear(GLES30.GL_COLOR_BUFFER_BIT)

        //Set view area
        GLES30.glViewport(0, 0, glSurfaceViewWidth, glSurfaceViewHeight)

        //Set status before rendering

    }
}