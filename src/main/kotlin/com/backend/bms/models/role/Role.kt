package com.backend.bms.models.role

import javax.persistence.*

@Entity
@Table( name = "roles" )
class Role {

    @Id
    @Column( name = "role_id" )
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    val roleID: Int = 0;

    @Enumerated( EnumType.STRING )
    @Column(
        length = 16,
        nullable = false
    )
    lateinit var label: ERole;
}