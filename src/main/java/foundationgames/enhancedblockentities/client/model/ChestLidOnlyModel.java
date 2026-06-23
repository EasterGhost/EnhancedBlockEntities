package foundationgames.enhancedblockentities.client.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.object.chest.ChestModel;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class ChestLidOnlyModel extends Model<Float> {
    private static final Map<ChestModel, ChestLidOnlyModel> CACHE = new WeakHashMap<>();

    private final ChestModel delegate;

    @SuppressWarnings("null")
    private ChestLidOnlyModel(ChestModel delegate) {
        super(new ModelPart(List.of(), Map.of(
                "lid", delegate.root().getChild("lid"),
                "lock", delegate.root().getChild("lock")
        )), delegate.renderType());
        this.delegate = delegate;
    }

    public static ChestLidOnlyModel of(ChestModel delegate) {
        return CACHE.computeIfAbsent(delegate, ChestLidOnlyModel::new);
    }

    @SuppressWarnings("null")
    @Override
    public void setupAnim(Float openAmount) {
        this.delegate.setupAnim(openAmount);
    }
}
