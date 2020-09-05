package com.mowmaster.pedestals.item.pedestalUpgrades;


import com.mowmaster.pedestals.tiles.PedestalTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.IItemHandler;

import java.util.stream.IntStream;

import static com.mowmaster.pedestals.pedestals.PEDESTALS_TAB;
import static com.mowmaster.pedestals.references.Reference.MODID;

public class ItemUpgradeFilterTag extends ItemUpgradeBaseFilter
{
    public ItemUpgradeFilterTag(Properties builder) {super(builder.group(PEDESTALS_TAB));}

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal, BlockPos pedestalPos)
    {

    }

    @Override
    public boolean canAcceptItem(World world, BlockPos posPedestal, ItemStack itemStackIn)
    {
        boolean returner = false;
        BlockPos posInventory = getPosOfBlockBelow(world, posPedestal, 1);

        LazyOptional<IItemHandler> cap = findItemHandlerAtPos(world,posInventory,getPedestalFacing(world, posPedestal),true);
            if(cap.isPresent())
            {
                IItemHandler handler = cap.orElse(null);
                if(handler != null)
                {
                    int range = handler.getSlots();


                    ItemStack itemFromInv = ItemStack.EMPTY;
                    itemFromInv = IntStream.range(0,range)//Int Range
                            .mapToObj((handler)::getStackInSlot)//Function being applied to each interval
                            .filter(itemStack -> itemStackIn.getItem().getTags().toString().contains(itemStack.getDisplayName().getString()))
                            .findFirst().orElse(ItemStack.EMPTY);

                    if(!itemFromInv.isEmpty())
                    {
                        returner = true;
                    }
                }
            }

        return returner;
    }

    public void upgradeAction(World world, BlockPos posOfPedestal, ItemStack coinInPedestal)
    {

    }

    public static final Item TAG = new ItemUpgradeFilterTag(new Properties().maxStackSize(64).group(PEDESTALS_TAB)).setRegistryName(new ResourceLocation(MODID, "coin/filtertag"));

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(TAG);
    }



}
