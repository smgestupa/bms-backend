package com.backend.bms.models.user

import com.backend.bms.models.role.Role
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table( name = "users" )
class User constructor(
    userCode: String,
    firstName: String,
    lastName: String,
    username: String,
    password: String,
    roles: Set<Role>
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

    @Column(
        length = 16,
        nullable = false
    )
    var username: String;

    @Column(
        length = 60,
        nullable = false
    )
    var password: String;

    @ManyToMany( fetch = FetchType.LAZY )
    @JoinTable(
        name = "user_roles",
        joinColumns = [ JoinColumn( name = "user_code" ) ],
        inverseJoinColumns = [ JoinColumn( name = "role_id" ) ]
    )
    var roles: Set<Role> = HashSet();

    init {
        this.userCode = userCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
}