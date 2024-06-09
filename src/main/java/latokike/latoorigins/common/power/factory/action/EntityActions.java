package latokike.latoorigins.common.power.factory.action;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import latokike.latoorigins.common.LatoOrigins;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class EntityActions {

    @SuppressWarnings("unchecked")
    public static void init() {
        register(new ActionFactory<>(LatoOrigins.identifier("sound"), new SerializableData()
            .add("sound", SerializableDataTypes.STRING)
            .add("volume", SerializableDataTypes.FLOAT, 1F)
            .add("pitch", SerializableDataTypes.FLOAT, 1F),
            (data, entity) -> {
                SoundCategory category;
                if(entity instanceof PlayerEntity) {
                    category = SoundCategory.PLAYERS;
                } else
                if(entity instanceof  HostileEntity) {
                    category = SoundCategory.HOSTILE;
                } else {
                    category = SoundCategory.NEUTRAL;
                }
                entity.getWorld().playSound(null, (entity).getX(), (entity).getY(), (entity).getZ(), SoundEvent.of(new Identifier(data.getString("sound"))),
                	category, data.getFloat("volume"), data.getFloat("pitch"));
            }));
        register(new ActionFactory<>(LatoOrigins.identifier("give_item"), new SerializableData()
                .add("item", SerializableDataTypes.ITEM_STACK),
                (data, entity) -> {
                    if (!entity.getWorld().isClient()) {
                        ItemStack item = (ItemStack)data.get("item");
                        item = item.copy();
                        if(entity instanceof PlayerEntity player) {
                            player.getInventory().offerOrDrop(item);
                        } else {
                            entity.getWorld().spawnEntity(new ItemEntity(entity.getWorld(), entity.getX(), entity.getY(), entity.getZ(), item));
                        }
                    }
                }));
    }
    private static void register(ActionFactory<Entity> actionFactory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
