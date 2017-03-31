package com.kurume_nct.friendly_mimic.inventory

import com.kurume_nct.friendly_mimic.entity.EntityMimic
import io.netty.buffer.ByteBuf
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import java.nio.charset.Charset

/**
 * Created by gedorinku on 2017/03/31.
 */
class MimicInventoryChangedMessage : IMessage {

    private val charset = Charset.forName("UTF-8")

    lateinit var uniqueID: String
        private set
    var dirtySlotIndex = 0
        private set

    constructor()

    constructor(uniqueID: String, dirtySlotIndex: Int) {
        this.uniqueID = uniqueID
        this.dirtySlotIndex = dirtySlotIndex
    }

    override fun fromBytes(buf: ByteBuf) {
        dirtySlotIndex = buf.readInt()
        uniqueID = charset
                .decode(buf.readBytes(buf.readableBytes()).nioBuffer())
                .toString()
    }

    override fun toBytes(buf: ByteBuf) {
        buf.writeInt(dirtySlotIndex)
        buf.writeBytes(charset.encode(uniqueID))
    }

    companion object {
        private val handlers = mutableMapOf<
                String,
                MutableList<(MimicInventoryChangedMessage, MessageContext) -> IMessage?>>()

        fun addHandler(entityMimic: EntityMimic, handler: (MimicInventoryChangedMessage, MessageContext) -> IMessage?) {
            val uuid = entityMimic.uniqueID.toString()
            handlers.getOrPut(uuid) { mutableListOf() }
                    .add(handler)
        }

        fun removeHandler(handler: (MimicInventoryChangedMessage, MessageContext) -> IMessage?) =
                handlers.forEach { _, value -> value.remove(handler) }

        fun removeAllHandlersOfEntity(entityMimic: EntityMimic) =
                handlers.remove(entityMimic.uniqueID.toString())
    }

    class MimicInventoryChangedMessageHandler : IMessageHandler<MimicInventoryChangedMessage, IMessage> {

        override fun onMessage(message: MimicInventoryChangedMessage, ctx: MessageContext): IMessage? {
            handlers[message.uniqueID]?.forEach { it(message, ctx) }
            return null
        }
    }
}