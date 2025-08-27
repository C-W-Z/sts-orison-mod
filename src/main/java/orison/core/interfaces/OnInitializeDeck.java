package orison.core.interfaces;

import java.util.List;

import basemod.abstracts.AbstractCardModifier;
import orison.core.patches.InitializeDeckPatch;
import orison.core.relics.OrganicForm;

public interface OnInitializeDeck {
    /**
     * @see InitializeDeckPatch
     * @see OrganicForm
     */
    default List<AbstractCardModifier> onInitDeckToAddModifiers() {
        return null;
    }
}
