package com.projetDonjon.model

import java.time.LocalDateTime
import kotlin.random.Random

data class Monster(
    var totalVie:Int=10,
     var description:String="Un monstre",
    var type:String="Monster"): Entite()
        {

            init {
                val current =LocalDateTime.now().minute
                val s =LocalDateTime.now().minute
                val randomValue = List(1){ Random.nextInt(0,100)}
                guid= "M$s${current+randomValue[0]}"
            }
            override fun attacker(elt:Entite,degats:Int) {
                super.attacker(elt,degats)
                this.vie+=degats
                elt.vie-=degats
            }



        }
