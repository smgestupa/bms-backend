package com.backend.bms.controllers.auth

import com.backend.bms.models.role.ERole
import com.backend.bms.models.role.Role
import com.backend.bms.models.user.User
import com.backend.bms.models.user.UserSummary
import com.backend.bms.payloads.requests.LoginRequest
import com.backend.bms.payloads.requests.RegisterRequest
import com.backend.bms.payloads.responses.JwtResponse
import com.backend.bms.payloads.responses.MessageResponse
import com.backend.bms.repositories.role.RoleRepository
import com.backend.bms.repositories.user.UserRepository
import com.backend.bms.security.jwt.JwtUtils
import com.backend.bms.security.services.UserDetailsImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.lang.RuntimeException
import java.util.stream.Collectors

@CrossOrigin( origins = [ "*" ], maxAge = 3600 )
@RestController
@RequestMapping( "/auth" )
class AuthController @Autowired constructor(
    private val authenticationManager: AuthenticationManager,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtils: JwtUtils,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository
) {

    @PostMapping(
        value = [ "/login" ],
        consumes = [ "application/json" ],
        produces = [ "application/json" ]
    )
    @Throws( Exception::class )
    fun loginUser( @RequestBody loginRequest: LoginRequest ): ResponseEntity<Any> {
        if ( !userRepository.existsByUserCodeAndUsername( loginRequest.userCode, loginRequest.username ) )
            return ResponseEntity( MessageResponse( "User does not exist" ), HttpStatus.UNAUTHORIZED );

        var user: UserSummary;
        userRepository.findById( loginRequest.userCode ).get().let {
            user = UserSummary( it.userCode, it.photo, it.firstName, it.lastName );
            user.bio = it.bio;
        };

        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken( loginRequest.username, loginRequest.password )
        );

        SecurityContextHolder.getContext().authentication = authentication;
        val jwt: String = jwtUtils.generateJwtToken( authentication );

        val userDetails: UserDetailsImpl = authentication.principal as UserDetailsImpl;
        val roles: List<String> = userDetails.authorities.stream()
            .map { item -> item.authority }
            .collect( Collectors.toList() );

        return ResponseEntity(
            JwtResponse(
                jwt,
                userDetails.getUserCode(),
                user.photo,
                user.firstName,
                user.lastName,
                user.bio,
                userDetails.username,
                roles
            ), HttpStatus.OK
        );
    }

    @PostMapping(
        value = [ "/register" ],
        consumes = [ "application/json" ],
        produces = [ "application/json" ]
    )
    @Throws( Exception::class )
    fun registerUser( @RequestBody registerRequest: RegisterRequest ) : ResponseEntity<Any> {
        if ( userRepository.existsByUserCode( registerRequest.userCode ) )
            return ResponseEntity( MessageResponse( "User code already been registered" ), HttpStatus.CONFLICT );

        if ( userRepository.existsByUsername( registerRequest.username ) )
            return ResponseEntity( MessageResponse( "Username already been registered" ), HttpStatus.CONFLICT );

        val strRoles: Set<String> = registerRequest.roles;
        val roles: MutableSet<Role> = mutableSetOf();

        strRoles.forEach { role -> run {
            when ( role ) {
                "teacher" -> {
                    val teacherRole: Role = roleRepository.findByLabel( ERole.ROLE_TEACHER )
                        .orElseThrow { RuntimeException( "Error: Teacher role wasn't found" ) }
                    roles.add( teacherRole );
                }
                "student" -> {
                    val studentRole: Role = roleRepository.findByLabel( ERole.ROLE_STUDENT )
                        .orElseThrow { RuntimeException( "Error: Student role wasn't found" ) }
                    roles.add( studentRole );
                }
                else -> {
                    val userRole: Role = roleRepository.findByLabel( ERole.ROLE_USER )
                        .orElseThrow { RuntimeException( "Error: User role wasn't found" ) }
                    roles.add( userRole );
                }
            }
        } };

        userRepository.save(
            User(
                registerRequest.userCode,
                registerRequest.firstName,
                registerRequest.lastName,
                registerRequest.username,
                passwordEncoder.encode( registerRequest.password ),
                roles.toSet()
            )
        );
        return ResponseEntity( MessageResponse( "User successfully registered" ), HttpStatus.CREATED );
    }
}