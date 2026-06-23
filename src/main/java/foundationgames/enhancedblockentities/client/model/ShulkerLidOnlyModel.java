package foundationgames.enhancedblockentities.client.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class ShulkerLidOnlyModel extends Model<Float> {
    private static final Map<Model<Float>, ShulkerLidOnlyModel> CACHE = new WeakHashMap<>();

    private final Model<Float> delegate;

    @SuppressWarnings("null")
    private ShulkerLidOnlyModel(Model<Float> delegate) {
        super(new ModelPart(List.of(), Map.of("lid", delegate.root().getChild("lid"))), delegate.renderType());
        this.delegate = delegate;
    }

    public static ShulkerLidOnlyModel of(Model<Float> delegate) {
        return CACHE.computeIfAbsent(delegate, ShulkerLidOnlyModel::new);
    }

    @Override
    public void setupAnim(Float openAmount) {
        this.delegate.setupAnim(openAmount);
    }
}
