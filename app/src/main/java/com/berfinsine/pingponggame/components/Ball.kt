package com.berfinsine.pingponggame.components

import android.content.Context
import android.graphics.Rect
import java.lang.System.currentTimeMillis

class Ball(context: Context, shapeId: Int?) : Sprite(context, shapeId) {
    var deltaOffset: Float = 1f

    override var updateRate: Int = 100
    override var timeToUpdate: Long = currentTimeMillis()

    init {
        this.movVec.set(3f, 4f)
    }

    override fun update() {
        if (shouldUpdate) {
            val current = currentTimeMillis()
            val delta = current - timeToUpdate
            deltaOffset = 1f + delta.toFloat() * updateRate / 1000f
            timeToUpdate = current + 1000L / updateRate
            location.offset(
                movVec.x * deltaOffset,
                movVec.y * deltaOffset
            )
        }
    }
}


    /*


    override fun collide(o: Any?): Boolean {
        var endgame = false
        if (o is Rect) {
            if (location.left <= 0 || location.right >= o.right) {
                movVec.x = -movVec.x
                if (location.left <= 0) {
                    location.offset(-location.left, 0f)
                } else {
                    location.offset(o.right - location.right, 0f)
                }
            }
            if (location.top <= 0 || location.bottom >= o.bottom) {
                movVec.y = -movVec.y
                if (location.top <= 0) {
                    location.offset(0f, -location.top)
                } else {
                    location.offset(0f, o.bottom - location.bottom)
                }
                endgame = true
            }

        }
        if (o is Player) {
            if (o.location.contains(location.centerX(), location.bottom)) {
                movVec.y = -movVec.y
                location.offset(0f, o.location.top - location.bottom)
            } else if (o.location.contains(location.centerX(), location.top)
            ) {
                movVec.y = -movVec.y
                location.offset(0f, o.location.bottom - location.top)
            }
        }
        return endgame
    }

    override var frameRate: Int = 60
        set(value) {
            field = value
        }
    override var timeToUpdate: Long = currentTimeMillis()
        set(value) {
            field = value
        }
*/


