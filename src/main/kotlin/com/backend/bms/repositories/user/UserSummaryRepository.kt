package com.backend.bms.repositories.user

import com.backend.bms.models.user.UserSummary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserSummaryRepository : JpaRepository<UserSummary, Int> {

    fun existsByUserCode( userCode: String ): Boolean;
    fun findByUserCode( userCode: String ): Optional<UserSummary>;
}