package com.kurume_nct.friendly_mimic;

import com.kurume_nct.friendly_mimic.inventory.MimicInventoryChangedMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by gedorinku on 2017/03/31.
 */
public class ChannelInitializer {

    public static void initChannel(SimpleNetworkWrapper channel) {
        int id = 0;
        channel.registerMessage(
                MimicInventoryChangedMessage.MimicInventoryChangedMessageHandler.class,
                MimicInventoryChangedMessage.class,
                id++,
                Side.CLIENT);
        channel.registerMessage(
                MimicInventoryChangedMessage.MimicInventoryChangedMessageHandler.class,
                MimicInventoryChangedMessage.class,
                id++,
                Side.SERVER);
    }
}
