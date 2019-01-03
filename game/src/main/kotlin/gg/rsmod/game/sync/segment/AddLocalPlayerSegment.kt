package gg.rsmod.game.sync.segment

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.sync.SynchronizationSegment
import gg.rsmod.net.packet.GamePacketBuilder

/**
 * @author Tom <rspsmods@gmail.com>
 */
class AddLocalPlayerSegment(val player: Player, val other: Player,
                            private val index: Int, private val tileHash: Int) : SynchronizationSegment {

    override fun encode(buf: GamePacketBuilder) {
        val updateLocation = PlayerLocationHashSegment(player.otherPlayerTiles[index], tileHash)

        /**
         * Signal to the client that the player needs to be decoded.
         */
        buf.putBits(1, 1)
        /**
         * Signal to the client that the non-local player needs to be added
         * as a local player.
         */
        buf.putBits(2, 0)
        /**
         * Signal to the client that the player needs to have their location
         * decoded.
         */
        buf.putBits(1, 1)
        /**
         * Encode the player's location hash.
         */
        updateLocation.encode(buf)

        buf.putBits(13, other.tile.x and 0x1FFF)
        buf.putBits(13, other.tile.z and 0x1FFF)
        buf.putBits(1, 1) // Requires block update
    }
}