package Events;

import me.ODINN.ASMBB.Project;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MultiBlockDeleteEvent extends Event implements Cancellable {



    private boolean isCancelled;
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final UUID multiBlockID;


    public MultiBlockDeleteEvent(UUID multiBlockID) {
        this.multiBlockID = multiBlockID;

        this.isCancelled = false;
    }


    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList(){
        return HANDLER_LIST;
    }

    /**
     *
     * @return the multi block's id
     */
    public UUID getMultiBlockID(){
        return this.multiBlockID;
    }


}
