package orison.core.interfaces;

import java.util.List;

import basemod.abstracts.AbstractCardModifier;

public interface OnInitializeDeck {
    default List<AbstractCardModifier> onInitDeckToAddModifiers() {
        return null;
    }
}
