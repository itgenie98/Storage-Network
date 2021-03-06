package com.lothrazar.storagenetwork.block.collection;

import com.lothrazar.storagenetwork.block.BaseBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockCollection extends BaseBlock {

  public BlockCollection() {
    super(Material.IRON, "collector");
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileCollection();
  }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult result) {
    if (!world.isRemote) {
      TileEntity tile = world.getTileEntity(pos);
      if (tile instanceof INamedContainerProvider) {
        ServerPlayerEntity player = (ServerPlayerEntity) playerIn;
        player.connection.sendPacket(tile.getUpdatePacket());
        //
        NetworkHooks.openGui(player, (INamedContainerProvider) tile, tile.getPos());
      }
      else {
        throw new IllegalStateException("Our named container provider is missing!" + tile);
      }
    }
    return ActionResultType.SUCCESS;
  }
}
