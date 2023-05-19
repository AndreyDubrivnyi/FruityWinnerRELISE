package com.fruity.fruitywinner

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.ViewGroup
import com.fruity.fruitywinner.databinding.ActivitySettBinding

class SettActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettBinding
    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        val secrets = Secrets()
        val sOn = secrets.getKxtHdnTextSoundOn(packageName)
        val sOff = secrets.getKxtHdnTextSoundOff(packageName)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        binding = ActivitySettBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (Objects.checkActivateSound) {
            binding.sound.text = sOn
        } else {
            binding.sound.text = sOff
        }
        binding.sound.setOnClickListener {
            val params: ViewGroup.LayoutParams =
                binding.sound.getLayoutParams() as ViewGroup.LayoutParams
            params.width -= 4
            params.height -= 4
            binding.sound.setLayoutParams(params)
            handler.postDelayed({
                params.width += 4
                params.height += 4
                binding.sound.setLayoutParams(params)
            }, 140)

            if (Objects.checkActivateSound) {
                Objects.checkActivateSound = false
                binding.sound.text = sOff
            } else {
                Objects.checkActivateSound = true
                binding.sound.text = sOn
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.no_anim, R.anim.slide_bot)

    }
}