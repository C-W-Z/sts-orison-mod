package orison.core.configs;

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

    public static class OrisonData {
        public String id;
        public List<Integer> values;
        public UseType useType; // enum
        public int useLimit;
        public float rarity;

        public OrisonData(String id, List<Integer> values, UseType useType, int useLimit, float rarity) {
            this.id = id;
            this.values = values;
            this.useType = useType;
            this.useLimit = useLimit;
            this.rarity = rarity;
        }
    }
}
