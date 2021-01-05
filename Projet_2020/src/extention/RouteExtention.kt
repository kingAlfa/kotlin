package com.projetDonjon.extention

import com.projetDonjon.model.Player
import com.projetDonjon.model.Room
import com.projetDonjon.responseJson.ConnectJson
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

/**
 * Builds a route to match the connect action
 */

fun Route.post(connect:String,player: Player,room: Room){
    this.post(connect){
        player.guid?.let { it1 -> ConnectJson(it1,player.totalVie,room) }?.let {
                it2 -> call.respond(it2)
        }
    }
}
