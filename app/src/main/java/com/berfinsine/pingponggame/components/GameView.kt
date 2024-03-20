package com.berfinsine.pingponggame.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.berfinsine.pingponggame.interfaces.GameLoop

class GameView (context: Context?, vsAI: Boolean = true) : SurfaceView(context),
    SurfaceHolder.Callback, Runnable,
    GameLoop {
    override var updateRate: Int = 120
    override var timeToUpdate: Long = System.currentTimeMillis()

    private var mThread: Thread? = null
    lateinit var mCanvas: Canvas
    var vsAI = true

    var mHolder: SurfaceHolder?
    private var bounds: Rect =Rect()
        set(value) {
            field = value
            setup()
        }

    var game: Game? = null

    init {
        mHolder = holder
        if (mHolder != null) {
            mHolder?.addCallback(this)
        }
        this.vsAI = vsAI
    }

    fun setup() {
        game = Game(this.context, vsAI, bounds)
        start()
    }

    fun start() {
        game?.state = Game.STATE.STARTED
        mThread = Thread(this)
        timeToUpdate = System.currentTimeMillis()
        game?.ball!!.timeToUpdate = System.currentTimeMillis()
        mThread?.start()
    }

    fun stop() {
        game?.state = Game.STATE.END
        try {
            mThread?.join()
        } catch (_:InterruptedException) {
        }
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        Log.d(this.toString(), "surface created")
    }

    override fun surfaceChanged(p0:SurfaceHolder, p1: Int, p2: Int, p3:Int) {
        Log.d(this.toString(), "surface changed")
        bounds= Rect(50, p3 / 12, p2 - 50, p3 - p3 / 12)
    }

    override fun surfaceDestroyed(p0: SurfaceHolder){
        stop()
    }

    override fun run() {
        while (game?.state == Game.STATE.STARTED) {
            while (shouldUpdate) {
                update()
            }
            render()
        }

    }

    override fun update() {
        timeToUpdate = System.currentTimeMillis() + 1000L / updateRate
        game?.update()
    }

    override fun render(canvas: Canvas?) {
        if(mHolder!!.surface?.isValid == true) {
            mCanvas = mHolder!!.lockCanvas()
            mCanvas.drawColor(Color.WHITE)
            game?.render(mCanvas)
            mHolder!!.unlockCanvasAndPost(mCanvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        game?.processInput(event)
        return true
    }
}


