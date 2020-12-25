package org.valkyrienskies.addon.control;

import org.valkyrienskies.addon.control.capability.LastNodeCapabilityProvider;
import org.valkyrienskies.addon.control.item.ItemBaseWire;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ControlEventsCommon {

    @SubscribeEvent
    public void onAttachCapabilityEventItem(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() instanceof ItemBaseWire) {
            event.addCapability(new ResourceLocation(ValkyrienSkiesControl.MOD_ID, "LastRelay"),
                new LastNodeCapabilityProvider());
        }
    }
    
    @SubscribeEvent()
	public void PlayerJoin(EntityJoinWorldEvent event) {
		if(!(event.getEntity() instanceof EntityPlayer))
			return;
		
		EntityPlayer player = (EntityPlayer) event.getEntity();
		if(!event.getEntity().world.isRemote) 
			player.sendMessage(new TextComponentString("Mod by One Piece : GoldenAge - Discord: https://discord.gg/VJpUWyg"));
	}
}
