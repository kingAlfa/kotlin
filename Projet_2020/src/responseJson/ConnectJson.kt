package com.projetDonjon.responseJson

import com.projetDonjon.model.Room

data class ConnectJson(val guid:String,
                        val totalVie:Int,
                        val salle:Room)
{

}
