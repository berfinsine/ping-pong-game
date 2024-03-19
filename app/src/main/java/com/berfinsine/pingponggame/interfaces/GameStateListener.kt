
package com.berfinsine.pingponggame.interfaces;


import com.berfinsine.pingponggame.components.Game

interface GameStateListener {
    fun stateChanged(state: Game.STATE)
}