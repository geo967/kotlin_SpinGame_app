package com.example.spingame_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.spingame_kotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() , Animation.AnimationListener{
    private var count=0
    private var flag=false
    lateinit var binding: ActivityMainBinding

    private var powerButton:ImageButton?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        powerButton=findViewById(R.id.powerButton)

        powerButton!!.setOnTouchListener(PowerTouchListener())

        intSpinner()

    }

    val prizes= intArrayOf(200,2000,20000,600,1000,500,400,700,800,3000,100,1200)
    private var mSpinDuration:Float=0f
    private var mSpinRevolution=0f
    var pointerImageView:ImageView?=null
    var info:TextView?=null
    var prizeText="N/A"

    private fun intSpinner() {
        pointerImageView=findViewById(R.id.imageWheel)
        info=findViewById(R.id.infoText)

    }
    fun startSpinner(){
        mSpinDuration=3600f
        mSpinRevolution=5000f
        if(count>=30){
            mSpinDuration=1000f
            mSpinRevolution=(3600*2).toFloat()
        }
        if(count>=60){
            mSpinDuration=15000f
            mSpinRevolution=(3600*3).toFloat()
        }
        //final

        val end=Math.floor(Math.random()*3600).toInt()
        val numOfPrize=prizes.size
        val degreesPesPrize=360/numOfPrize
        val shift=0
        val prizeIndex=(shift+end)%numOfPrize
        prizeText="Prize is : ${prizes[prizeIndex]}"
        val rotateAnim=RotateAnimation(
            0f,mSpinRevolution+end,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f
        )
        rotateAnim.interpolator=DecelerateInterpolator()
        rotateAnim.repeatCount=0
        rotateAnim.duration=mSpinDuration.toLong()
        rotateAnim.setAnimationListener(this)
        rotateAnim.fillAfter=true
        pointerImageView!!.startAnimation()

    }
    private inner class PowerTouchListener:View.OnTouchListener{
        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
            when (p1!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    flag = true
                    count = 0
                    Thread {
                        while (flag) {
                            count++
                            if (count == 100) {
                                try {
                                    Thread.sleep(100)
                                } catch (e: InterruptedException) {
                                    e.printStackTrace()
                                }
                                count = 0
                            }
                            try {
                                Thread.sleep(10)
                            } catch (e: InterruptedException) {
                                e.printStackTrace()
                            }
                        }
                    }.start()
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    flag = false
                    startSpinner()
                    return false
                }

            }
        }
    }

    override fun onAnimationStart(p0: Animation?) {
        info!!.text="Spinning..."
    }

    override fun onAnimationEnd(p0: Animation?) {
        info!!.text=prizeText
    }

    override fun onAnimationRepeat(p0: Animation?) {

    }
}