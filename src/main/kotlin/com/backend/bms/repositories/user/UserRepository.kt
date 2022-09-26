package com.backend.bms.repositories.user

import com.backend.bms.models.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, String>{

    fun existsByUserCode( userCode: String ): Boolean;
    fun existsByUsername( username: String ): Boolean;
    fun findByUsername( username: String ): Optional<User>;
    fun existsByUserCodeAndUsername( userCode: String, username: String ): Boolean;
}