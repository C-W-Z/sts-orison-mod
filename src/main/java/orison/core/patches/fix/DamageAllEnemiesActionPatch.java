package orison.core.patches.fix;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;

import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import orison.actions.InvokeSpikeToAllEnemiesAction;

/**
 * @implNote 把原本的this.damage = {@link DamageInfo#createDamageMatrix}(this.baseDamage);
 * 改成this.damage = {@link DamageInfo#createDamageMatrix}(this.baseDamage, this.damageType != DamageType.NORMAL);
 * @implNote 因為只有this.damageType == {@link DamageType#NORMAL}才能applyPowers
 *
 * @implNote 需要這個fix是因為 {@link InvokeSpikeToAllEnemiesAction} 會用到 {@link DamageAllEnemiesAction}
 */
@SpirePatch2(clz = DamageAllEnemiesAction.class, method = "update")
public class DamageAllEnemiesActionPatch {
    @SpireInstrumentPatch
    public static ExprEditor instrument() {
        return new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws javassist.CannotCompileException {
                // 找到 createDamageMatrix 呼叫
                if (m.getClassName().equals(DamageInfo.class.getName())
                        && m.getMethodName().equals("createDamageMatrix")) {
                    // 攔截呼叫，改成加上第二個參數
                    m.replace("{ $_ = $proceed($1, this.damageType != "
                            + DamageType.class.getName() + ".NORMAL); }");
                }
            }
        };
    }
}
