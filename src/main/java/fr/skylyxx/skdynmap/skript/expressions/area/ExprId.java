package fr.skylyxx.skdynmap.skript.expressions.area;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.skylyxx.skdynmap.utils.types.AreaBuilder;
import fr.skylyxx.skdynmap.utils.types.DynmapArea;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprId extends SimplePropertyExpression<Object, String> {

    static {
        register(ExprId.class, String.class,
                "id",
                "dynmaparea/areabuilder"
        );
    }

    @Nullable
    @Override
    public String convert(Object o) {
        if(o instanceof DynmapArea) {
            return ((DynmapArea) o).getId();
        } else if(o instanceof AreaBuilder) {
            return ((AreaBuilder) o).getId();
        }
        return null;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "id";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if(mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(String.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        for(Object o : getExpr().getArray(e)) {
            if(o instanceof DynmapArea) {
                ((DynmapArea) o).setId((String) delta[0]);
            } else if(o instanceof AreaBuilder) {
                ((AreaBuilder) o).setId((String) delta[0]);
            }
        }
    }
}
