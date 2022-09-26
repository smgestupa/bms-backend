package com.backend.bms.models.user

import com.backend.bms.models.role.Role
import javax.persistence.*

@Entity
@Table( name = "users" )
class UserSummary(
    userCode: String,
    photo: ByteArray?,
    firstName: String,
    lastName: String,
) {

    @Id
    @Column(
        name = "user_code",
        length = 7
    )
    var userCode: String;

    @Column
    var photo: ByteArray? = null;

    @Column( name = "first_name" )
    var firstName: String;

    @Column( name = "last_name" )
    var lastName: String;

    @Column
    var bio: String = "This user does not have a bio of themselves.";

    init {
        this.userCode = userCode;
        this.photo = photo;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}