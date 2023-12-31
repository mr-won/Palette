package com.example.palette

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat

class minute1Activity : AppCompatActivity() {
    private var countdown_timer: CountDownTimer? = null
    private var time_in_milliseconds: Long = 60000
    private var is_running = false

    private lateinit var start_btn : Button
    private lateinit var reset_btn : Button

    private lateinit var tv_second : TextView
    private lateinit var tv_minute : TextView

    private val TAG = minute1Activity::class.java.simpleName

    // Noti 객체 생성
    private lateinit var notificationHelper: NotificationHelper

    @RequiresApi(VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_minute1)

        tv_minute = findViewById(R.id.tv_minute1)
        tv_second = findViewById(R.id.tv_second1)

        set_minute_1()

        start_btn = findViewById(R.id.btn_start)

        reset_btn = findViewById(R.id.btn_reset)

        /*// 객체 생성
        val titleEdit: EditText = findViewById(R.id.title_edit)
        val messageEdit: EditText = findViewById(R.id.message_edit)
        val notiBtn: Button = findViewById(R.id.noti_btn)
*/
        // Noti 초기화
        notificationHelper = NotificationHelper(this)

        start_btn.setOnClickListener {
            if (is_running) {
                pause_timer()
            } else {
                start_timer()
            }
        }

        findViewById<Button>(R.id.btn_reset).setOnClickListener {
            reset_timer()
        }
    }

    private fun start_timer() {
        countdown_timer = object : CountDownTimer(time_in_milliseconds, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                time_in_milliseconds = millisUntilFinished
                update_timer_text()
            }

            override fun onFinish() {
                is_running = false
                ringPlay()
                notiPlay()
                start_btn.text = "시작"
                start_btn.setBackgroundColor(getColor(R.color.blue))
            }
        }.start()

        is_running = true
        start_btn.text = "일시정지"
        start_btn.setBackgroundColor(getColor(R.color.red))
    }

    private fun pause_timer() {
        countdown_timer?.cancel()
        is_running = false
        start_btn.text = "시작"
        start_btn.setBackgroundColor(getColor(R.color.blue))
    }

    private fun reset_timer() {
        countdown_timer?.cancel()
        is_running = false
        time_in_milliseconds = 60000
        set_minute_1()
        start_btn.text = "시작"
        start_btn.setBackgroundColor(getColor(R.color.blue))
    }

    private fun update_timer_text() {
        val minutes = (time_in_milliseconds / 1000) / 60
        val seconds = (time_in_milliseconds / 1000) % 60

        tv_minute.text = if (minutes < 10) "0$minutes" else "$minutes"
        tv_second.text = if (seconds < 10) ":0$seconds" else ":$seconds"
    }

    private fun set_minute_1() {
        tv_minute.text = "01"
        tv_second.text = ":00"
    }

    private fun set_minute_0() {
        tv_minute.text = "00"
        tv_second.text = ":00"
    }

    private fun ringPlay() {
        // 시스템 소리
        /* // 소리 얻기
         val sound : Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

         // 소리 담기
         val ringtone = RingtoneManager.getRingtone(applicationContext, sound)

         // 실행
         ringtone.play()*/

        // 사용자 정의

        // 소리 얻기
        val player: MediaPlayer = MediaPlayer.create(this, R.raw.tiny_button_push_sound)

        // 실행
        player.start()
    }

    @RequiresApi(VERSION_CODES.O)
    @SuppressLint("ServiceCast")
    private fun vibratorPlay() {

        val vibrator = getSystemService(VIBRATOR_MANAGER_SERVICE) as Vibrator

        // 버전 오레오, 그 이상(안드로이드 8)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 진동시간, 세기 설정(0.5초, 기본세기)
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            // 0.5초 동안 울린다.
            vibrator.vibrate(500)
        }
    }

    private fun notiPlay() {
        /*val title: String = titleEdit.text.toString()
        val message: String = messageEdit.text.toString()*/
        
        val title = "1분 크로키"
        val message = "종료"

        // 알림 호출
        showNotification(title, message)
    }

    // 알림 호출
    private fun showNotification(title:String, message:String) {
        val nb: NotificationCompat.Builder =
            notificationHelper.getChannelNotification(title,message)

        notificationHelper.getManager().notify(1, nb.build())
    }

}