package latokike.latoorigins.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import latokike.latoorigins.common.power.SpikedPower;
import latokike.latoorigins.common.registry.LOPowers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	@Shadow public abstract boolean isSpectator();

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	// RIDEABLE_ENTITY
	@Inject(method = "interact", at = @At(value = "HEAD"), cancellable = true)
	public void interact(Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		if (entity instanceof PlayerEntity && LOPowers.RIDEABLE_CREATURE.isActive(entity)) {
			if (!this.hasPassengers() && !((PlayerEntity)(Object)this).shouldCancelInteraction()) {
				if (!this.getWorld().isClient) {
					(this).startRiding(entity);
				}
				cir.setReturnValue(ActionResult.success(this.getWorld().isClient));
			} else {
				cir.setReturnValue(ActionResult.FAIL);
			}
		}
	}
	// SPIKED
	@Inject(method = "damage", at = @At(value = "HEAD"))
	public void damage$LatoOrigins(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		List<SpikedPower> spikedPowers = PowerHolderComponent.getPowers(((PlayerEntity)(Object)this), SpikedPower.class);
		if (source.getSource() instanceof LivingEntity livingEntity && !source.isOf(DamageTypes.MAGIC) && !source.isOf(DamageTypes.EXPLOSION) && !source.isOf(DamageTypes.PLAYER_EXPLOSION) && !spikedPowers.isEmpty()) {
			int damage = spikedPowers.stream().map(SpikedPower::getSpikeDamage).reduce(Integer::sum).get();
			System.out.println(damage);
			if ((this).getRandom().nextFloat() <= 0.75) {
				DamageSource thorns = livingEntity.getWorld().getDamageSources().thorns((PlayerEntity) (Object) this);
				source.getSource().damage(thorns, damage);
			}
		}
	}
}

// Original Code by UltrusBot