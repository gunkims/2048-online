package io.github.gunkim.domain

import io.github.gunkim.domain.vo.MoveType
import java.util.UUID

data class Gamer(
    val id: UUID = UUID.randomUUID(),
    val user: User,
    val board: Board,
    val isHost: Boolean = false,
    val isReady: Boolean = false,
) {
    val score: Int
        get() = board.score

    fun start() = Gamer(id, user, Board.start(), isHost)
    fun move(moveType: MoveType) = Gamer(id, user, moveType(board), isHost)

    fun hasPlayer(user: User) = this.user == user

    fun isSameUserId(userId: UUID) = user.isSameId(userId)

    fun host() = Gamer(id, user, board, true)

    fun ready() = Gamer(id, user, board, isHost, !isReady)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Gamer

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
