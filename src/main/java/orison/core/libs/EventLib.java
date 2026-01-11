package orison.core.libs;

import java.util.HashMap;
import java.util.Map;

import basemod.BaseMod;
import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.EventUtils.EventType;
import orison.core.configs.OrisonConfig;
import orison.extensions.vanilla.events.NourishingMother;

public class EventLib {

    private static Map<String, Boolean> enabled = new HashMap<>();

    public static void initialize() {
        enabled.put(NourishingMother.ID, OrisonConfig.EventEnable.load(NourishingMother.ID));
        BaseMod.addEvent(new AddEventParams.Builder(NourishingMother.ID, NourishingMother.class)
                .eventType(EventType.NORMAL)
                .bonusCondition(() -> getEnable(NourishingMother.ID))
                .create());
    }

    protected static boolean getDefaultEnable() {
        return true;
    }

    public static boolean getEnable(String eventID) {
        return enabled.getOrDefault(eventID, getDefaultEnable());
    }

    public static void saveEnable(String eventID, boolean newVal) {
        enabled.put(eventID, newVal);
        OrisonConfig.EventEnable.save(eventID, newVal);
    }
}
