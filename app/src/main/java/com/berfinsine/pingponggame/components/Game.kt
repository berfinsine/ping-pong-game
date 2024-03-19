package com.berfinsine.pingponggame.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.MotionEvent
import com.berfinsine.pingponggame.R
import com.berfinsine.pingponggame.interfaces.GameLoop
import com.berfinsine.pingponggame.interfaces.GameStateListener
import java.lang.Exception

class Game (context: Context, vsAI: Boolean = true, bounds: Rect) : GameLoop {
    var onGameStateChangeListener: GameStateListener? = null

    enum class STATE {
        END, PAUSED, STARTED
    }

    var state = STATE.PAUSED
        set(value) {
            try {
                if (field != value)
                    onGameStateChangeListener?.stateChanged(value)
            } catch (e: Exception) {
            }
            field = value
        }


    var bounds: Rect
    var ball: Ball
    var players: Array<Player>

    init {
        this.bounds = bounds
        ball = Ball(context, R.drawable.ball)
        players = arrayOf(
            Player(context, R.drawable.button),
            if (vsAI) BotPlayer(context, R.drawable.button, this) else
                Player(context, R.drawable.button)
        )

        players[0].location.offsetTo(
            bounds.exactCenterX() - players[0].location.width() / 2,
            bounds.bottom - players[0].location.height()
        )

        players[1].location.offsetTo(
            bounds.exactCenterX() - players[1].location.width()/2,
            bounds.top.toFloat()
        )
        ball.location.offsetTo(
            players[1].location.centerX() - ball.location.centerX(),
            players[1].location.bottom
        )
        onGameStateChangeListener = context as GameStateListener

    }
    override var updateRate: Int = 60
    override var timeToUpdate: Long = 0L

    override fun render(canvas: Canvas?) {
        ball.render(canvas)
        for(p in players) p.render(canvas)
    }

    override fun update() {
        ball.update()
        for (p in players) p.update()
        if (collide(bounds)) state = STATE.END
        for (p in players) collide(p)
    }

    fun processInput(o: Any?) {
        if (o is MotionEvent) {
            if (o.y > bounds.exactCenterY()) {
                players[0].location.offsetTo(o.x, players[0].location.top)
            } else if (players[1] !is BotPlayer) {
                players[1].location.offsetTo(o.x, players[1].location.top)
            }
        }
    }

    fun collide(o: Any?) : Boolean {
        if ( o is Rect) {
            if (ball.location.left <= o.left || ball.location.right >= o.right) {
                ball.movVec.x = -ball.movVec.x
                if (ball.location.left <= o.left) {
                    ball.location.offset(o.left - ball.location.left, 0f)
                } else {
                    ball.location.offset(o.right - ball.location.right, 0f)
                }
            }

            if (ball.location.top <= o.left || ball.location.bottom >= o.bottom) {
                if (ball.location.top <= o.top) {
                    players[0].score++
                    players[1].health--
                    ball.location.offsetTo(
                        players[1].location.centerX(),
                        players[1].location.bottom
                    )
                } else {
                    players[1].score++
                    players[0].health--
                    ball.location.offset(o.left - ball.location.left, 0f)
                    ball.location.offsetTo(
                        players[0].location.centerX() - ball.location.width() / 2,
                        players[0].location.top - ball.location.height()
                    )
                }

                if (players[0].health == 0 || players[1].health == 0) {
                    return true
                } else {
                    this.state = STATE.PAUSED
                }
            }
        } else if (o is Player) {
            if (o.location.contains(ball.location.centerX(), ball.location.bottom)) {
                ball.movVec.y = - ball.movVec.y
                ball.location.offset(0f, o.location.top - ball.location.bottom)
            } else if (o.location.contains(ball.location.centerX(),ball.location.top)
            ) {
                ball.movVec.y = -ball.movVec.y
                ball.location.offset(0f, o.location.bottom-ball.location.top)
            }
        }
        return false
    }

}