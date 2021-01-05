package com.projetDonjon

import io.ktor.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

const val portArgName= "--server.port"
const val defaultport = 7000

fun main(args:Array<String>){
    val portCOnfigured = args.isNotEmpty() && args[0].startsWith(portArgName)

    val port = if(portCOnfigured){
        args[0].split("=").last().trim().toInt()
    }else defaultport

    embeddedServer(Netty, port, module = Application::main).start(wait = true)
}