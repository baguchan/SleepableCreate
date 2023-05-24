package baguchan.sleepable_create.mixin;

import baguchan.sleepable_create.IHasStaticRespawn;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Inject(method = "respawn", at = @At("HEAD"))
    public void respawn(ServerPlayer p_11237_, boolean p_11238_, CallbackInfoReturnable<ServerPlayer> callbackInfoReturnable) {
        if (p_11237_ instanceof IHasStaticRespawn staticRespawn) {
            if (staticRespawn.getStaticRespawn() != null && staticRespawn.getStaticLocalPos() != null) {
                StructureTemplate.StructureBlockInfo info = staticRespawn.getStaticRespawn().getContraption().getBlocks()
                        .get(staticRespawn.getStaticLocalPos());
                if (info.state.is(Blocks.RESPAWN_ANCHOR)) {
                    info.state.setValue(RespawnAnchorBlock.CHARGE, Integer.valueOf(info.state.getValue(RespawnAnchorBlock.CHARGE) - 1));
                    if (info.state.getValue(RespawnAnchorBlock.CHARGE) <= 0) {
                        staticRespawn.setStaticRespawn(null);
                        staticRespawn.setStaticLocalPos(null);
                    }
                }
            } else {
                staticRespawn.setStaticLocalPos(null);
            }
        }

    }
}
