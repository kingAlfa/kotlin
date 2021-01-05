package com.projetDonjon.model

import java.util.concurrent.CopyOnWriteArrayList

data class Room(val description:String,
                var passage:CopyOnWriteArrayList<String>,
                var entites:CopyOnWriteArrayList<String?>)
