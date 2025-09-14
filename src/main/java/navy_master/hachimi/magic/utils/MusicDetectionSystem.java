package navy_master.hachimi.magic.utils;

import navy_master.hachimi.magic.HachimiMagic;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = HachimiMagic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MusicDetectionSystem {
    // 存储所有活跃音乐源的位置和结束时间
    private static final Map<BlockPos, Long> activeMusicSources = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void onPlaySound(PlaySoundEvent event) {
        SoundInstance sound = event.getSound();

        // 只处理唱片类别的音乐
        if (sound.getSource() != SoundSource.RECORDS) {
            return;
        }

        // 获取位置信息
        double x = sound.getX();
        double y = sound.getY();
        double z = sound.getZ();
        BlockPos blockPos = new BlockPos((int)x, (int)y, (int)z);

        // 获取音乐时长
        int duration = getMusicDuration(sound.getLocation());
        if (duration > 0) {
            // 添加到活跃音乐源
            activeMusicSources.put(blockPos, System.currentTimeMillis() + duration * 50); // 转换为毫秒
        }
    }

    private static int getMusicDuration(ResourceLocation soundId) {
        // 尝试从原版唱片获取时长
        for (RecordItem record : ForgeRegistries.ITEMS.getValues().stream()
                .filter(item -> item instanceof RecordItem)
                .map(item -> (RecordItem) item)
                .toList()) {

            if (record.getSound().getLocation().equals(soundId)) {
                return record.getLengthInTicks();
            }
        }

        // 默认时长（5分钟）
        return 20 * 60 * 5;
    }

    // 检查位置附近是否有活跃音乐源
    public static boolean isMusicPlayingNear(BlockPos center, int radius) {
        long currentTime = System.currentTimeMillis();
        AABB area = new AABB(center).inflate(radius);

        return activeMusicSources.entrySet().stream()
                .filter(entry -> entry.getValue() > currentTime) // 仍在播放中
                .anyMatch(entry -> area.contains(entry.getKey().getCenter()));
    }

    // 定期清理过期音乐源
    public static void cleanupExpiredSources() {
        long currentTime = System.currentTimeMillis();
        activeMusicSources.entrySet().removeIf(entry -> entry.getValue() <= currentTime);
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            for (ServerLevel level : event.getServer().getAllLevels()) {
                MusicDetectionSystem.cleanupExpiredSources();
            }
        }
    }
}