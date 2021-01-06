package com.projetDonjon.responseJson

import com.projetDonjon.model.Room

/**
 * The class that give us the result of the connect action
 */
data class ConnectJson(val guid:String,
                        val totalVie:Int,
                        val salle:Room)
{

}
