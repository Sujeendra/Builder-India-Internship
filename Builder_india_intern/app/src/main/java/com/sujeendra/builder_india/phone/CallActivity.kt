package com.sujeendra.builder_india.phone

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.telecom.Call
import android.telecom.CallAudioState
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_call.*

import java.util.concurrent.TimeUnit

import android.media.AudioAttributes
import android.net.Uri

import android.view.View
import android.widget.Toast

import java.io.IOException

class CallActivity : AppCompatActivity() {
   lateinit var call: CallAudioState
    private val disposables = CompositeDisposable()
    private lateinit var number: String


    fun stopButton(view: View) {
        val sharedPreferences = this.getSharedPreferences("com.sujee.storage_app", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("boolean",false).apply()
        Toast.makeText(
            this,
            "Calls loop has stopped to continue \npress hung up and then press Enter\ntostart from where You left",
            Toast.LENGTH_LONG
        ).show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)

        number = intent.data.schemeSpecificPart
    }

    override fun onStart() {
        super.onStart()

        answer.setOnClickListener {
            OngoingCall.answer()
        }

        hangup.setOnClickListener {
            OngoingCall.hangup()
        }

        OngoingCall.state
            .subscribe(::updateUi)
            .addTo(disposables)

        OngoingCall.state
            .filter { it == Call.STATE_DISCONNECTED }
            .delay(1, TimeUnit.SECONDS)
            .firstElement()
            .subscribe { finish() }
            .addTo(disposables)
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(state: Int) {
        callInfo.text = "${state.asString().toLowerCase().capitalize()}\n$number"

        if(state==Call.STATE_ACTIVE){
            Toast.makeText(
                this,
                "User "+"with "+"$number"+" "+"Successfully Verified",
                Toast.LENGTH_LONG
            ).show()
            val uri = Uri.parse("android.resource://" + this.packageName + "/" + R.raw.test)
            val player = MediaPlayer()
            player.reset()
            player.setAudioAttributes(
                AudioAttributes.Builder()
                    .setLegacyStreamType(AudioManager.STREAM_VOICE_CALL).build()
            )
            try {
                player.setDataSource(this, uri)
                player.prepareAsync()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            player.setOnPreparedListener { mediaPlayer ->
                if (!mediaPlayer.isPlaying) {
                    mediaPlayer.start()
                }
            }

        }

        answer.isVisible = state == Call.STATE_RINGING
        hangup.isVisible = state in listOf(
            Call.STATE_DIALING,
            Call.STATE_RINGING,
            Call.STATE_ACTIVE
        )
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }

    companion object {
        fun start(context: Context, call: Call) {
            Intent(context, CallActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setData(call.details.handle)
                .let(context::startActivity)
        }
    }

}
