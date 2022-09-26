package com.backend.bms.payloads.responses

class MessageResponse constructor(
    message: String
) {

    val message: String;

    init {
        this.message = message;
    }
}