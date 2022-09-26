package com.backend.bms.security.services

import com.backend.bms.models.user.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import java.util.stream.Collectors

class UserDetailsImpl(
    userCode: String,
    username: String,
    password: String,
    authorities: List<GrantedAuthority>
): UserDetails {

    private val userCode: String;
    private val username: String;
    private val password: String;
    private val authorities: List<GrantedAuthority>;

    init {
        this.userCode = userCode;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    companion object {
        fun build( user: User ): UserDetailsImpl {
            val authorities: List<GrantedAuthority> = user.roles.stream()
                .map { role -> SimpleGrantedAuthority( role.label.name ) }
                .collect( Collectors.toList() );

            return UserDetailsImpl(
                user.userCode,
                user.username,
                user.password,
                authorities
            )
        }
    }

    override fun getAuthorities(): List<GrantedAuthority> {
        return authorities;
    }

    fun getUserCode(): String {
        return userCode;
    }

    override fun getPassword(): String {
        return password;
    }

    override fun getUsername(): String {
        return username;
    }

    override fun isAccountNonExpired(): Boolean {
        return true;
    }

    override fun isAccountNonLocked(): Boolean {
        return true;
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true;
    }

    override fun isEnabled(): Boolean {
        return true;
    }

    override fun equals( other: Any? ): Boolean {
        if ( this === other ) return true;
        else if ( other == null || javaClass != other::class.java ) return false;

        val user: UserDetailsImpl = other as UserDetailsImpl;
        return Objects.equals( userCode, user.userCode );
    }

}