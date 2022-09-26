package com.backend.bms.payloads.responses

class JwtResponse constructor(
    token: String,
    userCode: String,
    photo: ByteArray?,
    firstName: String,
    lastName: String,
    bio: String,
    username: String,
    roles: List<String>
) {

    val token: String;
    val type: String = "Bearer";
    val userCode: String;
    val photo: ByteArray?;
    val firstName: String;
    val lastName: String;
    val bio: String;
    val username: String;
    val roles: List<String>;

    init {
        this.token = token;
        this.userCode = userCode;
        this.photo = photo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
        this.username = username;
        this.roles = roles;
    }
}