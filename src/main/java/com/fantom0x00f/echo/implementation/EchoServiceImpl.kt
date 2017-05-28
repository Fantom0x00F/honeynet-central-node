package com.fantom0x00f.echo.implementation

import com.fantom0x00f.echo.EchoService
import org.springframework.stereotype.Service

@Service
open class EchoServiceImpl : EchoService {

    override fun getResponse(message: String): String = message

}