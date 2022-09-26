package com.backend.bms.controllers.user

import com.backend.bms.models.user.UserSummary
import com.backend.bms.payloads.requests.UpdateRequest
import com.backend.bms.payloads.responses.MessageResponse
import com.backend.bms.repositories.user.UserSummaryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin( origins = [ "*" ], maxAge = 3600 )
@RestController
@RequestMapping( "/user" )
class UserController @Autowired constructor(
    private val userSummaryRepository: UserSummaryRepository
) {

    @PreAuthorize( "hasRole('USER')" )
    @PostMapping(
        value = [ "/update" ],
        consumes = [ "application/json" ],
        produces = [ "application/json" ]
    )
    @Throws( Exception::class )
    fun updateUser( @RequestBody updateRequest: UpdateRequest ): ResponseEntity<Any> {
        if ( !userSummaryRepository.existsByUserCode( updateRequest.userCode ) )
            return ResponseEntity( MessageResponse( "User does not exist" ), HttpStatus.CONFLICT );

        var updated: UserSummary;
        userSummaryRepository.findByUserCode( updateRequest.userCode ).get().let {
            var photo: ByteArray? = it.photo;
            updateRequest.photo?.let { p -> photo = Base64.getDecoder().decode( p ) };

            updated = UserSummary( it.userCode, photo, it.firstName, it.lastName );
            updateRequest.bio?.let { b -> updated.bio = b };

            userSummaryRepository.save( updated );
        }

        return ResponseEntity( MessageResponse( "Data successfully updated" ), HttpStatus.OK );
    }
}