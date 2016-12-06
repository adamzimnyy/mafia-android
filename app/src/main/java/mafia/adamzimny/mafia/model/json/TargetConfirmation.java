package mafia.adamzimny.mafia.model.json;

import mafia.adamzimny.mafia.model.Code;
import mafia.adamzimny.mafia.model.Target;

/**
 * Created by adamz on 20.09.2016.
 */
public class TargetConfirmation {
    Target target;
    String code;

    public TargetConfirmation() {
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public TargetConfirmation(Target target, String code) {
        this.target = target;
        this.code = code;
    }
}
