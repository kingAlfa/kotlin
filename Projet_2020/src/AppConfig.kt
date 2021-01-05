package com.projetDonjon

import com.projetDonjon.exception.ExceptionJson
//import com.projetDonjon.extention.get
import com.projetDonjon.extention.post
import com.projetDonjon.model.Monster
import com.projetDonjon.model.Player
import com.projetDonjon.model.Room
import com.projetDonjon.objet.GameObjet
import com.projetDonjon.responseJson.*
import io.ktor.application.*
import io.ktor.content.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import kotlinx.html.*
import java.lang.IllegalArgumentException
import java.util.concurrent.CopyOnWriteArrayList


fun Application.main(){
    install(DefaultHeaders)
    install(ContentNegotiation){
        gson{
            setPrettyPrinting()
        }
    }

    install(StatusPages) {
        status(HttpStatusCode.NotFound) {
            call.respond(TextContent("${it.value} ${it.description}", ContentType.Text.Plain.withCharset(Charsets.UTF_8), it))
        }
        status(HttpStatusCode.BadRequest){
            call.respond(TextContent("${it.value} ${it.description}", ContentType.Text.Plain.withCharset(Charsets.UTF_8),it))
        }
        status(HttpStatusCode.Conflict){
            call.respond(TextContent("${it.value} ${it.description}", ContentType.Text.Plain.withCharset(Charsets.UTF_8),it))
        }
    }
    val l = CopyOnWriteArrayList<String>()
    val le = CopyOnWriteArrayList<String?>()

    val directionR2 = CopyOnWriteArrayList<String>()
    val guidR2      = CopyOnWriteArrayList<String?>()

    l.add("S")
    l.add("W")
    directionR2.add("N")
    directionR2.add("S")
    directionR2.add("W")

    val p=Player()
    val m = Monster()
    val m2 = Monster(20,"Le second monstre dans la salle")

    le.add(m.guid)
    le.add(m2.guid)
    guidR2.add(m.guid)
    guidR2.add(m2.guid)

    val s = Room("Premiere salle",l,le)
    val r2 = Room("Troisieme salle",directionR2,guidR2)

    GameObjet.addListRoom(s)
    GameObjet.addListRoom(r2)

    GameObjet.addMonster(m)
    GameObjet.addMonster(m2)
    GameObjet.putPlayerInRoom(p,s)

    val currentPlayer = GameObjet.getP()
    var currentRoom = currentPlayer?.let { GameObjet.getR(it) }

    routing {

        /**
         * Builds the welcome path
         */

        get("/"){
                call.respondHtml{
                    head{title("Projet Donjon")}
                    body{
                        div{
                            h1{+ "Welcome to Home page "}
                            p{ + "Go to [POST]'/connect' to use the API"}
                            h3{+"Manual"}
                            div{
                                p{+"When you're connected you will find your id like PXXX "}
                                h4{+"Actions : "}
                                p{+"Look : [GET]/PXXX/regarder"}
                                p{+"Examine :[GET]/PXXX/examiner/MXXX where MXXX is the id of the Monster to examine"}
                                p{+"Attack: [POST]/PXXX(or MXXX)/taper/MXXX(or PXXX)"}
                                p{+"Move to : [POST]/PXXX/deplacement/direction where direction in [W,N,S,E]"}
                                h4{+"Run:"}
                                p{+"Use Postman to test api or use your shell command line : curl -X [POST/GET] http://localhost:7000"}
                            }
                        }
                    }
                }
        }
        /**
         *  Builds the connect path
         */
        if (currentPlayer != null) {
            if (currentRoom != null) {
                post("/connect",currentPlayer, currentRoom!!)
            }
        }

        /**
         * Builds the look path
         */
        get("/${currentPlayer?.guid}/regarder"){
            errorAware {
                val room = currentPlayer?.let { it1 -> GameObjet.getR(it1) }
                if (room != null) {
                    call.respond(Room(room.description,room.passage,room.entites))
                }
            }

            }

        /**
         * Builds the examine path
         */
        get("/${currentPlayer?.guid}/examiner/{guid}"){
            errorAware {
                val guid= call.parameters["guid"]?: throw IllegalArgumentException("guid not found")
                val monster = currentPlayer?.let { it1 -> GameObjet.getMonsterInRoom(guid, it1) }
                if(monster != null){
                    call.respond(ExaminerJson(
                        monster.description,
                        monster.type,
                        monster.vie,
                        monster.totalVie,
                    ))
                }
            }

        }

        /**
         * Builds the post path of attack
         */
        post ("/{guidsource}/taper/{guidcible}"){
            val degats = 5
            val guidsource = call.parameters["guidsource"]?: throw IllegalArgumentException("Guide source notValie")
            val guidcible = call.parameters["guidcible"]?: throw  IllegalArgumentException("Guid cible not found")
            val player:Player
            val monster :Monster?

            if (guidsource.startsWith("P") and guidcible.startsWith("M")){
                if (GameObjet.getP()?.guid == guidsource){
                    player= GameObjet.getP()!!
                    monster =  GameObjet.getMonsterInRoom(guidcible,player)

                    if (monster != null) {
                        if(monster.vie >0){
                            player.attacker(monster,degats)

                            val  attaquant = player.guid?.let { it1 -> Attaquant(it1,degats,player.vie) }!!
                            val attaque = monster.guid?.let { it1 -> Attaque(it1, degats, monster.vie) }!!
                            errorAware {
                                call.respond(TaperJson(attaquant,attaque))
                            }
                            if (currentRoom != null) {
                                GameObjet.setUpDatePlayerAndMonsterAfterAttack(player,monster, currentRoom!!)
                            }
                        } else{
                            call.respond(ExceptionJson("Mort","Le montre est deja mort"))
                        }
                    }//End if player !=null
                }//End if vie>=0

            }//End First If

            else if(guidsource.startsWith("M") and guidcible.startsWith("P")){
                if (GameObjet.getP()?.guid == guidcible) {
                    player = GameObjet.getP()!!
                    monster = player.let { it1 -> GameObjet.getMonsterInRoom(guidsource, it1) }!!
                    if (player.vie > 0) {
                        monster.attacker(player, degats)

                        val attaquant = monster.guid?.let { it1 -> Attaquant(it1, degats, monster.vie) }!!
                        val attaque = player.guid?.let { it1 -> Attaque(it1, degats, player.vie) }!!
                        errorAware {
                            call.respond(TaperJson(attaquant, attaque))
                        }
                        if (currentRoom != null) {
                            GameObjet.setUpDatePlayerAndMonsterAfterAttack(player, monster, currentRoom!!)
                        }
                    }
                    else {
                        call.respond(ExceptionJson("Mort","Le joueur est deja mort.Le jeux est terminé"))

                    }
                }

            }//End else if


        }//End post

        /**
         * Builds the path to match the move to post
         */
        post ("/{guid}/deplacement"){
            val guid = call.parameters["guid"]
            val direction = call.parameters["direction"]!!
            if(GameObjet.validDirection(direction)){
                if (currentPlayer != null) {
                    if (guid == currentPlayer.guid){
                        val newRoom = currentRoom?.let { it1 -> GameObjet.changeRoom(direction, it1) }
                        if(newRoom != null){
                            GameObjet.putPlayerInRoom(currentPlayer,newRoom)
                            currentRoom = newRoom
                            call.respond(newRoom)
                        }

                        else{
                            call.respond(ExceptionJson("Mur","No Passage for this direction"))
                        }
                    }
                }
            }
            else {
                call.respond(ExceptionJson("Not Found","Direction Not Valid"))
            }

        }

    } // End routing





}

private suspend fun <R> PipelineContext<*, ApplicationCall>.errorAware(block: suspend () -> R): R? {
    return try {
        block()
    } catch (e: Exception) {
        call.respondText("""{"error":"$e"}"""
            , ContentType.parse("application/json")
            , HttpStatusCode.InternalServerError)
        null
    }
}
