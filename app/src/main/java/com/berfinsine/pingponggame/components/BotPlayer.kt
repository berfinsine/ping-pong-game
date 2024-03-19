package com.berfinsine.pingponggame.components;

import android.content.Context

class BotPlayer (context: Context, shapeId: Int?, game: Game?):
    Player(context, shapeId){
    var game: Game?

    enum class TYPE {
        DUMP, NORMAL, SMART
    }

    var type = TYPE.SMART

    init {
        when (type) {
            TYPE.DUMP -> {
                location.right = (game!!.bounds.width()/ 2.5).toFloat()
                movVec.x = game.ball.movVec.x * 1.6f
                updateRate = 50
            }
            TYPE.NORMAL -> {
                updateRate = 80
                location.right = (game!!.bounds.width()/ 4).toFloat()
                movVec.x = game.ball.movVec.x * 1.6f

            }
            TYPE.SMART -> {
                location.right = (game!!.bounds.width()/ 6).toFloat()
                movVec.x = game.ball.movVec.x * 2
                updateRate = 100
            }
        }

        this.game = game
    }
    override fun update(){
        if (shouldUpdate) {
            super.update()
            if (game!!.ball.movVec.y > 0 && location.centerX().toInt() == game!!.bounds.centerX()){
                return
            }
            location.offset(movVec.x, movVec.y)
            if (game?.ball?.location!!.centerX() > location.right ||
                game?.ball?.location!!.centerX() < location.left && game!!.ball.movVec.y < 0){
                if ((location.centerX() - game?.ball?.location!!.centerX()) *  movVec.x > 0){
                    movVec.x = -movVec.x
                }
            } else if (location.left <= 0 || location.right >= game!!.bounds.width()){
                movVec.x = -movVec.x
            }
        }
    }
}