package com.example.apprelogio

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.apprelogio.databinding.ActivityFullscreenBinding

class FullscreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullscreenBinding
    private var isChecked = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //VERIFICACAO A NIVEL DE VERSAO E ESCONDER AS FLAGS DO FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        // MANTER A TELA SEMPRE LIGADA
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        //PARA MOSTRAR O NIVEL DE BATERIA
        //Implementar o object (deixar mouse em cima, ou aparecer alertar vermelho)
        val bateriaReceiver: BroadcastReceiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent != null){
                    val nivel: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
                    //Toast.makeText(applicationContext, nivel.toString(), Toast.LENGTH_SHORT).show()
                    binding.textNivelBateria.text = "${nivel.toString()}%"
                }
            }

        }
        //QUANDO O NIVEL BATERIA SEJA ALTERADO
        registerReceiver(bateriaReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        binding.checkNivelBateria.setOnClickListener {
            if(isChecked){
                isChecked = false
                binding.textNivelBateria.visibility = View.GONE
            } else {
                isChecked = true
                binding.textNivelBateria.visibility = View.VISIBLE
            }
            binding.checkNivelBateria.isChecked = isChecked
        }

        //ESCONDER O LAYOUT QUANDO INICIAR O APP
        binding.layoutMenu.animate().translationY(500f)

        binding.imageViewPreferencias.setOnClickListener {
            binding.layoutMenu.visibility = View.VISIBLE
            binding.layoutMenu.animate().translationY(0F).setDuration(
                resources.getInteger(android.R.integer.config_mediumAnimTime).toLong() //ESSA CONFIGURACAO VEM DO ANDROID
            )
        }

        binding.imageViewFechar.setOnClickListener{
            binding.layoutMenu.animate().translationY(binding.layoutMenu.measuredHeight.toFloat()) //medir a altura do elemento
                .setDuration(
                resources.getInteger(android.R.integer.config_mediumAnimTime).toLong() //ESSA CONFIGURACAO VEM DO ANDROID
            )
        }

    }
}