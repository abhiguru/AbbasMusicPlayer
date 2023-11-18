package `in`.tutorial.abbasmusicplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.os.postDelayed
import `in`.tutorial.abbasmusicplayer.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    var startTime = 0.0
    var finalTime = 0.0
    var forwardTime = 10000
    var backwardTime = 1000
    var oneTimeOnly = 0
    var handler : Handler = Handler(Looper.getMainLooper())
    var mediaPlayer = MediaPlayer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mediaPlayer = MediaPlayer.create(
            this,
            R.raw.astronaut
        )
        binding.tvSongTitle.text = ""+ resources.getIdentifier("astronaut", "raw", packageName)
        binding.sbMain.isClickable = false
        binding.ivBtnPlay.setOnClickListener {
            mediaPlayer.start()
            startTime = mediaPlayer.currentPosition.toDouble()
            finalTime = mediaPlayer.duration.toDouble()
            if (oneTimeOnly == 0){
                binding.sbMain.max = finalTime.toInt()
                oneTimeOnly = 1
            }
            binding.tvTime.text = startTime.toString()
            binding.sbMain.setProgress(startTime.toInt())
            handler.postDelayed(updateSongTime, 100)
        }
        binding.ivBtnPause.setOnClickListener {
            mediaPlayer.pause()
        }
        binding.ivBtnRewind.setOnClickListener {
            var temp = startTime
            if(temp - forwardTime > 0){
                startTime = startTime - forwardTime
                mediaPlayer.seekTo(startTime.toInt())
            }else{
                mediaPlayer.seekTo(0)
            }
        }
        binding.ivBtnFF.setOnClickListener {
            var temp = startTime
            if(temp + forwardTime <= finalTime){
                startTime = startTime + forwardTime
                mediaPlayer.seekTo(startTime.toInt())
            }
        }
    }
    val updateSongTime : Runnable = object: Runnable {
        override fun run() {
            startTime = mediaPlayer.currentPosition.toDouble()
            binding.tvTime.text = "" +
                    String.format(
                        "%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(
                            startTime.toLong() -
                                    TimeUnit.MINUTES.toSeconds(
                                        TimeUnit.MILLISECONDS.toMinutes(startTime.toLong())
                                    )
                        )
                    )
            binding.sbMain.progress = startTime.toInt()
            handler.postDelayed(this, 100)
        }
    }
}