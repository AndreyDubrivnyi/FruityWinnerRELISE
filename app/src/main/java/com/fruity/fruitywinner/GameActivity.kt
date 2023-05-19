package com.fruity.fruitywinner

import android.animation.Animator
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.fruity.fruitywinner.databinding.ActivityGameBinding
import java.util.*

class GameActivity : AppCompatActivity() {
    private val secrets = Secrets()
    private lateinit var binding: ActivityGameBinding

    private var check1 = false
    private var check2 = false
    private var check3 = false
    private var check4 = false
    private var check5 = false
    private var checkAddElement = false
    private var speed = 0
    private var speed2 = 0
    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        checkAddElement = true
        Objects.Life = 4
        levelHard()
        val b = secrets.getKxtHdnTextBalance(packageName)
        binding.linear1.setOnClickListener {
            changeSpeed()
            Objects.SCORE += 3
            binding.numbers.text = b + "\n" + Objects.SCORE
            binding.linear1.animate().cancel()
        }
        binding.linear2.setOnClickListener {
            changeSpeed()
            Objects.SCORE += 2
            binding.numbers.text = b + "\n" + Objects.SCORE
            binding.linear2.animate().cancel()
        }
        binding.linear3.setOnClickListener {
            changeSpeed()
            Objects.SCORE += 5
            binding.numbers.text = b + "\n" + Objects.SCORE
            binding.linear3.animate().cancel()
        }
        binding.linear4.setOnClickListener {
            changeSpeed()
            Objects.SCORE += 4
            binding.numbers.text = b + "\n" + Objects.SCORE
            binding.linear4.animate().cancel()
        }
        binding.linear5.setOnClickListener {
            changeSpeed()
            Objects.SCORE += 1
            binding.numbers.text = b + "\n" + Objects.SCORE
            binding.linear5.animate().cancel()
        }
    }

    //changeSpeed
    private fun changeSpeed() {
        speed -= 40
        speed2 -= 45
    }

    // It is function for hard level
    private fun levelHard() {
        binding.start.setOnClickListener {
            Objects.Life = 4
            speed = 2400
            speed2 = 1400
            binding.start.visibility = View.INVISIBLE
            binding.linear1.isClickable = true
            binding.linear2.isClickable = true
            binding.linear3.isClickable = true
            binding.linear4.isClickable = true
            binding.linear5.isClickable = true
            spawnElements()
        }
    }

    //spawn elemnts
    private fun spawnElements() {
        animationFalling()
    }

    // random image
    private fun setImage() {
        val imageRes = intArrayOf(
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5,
            R.drawable.image6,
        )
        val r = Random()
        binding.linear1.setImageResource(imageRes[r.nextInt(imageRes.size)])
        binding.linear2.setImageResource(imageRes[r.nextInt(imageRes.size)])
        binding.linear3.setImageResource(imageRes[r.nextInt(imageRes.size)])
        binding.linear4.setImageResource(imageRes[r.nextInt(imageRes.size)])
    }

    // Music
    private fun soundClick() {
        if (Objects.checkActivateSound) {
            val mp: MediaPlayer = MediaPlayer.create(this, R.raw.losing)
            mp.start()
        }
    }

    //game over
    private fun gameOver() {
         val lif = secrets.getKxtHdnTextLife(packageName)
        binding.losing.visibility = View.VISIBLE
        soundClick()
        handler.postDelayed({
            Objects.Life = 4
            binding.life.text = lif + Objects.Life.toString()
            binding.losing.visibility = View.INVISIBLE
            binding.start.visibility = View.VISIBLE
        }, 2900)

        binding.linear1.isClickable = false
        binding.linear2.isClickable = false
        binding.linear3.isClickable = false
        binding.linear4.isClickable = false
        binding.linear5.isClickable = false
    }

    // start animation
    private fun animationFalling() {
        val lif = secrets.getKxtHdnTextLife(packageName)

        if (checkAddElement) {
            checkAddElement = false
            check5 = false
            setImage()
            val handler = Handler()
            handler.postDelayed({

                val bottomOfScreen =
                    (resources.displayMetrics.heightPixels - binding.linear5.height * 2).toFloat()
                binding.linear5.animate().translationY(bottomOfScreen).setDuration(speed.toLong())
                    .setListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {
                            binding.linear5.visibility = View.VISIBLE
                        }

                        override fun onAnimationEnd(animation: Animator) {
                            binding.linear5.translationY = -150f
                            if (Objects.Life <= 0) {
                                gameOver()
                            }
                            binding.linear5.visibility = View.INVISIBLE
                            if (Objects.Life > 0) {
                                if (!check5) {
                                    Objects.Life -= 1
                                    binding.life.text = lif + Objects.Life.toString()
                                }
                            }

                        }

                        override fun onAnimationCancel(animation: Animator) {
                            binding.linear5.translationY = -150f
                            check5 = true
                        }

                        override fun onAnimationRepeat(animation: Animator) {

                        }
                    }).start()
                checkAddElement = true
            }, speed2.toLong())
        }
        val random = (1..4).random()
        if (random == 1) {
            binding.linear1.visibility = View.VISIBLE
            check1 = false
            setImage()
            val bottomOfScreen =
                (resources.displayMetrics.heightPixels - binding.linear1.height * 2).toFloat()
            binding.linear1.animate().translationY(bottomOfScreen).setDuration(speed.toLong())
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {

                    }

                    override fun onAnimationEnd(animation: Animator) {
                        binding.linear1.translationY = -150f
                        if (Objects.Life <= 0) {
                            gameOver()
                        }
                        binding.linear1.visibility = View.INVISIBLE
                        if (Objects.Life > 0) {
                            if (!check1) {
                                Objects.Life -= 1
                                binding.life.text = lif + Objects.Life.toString()
                            }
                            spawnElements()
                        }

                    }


                    override fun onAnimationCancel(animation: Animator) {
                        binding.linear1.translationY = -150f
                        check1 = true
                    }

                    override fun onAnimationRepeat(animation: Animator) {

                    }

                }).start()
        }
        if (random == 2) {
            binding.linear2.visibility = View.VISIBLE
            check2 = false
            setImage()
            val bottomOfScreen =
                (resources.displayMetrics.heightPixels - binding.linear2.height * 2).toFloat()
            binding.linear2.animate().translationY(bottomOfScreen).setDuration(speed.toLong())
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {

                    }

                    override fun onAnimationEnd(animation: Animator) {
                        binding.linear2.translationY = -150f
                        if (Objects.Life <= 0) {
                            gameOver()
                        }
                        binding.linear2.visibility = View.INVISIBLE
                        if (Objects.Life > 0) {
                            if (!check2) {
                                Objects.Life -= 1
                                binding.life.text = lif + Objects.Life.toString()
                            }
                            spawnElements()
                        }

                    }


                    override fun onAnimationCancel(animation: Animator) {
                        binding.linear2.translationY = -150f
                        check2 = true

                    }

                    override fun onAnimationRepeat(animation: Animator) {

                    }

                }).start()
        }
        if (random == 3) {
            binding.linear3.visibility = View.VISIBLE
            check3 = false
            setImage()
            val bottomOfScreen =
                (resources.displayMetrics.heightPixels - binding.linear3.height * 2).toFloat()
            binding.linear3.animate().translationY(bottomOfScreen).setDuration(speed.toLong())
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        binding.linear3.translationY = -150f
                        if (Objects.Life <= 0) {
                            gameOver()
                        }
                        binding.linear3.visibility = View.INVISIBLE
                        if (Objects.Life > 0) {
                            if (!check3) {
                                Objects.Life -= 1
                                binding.life.text = lif + Objects.Life.toString()
                            }
                            spawnElements()
                        }
                    }


                    override fun onAnimationCancel(animation: Animator) {
                        binding.linear3.translationY = -150f
                        check3 = true
                    }

                    override fun onAnimationRepeat(animation: Animator) {

                    }

                }).start()
        }
        if (random == 4) {
            binding.linear4.visibility = View.VISIBLE
            check4 = false
            setImage()
            val bottomOfScreen =
                (resources.displayMetrics.heightPixels - binding.linear4.height * 2).toFloat()
            binding.linear4.animate().translationY(bottomOfScreen).setDuration(speed.toLong())
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {

                    }

                    override fun onAnimationEnd(animation: Animator) {
                        binding.linear4.translationY = -150f
                        if (Objects.Life <= 0) {
                            gameOver()
                        }
                        binding.linear4.visibility = View.INVISIBLE
                        if (Objects.Life > 0) {
                            if (!check4) {
                                Objects.Life -= 1
                                binding.life.text = lif + Objects.Life.toString()
                            }
                            spawnElements()
                        }
                    }


                    override fun onAnimationCancel(animation: Animator) {
                        binding.linear4.translationY = -150f
                        check4 = true

                    }

                    override fun onAnimationRepeat(animation: Animator) {


                    }

                }).start()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.no_anim, R.anim.slide_bot)

    }
}