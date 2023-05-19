package com.fruity.fruitywinner

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.ViewGroup
import android.widget.Toast
import com.fruity.fruitywinner.databinding.ActivityMenuBinding
import com.onesignal.OneSignal

class Menu : AppCompatActivity() {
    lateinit var binding: ActivityMenuBinding
    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        OneSignal.disablePush(true)
        binding.beginPlay.setOnClickListener {
                val params: ViewGroup.LayoutParams =
                    binding.beginPlay.layoutParams as ViewGroup.LayoutParams
                params.width -= 4
                params.height -= 4
                binding.beginPlay.layoutParams = params
                handler.postDelayed({
                    params.width += 4
                    params.height += 4
                    binding.beginPlay.layoutParams = params
                }, 140)
                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_top, R.anim.no_anim)
        }
        binding.sett.setOnClickListener {
            val params: ViewGroup.LayoutParams =
                binding.sett.layoutParams as ViewGroup.LayoutParams
            params.width -= 4
            params.height -= 4
            binding.sett.layoutParams = params
            handler.postDelayed({
                params.width += 4
                params.height += 4
                binding.sett.layoutParams = params
            }, 140)

            val intent = Intent(this, SettActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_top, R.anim.no_anim)
        }

        binding.information.setOnClickListener {
            val params: ViewGroup.LayoutParams =
                binding.information.layoutParams as ViewGroup.LayoutParams
            params.width -= 4
            params.height -= 4
            binding.information.layoutParams = params
            handler.postDelayed({
                params.width += 4
                params.height += 4
                binding.information.layoutParams = params
            }, 140)

            val intent = Intent(this, InformationActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_top, R.anim.no_anim)
        }
        binding.buttonPrivacyPolicy.setOnClickListener{
            val params: ViewGroup.LayoutParams =
                binding.buttonPrivacyPolicy.layoutParams as ViewGroup.LayoutParams
            params.width -= 4
            params.height -= 4
            binding.buttonPrivacyPolicy.layoutParams = params
            handler.postDelayed({
                params.width += 4
                params.height += 4
                binding.buttonPrivacyPolicy.layoutParams = params
            }, 140)
            onClickPrivacyButton()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.no_anim, R.anim.slide_bot)

    }

    private fun onClickPrivacyButton() {
        val secrets = Secrets()
        val textPC = secrets.getKxtHdnToastForPc(packageName)
        try {
            val PRIVACY_POLICY_LINK = "https://telegra.ph/Privacy-Policy-04-04-24"
            val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_LINK))
            startActivity(myIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this, textPC, Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
        }
    }
}
