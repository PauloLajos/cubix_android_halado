package com.cubixedu.motionlayouttest.ui

import java.util.UUID

data class User(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val photo: String
)