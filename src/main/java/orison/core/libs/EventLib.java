package orison.core.libs;

import basemod.BaseMod;
import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.EventUtils.EventType;
import orison.extensions.vanilla.events.NourishingMother;

public class EventLib {

    public static void initialize() {
        BaseMod.addEvent(new AddEventParams.Builder(NourishingMother.ID, NourishingMother.class)
                .eventType(EventType.NORMAL)
                .create());
    }
}
