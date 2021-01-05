package com.projetDonjon.objet

import com.projetDonjon.model.Monster
import com.projetDonjon.model.Player
import com.projetDonjon.model.Room
import java.util.concurrent.CopyOnWriteArrayList
object GameObjet {

    private  var listMonster = CopyOnWriteArrayList<Monster>()
    private  var listRoom=CopyOnWriteArrayList<Room>()
    private  var player_in_room= HashMap<Player,Room>()

    fun addMonster(monster: Monster){
        this.listMonster.add(monster)
    }

    fun putPlayerInRoom(p:Player,r:Room)
    {
        player_in_room[p] = r
    }

    fun getR(p:Player)= run { this.player_in_room[p] }

    fun getP(): Player? = run {
        val mutList = this.player_in_room.keys
        return mutList.elementAtOrNull(0)
    }

    fun getMonsterInRoom(guid:String,p: Player): Monster? {
        val currentRoom = this.getR(p)
        var res :Monster? = null
        if (currentRoom != null ){
            if (currentRoom.entites.contains(guid)) {
                listMonster.filter { it.guid == guid }[0].also { res = it }
                return res
            }
            res=null
        }
        return res

    }

    fun setUpDatePlayerAndMonsterAfterAttack(player: Player,monster: Monster,currentRoom: Room){
        val newMonster= monster.guid
        val index = listMonster.indexOf(listMonster.filter{it.guid == newMonster}[0])
        listMonster[index] = monster
        val guidInRoom = currentRoom.entites.indexOf(newMonster)
        currentRoom.entites[guidInRoom] = newMonster
        putPlayerInRoom(player,currentRoom)

    }

    fun addListRoom(room: Room){
        if (!listRoom.contains(room)){
        listRoom.add(room)
        }
    }

    private fun makeCopyOnWriteArrayList(l:Array<String>):CopyOnWriteArrayList<String>{
        val res = CopyOnWriteArrayList<String>()
        for (elt in l){
            res.add(elt)
        }
        return res
    }

     private fun getThatRoomHasDirection(l: CopyOnWriteArrayList<String>)=listRoom.filter{it.passage == l}.getOrNull(0)


    fun changeRoom(direction:String,room: Room): Room? { //Return Room

        when (room.passage) {
            makeCopyOnWriteArrayList(arrayOf("S","W")) -> {
                when (direction){
                    "W" ->{
                        return getThatRoomHasDirection(makeCopyOnWriteArrayList(arrayOf("S","E")))
                    }
                    "S" ->{
                        return getThatRoomHasDirection(makeCopyOnWriteArrayList(arrayOf("N","S","W")))
                    }
                }
            }
            makeCopyOnWriteArrayList(arrayOf("N","S","W")) -> {
                when (direction) {
                    "N" -> {
                        return getThatRoomHasDirection(makeCopyOnWriteArrayList(arrayOf("S","W")))
                    }
                    "W" -> {
                        return getThatRoomHasDirection(makeCopyOnWriteArrayList(arrayOf("N","S","E")))
                    }
                    "S" -> {
                        return getThatRoomHasDirection(makeCopyOnWriteArrayList(arrayOf("N","W")))
                    }
                }
            }
            makeCopyOnWriteArrayList(arrayOf("N","W")) -> {
                when(direction){
                    "N" ->{
                        return getThatRoomHasDirection(makeCopyOnWriteArrayList(arrayOf("N","W","S")))
                    }
                    "W" ->{
                        return getThatRoomHasDirection(makeCopyOnWriteArrayList(arrayOf("N","E")))
                    }
                }
            }
            makeCopyOnWriteArrayList(arrayOf("N","E")) -> {
                when(direction){
                    "N" ->{
                        return getThatRoomHasDirection(makeCopyOnWriteArrayList(arrayOf("N","S","E")))
                    }
                    "E" ->{
                        return getThatRoomHasDirection(makeCopyOnWriteArrayList(arrayOf("N","W")))
                    }
                }
            }
            makeCopyOnWriteArrayList(arrayOf("N","S","E")) -> {
                when(direction){
                    "N" -> {
                        return getThatRoomHasDirection(makeCopyOnWriteArrayList(arrayOf("S","E")))
                    }
                    "S" ->{
                        return getThatRoomHasDirection(makeCopyOnWriteArrayList(arrayOf("N","E")))
                    }
                    "E" ->{
                        return getThatRoomHasDirection(makeCopyOnWriteArrayList(arrayOf("N","S","W")))
                    }
                }
            }
            makeCopyOnWriteArrayList(arrayOf("S","E")) -> {
                when (direction){
                    "E" -> {
                        return getThatRoomHasDirection(makeCopyOnWriteArrayList(arrayOf("S","W")))
                    }
                    "S" ->{
                        return getThatRoomHasDirection(makeCopyOnWriteArrayList(arrayOf("N","S","E")))
                    }
                }
            }
        }
        return null

    }
    fun validDirection(direction: String): Boolean {
        return when(direction){
            "N","E","S","W" -> {
                true
            }
            else -> {
                false
            }
        }

    }



}