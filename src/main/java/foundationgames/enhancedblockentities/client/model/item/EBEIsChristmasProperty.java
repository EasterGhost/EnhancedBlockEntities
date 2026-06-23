package foundationgames.enhancedblockentities.client.model.item;

import com.mojang.serialization.MapCodec;
import foundationgames.enhancedblockentities.util.DateUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public record EBEIsChristmasProperty() implements ConditionalItemModelProperty {
    @SuppressWarnings("null")
    public static final MapCodec<EBEIsChristmasProperty> CODEC = MapCodec.unit(new EBEIsChristmasProperty());

    @SuppressWarnings("null")
    @Override
    public boolean get(ItemStack stack, ClientLevel level, LivingEntity entity, int seed, ItemDisplayContext displayContext) {
        return DateUtil.isChristmas();
    }

    @SuppressWarnings("null")
    @Override
    public MapCodec<EBEIsChristmasProperty> type() {
        return CODEC;
    }
}
