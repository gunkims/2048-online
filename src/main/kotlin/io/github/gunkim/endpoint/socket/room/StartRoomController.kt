package io.github.gunkim.endpoint.socket.room

import io.github.gunkim.application.room.StartRoom
import io.github.gunkim.endpoint.common.id
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class StartRoomController(
    private val startRoom: StartRoom,
) {
    @PutMapping("/rooms/{roomId}/start")
    fun start(
        user: OAuth2AuthenticationToken,
        @PathVariable roomId: UUID,
    ) {
        startRoom.start(roomId, user.id)
    }
}
