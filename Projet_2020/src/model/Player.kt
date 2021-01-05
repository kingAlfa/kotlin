package com.projetDonjon.model

import java.time.LocalDateTime

data class Player( var totalVie:Int=5,
                   var description:String="Un player",
                   var type:String="Player"):Entite(){
    init {
        val current =LocalDateTime.now().second
       val s =LocalDateTime.now().minute
        guid= "P$s$current"
    }

    override fun attacker(elt:Entite,degats:Int) {
        super.attacker(elt,degats)
        this.vie+=degats
        this.totalVie+=degats
        elt.vie-=degats
    }


}
