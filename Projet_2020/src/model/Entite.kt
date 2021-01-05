package com.projetDonjon.model

abstract class Entite(
)
    {

        open var guid:String?=null
        open var vie:Int=10
        open fun attacker(elt:Entite,degats:Int){}

    }
