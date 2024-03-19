package com.berfinsine.pingponggame.components;

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import com.berfinsine.pingponggame.interfaces.GameLoop
import kotlin.jvm.Volatile;

abstract class Sprite (context: Context, shapeId: Int?) : GameLoop {
    @Volatile
    var movVec = PointF()
    var paint = Paint()
    var scale = 1f
        set (value) {
            field = value
            location.right *= scale
            location.bottom *= scale
        }
    var context: Context? = context
    var location = RectF(0f, 0f, 80f, 80f)
    var shape: Bitmap? = null
    override fun render(canvas: Canvas?) {
        if (shape != null) {
            canvas?.drawBitmap(shape!!, null, location, paint)
        } else {
            canvas?.drawRect(location, paint)
        }
    }
    init {
        this.shape = shapeId?.let { BitmapFactory.decodeResource(context.resources, it)}
        if (shape != null) {
            location.right = shape!!.width.toFloat()
            location.bottom = shape!!.height.toFloat()
        }
    }
}
