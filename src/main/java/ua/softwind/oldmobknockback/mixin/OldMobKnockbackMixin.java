package ua.softwind.oldmobknockback.mixin;

import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class OldMobKnockbackMixin extends Entity implements Attackable {
    public OldMobKnockbackMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;takeKnockback(DDD)V"))
    public void takeKnockback(LivingEntity instance, double strength, double x, double z) {
        LivingEntity thisObject = (LivingEntity) (Object) this;
        strength *= 1.0 - thisObject.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
        if (!(strength <= 0.0)) {
            this.velocityDirty = true;
            Vec3d vec3d = thisObject.getVelocity();
            Vec3d vec3d2 = (new Vec3d(x, 0.0, z)).normalize().multiply(strength);
            this.setVelocity(vec3d.x / 2.0 - vec3d2.x, 0.45D, vec3d.z / 2.0 - vec3d2.z);
        }
    }
}