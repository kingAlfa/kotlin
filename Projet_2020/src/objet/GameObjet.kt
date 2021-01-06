package com.projetDonjon.objet

import com.projetDonjon.model.Monster
import com.projetDonjon.model.Player
import com.projetDonjon.model.Room
import java.util.concurrent.CopyOnWriteArrayList

/**
 * The Game manager class
 */
object GameObjet {
    /**
     * List of monster in the game
     */
    private  var listMonster = CopyOnWriteArrayList<Monster>()

    /**
     * List of rooms in the game
     */
    private  var listRoom=CopyOnWriteArrayList<Room>()

    /**
     * The associate the player to the game
     */
    private  var player_in_room= HashMap<Player,Room>()

    /**
     * Add monster in the list of monster
     * @param monster the monster
     */
    fun addMonster(monster: Monster){
        this.listMonster.add(monster)
    }

    /**
     * Put the player in the room
     * @param p the player
     * @param r the room
     */
    fun putPlayerInRoom(p:Player,r:Room)
    {
        player_in_room[p] = r
    }

    /**
     * @return Room in which the player are in
     * @param p The player
     */
    fun getR(p:Player)= run { this.player_in_room[p] }

    /**
     * @return Player
     */
    fun getP(): Player? = run {
        val mutList = this.player_in_room.keys
        return mutList.elementAtOrNull(0)
    }

    /**
     * @return Monster
     * @param guid the guid of the monster to get
     * @param p the player in the room
     */
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

    /**
     * Update the player in the room after attach
     * @param player the player
     * @param monster The monster
     * @param currentRoom the current room
     */
    fun setUpDatePlayerAndMonsterAfterAttack(player: Player,monster: Monster,currentRoom: Room){
        val newMonster= monster.guid
        val index = listMonster.indexOf(listMonster.filter{it.guid == newMonster}[0])
        listMonster[index] = monster
        val guidInRoom = currentRoom.entites.indexOf(newMonster)
        currentRoom.entites[guidInRoom] = newMonster
        putPlayerInRoom(player,currentRoom)

    }

    /**
     * Update the list of room
     * @param room The room the Room
     */
    fun addListRoom(room: Room){
        if (!listRoom.contains(room)){
        listRoom.add(room)
        }
    }

    /**
     * Make list
     * @return CopyOnWriteArrayList<String>
     * @param l an array
     */
    private fun makeCopyOnWriteArrayList(l:Array<String>):CopyOnWriteArrayList<String>{
        val res = CopyOnWriteArrayList<String>()
        for (elt in l){
            res.add(elt)
        }
        return res
    }

    /**
     * Get a room that has a given direction
     * @return Room
     * @param l a list of direction
     */
     private fun getThatRoomHasDirection(l: CopyOnWriteArrayList<String>)=listRoom.filter{it.passage == l}.getOrNull(0)

    /**
     * Change the room by the given direction
     * @return Room the new room
     * @param direction the given direction
     * @param room The current room
     */
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

    /**
     * Test if the given direction is valid
     * @return Boolean
     * @param direction the given direction
     */
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