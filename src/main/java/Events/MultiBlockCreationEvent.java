package Events;

import me.ODINN.ASMBB.Project;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MultiBlockCreationEvent extends Event implements Cancellable {

    private final UUID multiBlockID;
    private final Project project;
    private boolean isCancelled;
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public MultiBlockCreationEvent(UUID multiBlockID, Project project){
        this.multiBlockID = multiBlockID;
        this.project = project;

        this.isCancelled = false;
    }


    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList(){
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    /**
     *
     * @return the multi block's project
     */
    public Project getProject(){
        return this.project;
    }

    /**
     *
     * @return the multi block's id
     */
    public UUID getMultiBlockID(){
        return this.multiBlockID;
    }


}
