package navy_master.hachimi.magic.spells;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MidnightCitySustainedSpellInstance implements SustainedSpell {
    private static final int CHARGE_TICKS = 50;
    private static final int TOTAL_DURATION = 200;
    private final Player player;
    private final UUID playerId;
    private final ServerLevel level;
    private int tickCount = 0;
    private List<UUID> targetIds = new ArrayList<>();

    public MidnightCitySustainedSpellInstance(Player player, ServerLevel level) {
        this.player = player;
        this.playerId = player.getUUID();
        this.level=level;
    }

    public void start() {
        player.addEffect(new MobEffectInstance(
                MobEffects.MOVEMENT_SPEED,
                TOTAL_DURATION,
                1 // 速度II
        ));
        updateTargets(level,1);

        SpellManager.addSpell(uuid,this);
    }

    public boolean tick() {
        Player currentPlayer = level.getPlayerByUUID(playerId);
        if (currentPlayer == null || !currentPlayer.isAlive()) return true;
        tickCount++;

        // 充能阶段（显示目标标记和玩家尾迹）
        if (tickCount <= CHARGE_TICKS) {
            // 营造充能过程中逐个选择目标感觉
            if (tickCount <= 15) {
                updateTargets(level,1);
            }else if(tickCount <= 35){
                updateTargets(level,2);
            }else{
                updateTargets(level,3);
            }

            showTargetParticles(level);
        }
        // 爆炸时刻
        else if (tickCount == CHARGE_TICKS + 1) {
            explode(level);
        }

        // 显示玩家尾迹
        showPlayerTrail(level);
        int currentTick=level.getServer().getTickCount();

        // 检查是否达到总持续时间
        return tickCount >= TOTAL_DURATION;
    }

    private void updateTargets(ServerLevel level,int number) {
        // 获取玩家位置和附近生物
        Vec3 playerPos = player.position();
        AABB searchArea = new AABB(playerPos, playerPos).inflate(15.0);
        List<Entity> nearbyEntities = level.getEntities(player, searchArea,
                entity -> entity instanceof LivingEntity && entity != player);

        // 按距离排序并选择最近的三个生物
        nearbyEntities.sort(Comparator.comparingDouble(e -> e.distanceToSqr(player)));
        targetIds = nearbyEntities.stream()
                .limit(number)
                .map(Entity::getUUID)
                .collect(Collectors.toList());
    }

    private void showTargetParticles(ServerLevel level) {
        int colorIndex = 0;
        for (UUID targetId : targetIds) {
            Entity target = level.getEntity(targetId);
            if (target != null) {
                int color = colorIndex == 0 ? 0xFF00FF : // 洋红
                        colorIndex == 1 ? 0x9400D3 : // 紫色
                                0x00BFFF;  // 亮蓝
                colorIndex++;
                float r = ((color >> 16) & 0xFF) / 255.0f;
                float g = ((color >> 8) & 0xFF) / 255.0f;
                float b = (color & 0xFF) / 255.0f;
                Vector3f colorVec = new Vector3f(r, g, b);

                for (int i = 0; i < 3; i++) {
                    double offsetX = (level.random.nextDouble() - 0.5) * 1.5;
                    double offsetY = (level.random.nextDouble() - 0.5) * 1.5 + target.getBbHeight()/2;
                    double offsetZ = (level.random.nextDouble() - 0.5) * 1.5;

                    level.sendParticles(
                            new DustParticleOptions(colorVec, 1.0f),
                            target.getX() + offsetX,
                            target.getY() + offsetY,
                            target.getZ() + offsetZ,
                            1, 0, 0, 0, 0
                    );
                }
            }
        }
    }

    private void showPlayerTrail(ServerLevel level) {
            double angle = level.random.nextDouble() * Math.PI * 2;
            double radius = level.random.nextDouble();
            double offsetX = Math.cos(angle) * radius;
            double offsetZ = Math.sin(angle) * radius;

            level.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                    player.getX() + offsetX,
                    player.getY() + player.getBbHeight() * 0.5,
                    player.getZ() + offsetZ,
                    1, 0, 0.05, 0, 0);
    }

    private void explode(ServerLevel level) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS,
                2.0f, 0.8f);

        for (UUID targetId : targetIds) {
            Entity target = level.getEntity(targetId);
            if (target != null) {
                level.explode(
                        null,
                        target.getX(),
                        target.getY(),
                        target.getZ(),
                        3.0f,
                        Level.ExplosionInteraction.NONE
                );

                for (int i = 0; i < 30; i++) {
                    double offsetX = (level.random.nextDouble() - 0.5) * 2.0;
                    double offsetY = (level.random.nextDouble() - 0.5) * 2.0;
                    double offsetZ = (level.random.nextDouble() - 0.5) * 2.0;

                    int colorIndex = level.random.nextInt(3);
                    int color = colorIndex == 0 ? 0xFF00FF :
                            colorIndex == 1 ? 0x9400D3 :
                                    0x00BFFF;

                    float r = ((color >> 16) & 0xFF) / 255.0f;
                    float g = ((color >> 8) & 0xFF) / 255.0f;
                    float b = (color & 0xFF) / 255.0f;
                    Vector3f colorVec = new Vector3f(r, g, b);

                    level.sendParticles(
                            new DustParticleOptions(colorVec, 1.0f),
                            target.getX() + offsetX,
                            target.getY() + target.getBbHeight()/2 + offsetY,
                            target.getZ() + offsetZ,
                            1, 0, 0, 0, 0
                    );
                }
            }
        }
    }
}