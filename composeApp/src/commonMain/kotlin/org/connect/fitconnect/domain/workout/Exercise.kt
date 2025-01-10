package org.connect.fitconnect.domain.workout

data class Exercise(
    val id: Long,
    val name: String,
    val muscle_group_id: Long,
    val is_body_weight: Long,
)