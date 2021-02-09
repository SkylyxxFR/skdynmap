package fr.skylyxx.skdynmap.skript.scopes;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.config.SimpleNode;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.util.Kleenean;
import fr.skylyxx.skdynmap.SkDynmap;
import fr.skylyxx.skdynmap.utils.EffectSection;
import fr.skylyxx.skdynmap.utils.types.AreaBuilder;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScopeMakeArea extends EffectSection {

    public static AreaBuilder lastArea;

    static {
        Skript.registerCondition(ScopeMakeArea.class, "make [new] [dynmap] area");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (checkIfCondition()) {
            return false;
        }
        if (!hasSection()) {
            return false;
        }
        loadSection(true);
        SectionNode topNode = (SectionNode) SkriptLogger.getNode();
        boolean hasNameSetter = false;
        boolean hasLocSetter = false;

        Pattern regexName1 = Pattern.compile("(last )?(generated |created )?area's name");
        Pattern regexName2 = Pattern.compile("name of( last)?( generated| created)? area");
        Pattern regexLoc1 = Pattern.compile("(last )?(generated |created )?area's location(s)?");
        Pattern regexLoc2 = Pattern.compile("location(s)? of( last)?( generated| created)? area");

        for(Node node : topNode) {
            Matcher matcherName1 = regexName1.matcher(node.getKey());
            Matcher matcherName2 = regexName2.matcher(node.getKey());
            if(matcherName1.find() || matcherName2.find()) {
                hasNameSetter = true;
                continue;
            }
            Matcher matcherLoc1 = regexLoc1.matcher(node.getKey());
            Matcher matcherLoc2 = regexLoc2.matcher(node.getKey());
            if(matcherLoc1.find() || matcherLoc2.find()) {
                hasLocSetter = true;
                continue;
            }
        }

        if(!hasNameSetter) {
            Skript.error("You have to define a name for your area in the \"make area\" scope !");
        }
        if(!hasLocSetter) {
            Skript.error("You have to define locations for your area in the \"make area\" scope !");
        }
        return true;
    }

    @Override
    protected void execute(Event e) {
        lastArea = new AreaBuilder();
        runSection(e);

    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "make new dynmap area";
    }

}