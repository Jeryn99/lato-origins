package latokike.latoorigins.common.power;

import io.github.apace100.apoli.power.ActiveCooldownPower;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.util.HudRender;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.Explosion.DestructionType;

public class ExplodePower extends ActiveCooldownPower {

	float explosionStrength;
	boolean shouldBreakBlocks;
	float selfDamage;
	boolean ignitable;

	public ExplodePower(PowerType<?> type, LivingEntity player, int cooldownDuration, HudRender hudRender,
						float explosionStrength, boolean shouldBreakBlocks, float selfDamage, boolean ignitable) {
		super(type, player, cooldownDuration, hudRender, null);
		
		this.explosionStrength = explosionStrength;
		this.shouldBreakBlocks = shouldBreakBlocks;
		this.selfDamage = selfDamage;
		this.ignitable = ignitable;
	}
	
	@Override
	public void onUse() {
		if (!entity.getWorld().isClient) {
			if (canUse()) {
				explode();
				use();
			}
		}
	}
	
	private void explode() {
		World.ExplosionSourceType type = shouldBreakBlocks ? World.ExplosionSourceType.BLOCK : World.ExplosionSourceType.NONE;

		Explosion explosion = entity.getWorld().createExplosion(entity, entity.getX(), entity.getY(), entity.getZ(), explosionStrength, type);
		DamageSource source = entity.getWorld().getDamageSources().explosion(explosion);
		entity.damage(source, selfDamage);
	}
	
	public boolean isIgnitable() {
		return ignitable;
	}
}
