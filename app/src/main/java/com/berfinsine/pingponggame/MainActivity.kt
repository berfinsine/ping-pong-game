package com.berfinsine.pingponggame

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.berfinsine.pingponggame.R
import com.berfinsine.pingponggame.components.Game
import com.berfinsine.pingponggame.components.GameView
import com.berfinsine.pingponggame.interfaces.GameStateListener

class MainActivity : AppCompatActivity(), View.OnClickListener, GameStateListener {
    lateinit var ping: GameView
    lateinit var menuDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createDialog()
    }

    private fun createDialog(viewId: Int = R.layout.menu) {
        menuDialog = AlertDialog.Builder(this).setView(viewId)
            .setCancelable(false).create()
        menuDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        when(viewId){
            R.layout.pause -> {
            }
        }
    }

    override fun onResume(){
        super.onResume()
        menuDialog.show()
    }

    override fun onPause(){
        super.onPause()
        ping.stop()
    }

    override fun onClick(p0: View?){
        if (menuDialog.isShowing){
            menuDialog.dismiss()
        }

        when (p0?.id){
            R.id.single_player_btn,
            R.id.two_player_btn -> {
                ping = GameView(this, p0?.id == R.id.single_player_btn)
                setContentView(ping)
            }
            R.id.quit_btn -> this.finish()
            R.id.continue_btn -> {
                ping.start()
            }
        }
    }

    override fun stateChanged(state: Game.STATE){

        Log.d("state", "changed to $state")
        when (state){
            Game.STATE.END -> runOnUiThread( {
                createDialog(R.layout.menu)
                menuDialog.show()
            })

            Game.STATE.PAUSED -> runOnUiThread( {
                createDialog(R.layout.pause)
                menuDialog.show()
            })

            else -> {

            }
        }
    }
}