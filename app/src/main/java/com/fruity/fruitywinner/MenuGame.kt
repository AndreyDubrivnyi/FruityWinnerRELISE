package com.fruity.fruitywinner

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.ViewGroup
import android.widget.Toast
import com.fruity.fruitywinner.databinding.ActivityMenuGameBinding
import com.onesignal.OneSignal

class MenuGame : AppCompatActivity() {
    private lateinit var binding: ActivityMenuGameBinding
    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityMenuGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
            OneSignal.disablePush(true)
            binding.refresh.setOnClickListener{
                val params: ViewGroup.LayoutParams =
                    binding.refresh.layoutParams as ViewGroup.LayoutParams
                params.width -= 4
                params.height -= 4
                binding.refresh.layoutParams = params
                handler.postDelayed({
                    params.width += 4
                    params.height += 4
                    binding.refresh.layoutParams = params
                }, 140)
                checkConnection()
            }

        }

        private fun intentToMenu(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        private fun checkConnection() {
            val secrets = Secrets()
            val textConnect = secrets.getKxtHdnToastForConnection(packageName)
            val connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo == null || !activeNetworkInfo.isConnected) {
                Toast.makeText(this, textConnect, Toast.LENGTH_SHORT).show()
            } else {
                intentToMenu()
            }
        }
    }