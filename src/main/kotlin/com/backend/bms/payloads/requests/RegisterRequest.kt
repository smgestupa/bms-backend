package com.backend.bms.payloads.requests

class RegisterRequest {

    lateinit var userCode: String;
    lateinit var firstName: String;
    lateinit var lastName: String;
    lateinit var username: String;
    lateinit var password: String;
    lateinit var roles: Set<String>;
}