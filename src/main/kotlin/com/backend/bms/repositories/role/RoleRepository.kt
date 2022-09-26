package com.backend.bms.repositories.role

import com.backend.bms.models.role.ERole
import com.backend.bms.models.role.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoleRepository : JpaRepository<Role, Int> {

    fun findByLabel( label: ERole ): Optional<Role>;
}