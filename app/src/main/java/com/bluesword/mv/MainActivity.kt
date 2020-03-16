package com.bluesword.mv

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bluesword.mv.utils.FileUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {
    val permissionList = ArrayList<String>()
    var PATH_NAME: File? = null
    /**
     * 是否正在录音
     */
    var isRecording = true

    val recorder = MediaRecorder()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestAllPermission()

        setContentView(R.layout.activity_main)

        PATH_NAME = File(FileUtils.getCacheDirectory(this, null), "haha.mp3")


        /***
         *
         *

        Intent intent=new Intent();
        // 指定开启系统相机的Action
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        // 根据文件地址创建文件
        File file=new File(FILE_PATH);
        // 把文件地址转换成Uri格式
        Uri uri=Uri.fromFile(file);
        // 设置系统相机拍摄照片完成后图片文件的存放地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);


         *
         */
        button1.setOnClickListener {

            val intent = Intent()
            intent.action = MediaStore.ACTION_IMAGE_CAPTURE
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            startActivity(intent)
        }


        btn_mediaRecorder.setOnClickListener {
            recordAudio()
        }


        btn_play.setOnClickListener {
            play()
        }

        btn_open_camera.setOnClickListener {

        }

    }

    /**
     * 申请所有权限
     */
    private fun requestAllPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionList.add(Manifest.permission.RECORD_AUDIO)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionList.add(Manifest.permission.CAMERA)
        }

        if (permissionList.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionList.toTypedArray(), 100
            )
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun recordAudio() {
        isRecording = !isRecording
        if (isRecording) {
            recorder.stop()
        } else {
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            recorder.setOutputFile(PATH_NAME)
            recorder.prepare()
            recorder.start()
        }


    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100) {
            if (grantResults.isNotEmpty()) {
                grantResults.forEach {
                    if (it != PackageManager.PERMISSION_GRANTED) {
                        finish()
                    }
                }
            }

        }

    }


    private fun play() {
        val mMediaPlayer = MediaPlayer()
        mMediaPlayer.setDataSource(PATH_NAME!!.absolutePath)
//        mMediaPlayer.setDisplay(mSurfaceHolder)
        mMediaPlayer.setOnPreparedListener {
            mMediaPlayer.start()
        }
        mMediaPlayer.prepareAsync()

    }


}
