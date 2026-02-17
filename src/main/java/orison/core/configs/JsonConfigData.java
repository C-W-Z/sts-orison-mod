package orison.core.configs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import orison.core.abstracts.AbstractOrison.UseType;

public class JsonConfigData {

    public int CONFIG_SCREEN_BG = 0;

    public boolean CAN_ATTACH_ON_UNPLAYABLE_CARD = false;
    public boolean CAN_ATTACH_ON_STATUS = false;
    public boolean CAN_ATTACH_ON_CURSE = false;
    public boolean CAN_ATTACH_ON_COLORLESS = true;

    public float MONSTER_DROP_ORISON_CHANCE = 0.25F;
    public float MONSTER_DROP_ORISON_ADV_CHANCE = 0F;
    public boolean MONSTER_DROP_ORISON_LINKED = true;

    public float ELITE_DROP_ORISON_CHANCE = 1F;
    public float ELITE_DROP_ORISON_ADV_CHANCE = 0.5F;
    public boolean ELITE_DROP_ORISON_LINKED = true;

    public float BOSS_DROP_ORISON_CHANCE = 1F;
    public float BOSS_DROP_ORISON_ADV_CHANCE = 1F;
    public boolean BOSS_DROP_ORISON_LINKED = false;

    public float TREASURE_DROP_ORISON_CHANCE = 1F;
    public float TREASURE_DROP_ORISON_ADV_CHANCE = 0.25F;

    public Map<String, Boolean> eventEnabledMap = new HashMap<>();

    public Map<String, OrisonData> orisonDataMap = new HashMap<>();
    public Map<String, OrisonData> advOrisonDataMap = new HashMap<>();

    public OrisonData getOrisonData(String id, boolean adv) {
        return adv ? advOrisonDataMap.get(id) : orisonDataMap.get(id);
    }

    public static class OrisonData {
        public String id;
        public List<Integer> values;
        public UseType useType; // enum
        public Integer useLimit;
        public Float rarity;

        public OrisonData(String id, List<Integer> values, UseType useType, Integer useLimit, Float rarity) {
            this.id = id;
            this.values = values;
            this.useType = useType;
            this.useLimit = useLimit;
            this.rarity = rarity;
        }
    }

    public JsonConfigData copy() {
        JsonConfigData newConfig = new JsonConfigData();
        newConfig.CONFIG_SCREEN_BG = CONFIG_SCREEN_BG;

        newConfig.CAN_ATTACH_ON_UNPLAYABLE_CARD = CAN_ATTACH_ON_UNPLAYABLE_CARD;
        newConfig.CAN_ATTACH_ON_STATUS = CAN_ATTACH_ON_STATUS;
        newConfig.CAN_ATTACH_ON_CURSE = CAN_ATTACH_ON_CURSE;
        newConfig.CAN_ATTACH_ON_COLORLESS = CAN_ATTACH_ON_COLORLESS;

        newConfig.MONSTER_DROP_ORISON_CHANCE = MONSTER_DROP_ORISON_CHANCE;
        newConfig.MONSTER_DROP_ORISON_ADV_CHANCE = MONSTER_DROP_ORISON_ADV_CHANCE;
        newConfig.MONSTER_DROP_ORISON_LINKED = MONSTER_DROP_ORISON_LINKED;

        newConfig.ELITE_DROP_ORISON_CHANCE = ELITE_DROP_ORISON_CHANCE;
        newConfig.ELITE_DROP_ORISON_ADV_CHANCE = ELITE_DROP_ORISON_ADV_CHANCE;
        newConfig.ELITE_DROP_ORISON_LINKED = ELITE_DROP_ORISON_LINKED;

        newConfig.BOSS_DROP_ORISON_CHANCE = BOSS_DROP_ORISON_CHANCE;
        newConfig.BOSS_DROP_ORISON_ADV_CHANCE = BOSS_DROP_ORISON_ADV_CHANCE;
        newConfig.BOSS_DROP_ORISON_LINKED = BOSS_DROP_ORISON_LINKED;

        newConfig.TREASURE_DROP_ORISON_CHANCE = TREASURE_DROP_ORISON_CHANCE;
        newConfig.TREASURE_DROP_ORISON_ADV_CHANCE = TREASURE_DROP_ORISON_ADV_CHANCE;

        newConfig.eventEnabledMap = new HashMap<>(this.eventEnabledMap);

        newConfig.orisonDataMap = copy(this.orisonDataMap);
        newConfig.advOrisonDataMap = copy(this.advOrisonDataMap);

        return newConfig;
    }

    public static HashMap<String, OrisonData> copy(Map<String, OrisonData> original) {
        HashMap<String, OrisonData> copy = new HashMap<>();
        for (OrisonData o : original.values()) {
            List<Integer> newVals = o.values == null ? null : new ArrayList<>(o.values);
            copy.put(o.id, new OrisonData(o.id, newVals, o.useType, o.useLimit, o.rarity));
        }
        return copy;
    }
}
