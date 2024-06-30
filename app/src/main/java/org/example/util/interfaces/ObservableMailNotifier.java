package org.example.util.interfaces;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for classes that need to be notified when an email is sent
 */
public interface ObservableMailNotifier {
    /**
     * Types of events that can be triggered
     */
    enum EventType {
        SEND_SUCCESS,
        SEND_FAILURE
    }

    /**
     * Notify the listener of the event
     * @param type Type of event
     * @param message Description of the event
     */
    void notifySendListener(EventType type, String message);

    /**
     * Set the listener for the event
     * @param type Type of event
     * @param listener Listener to be notified
     */
    void setSendListener(EventType type, @NotNull MailCompleteListener listener);

    /**
     * Get the listener for the event
     * @param type Type of event
     * @return Listener to be notified
     */
    @Nullable MailCompleteListener getSendListener(EventType type);

    /**
     * Remove the listener for the event
     * @param type Type of event
     */
    void removeSendListener(@NotNull EventType type);
}
