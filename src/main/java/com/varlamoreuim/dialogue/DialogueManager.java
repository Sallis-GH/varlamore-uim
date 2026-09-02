package com.varlamoreuim.dialogue;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.chatbox.ChatboxPanelManager;

import java.util.function.Consumer;

/**
 * Opens and closes dialogues on the chatbox. Holds at most one open dialogue,
 * closes it when the player moves, and forwards page effects to the caller.
 */
@Slf4j
public class DialogueManager
{
	private final Client client;
	private final ClientThread clientThread;
	private final ChatboxPanelManager chatboxPanelManager;

	private DialogueInput current;
	private WorldPoint openedAt;

	public DialogueManager(Client client, ClientThread clientThread, ChatboxPanelManager chatboxPanelManager)
	{
		this.client = client;
		this.clientThread = clientThread;
		this.chatboxPanelManager = chatboxPanelManager;
	}

	public boolean isOpen()
	{
		return current != null && chatboxPanelManager.getCurrentInput() == current;
	}

	public void open(DialogueScript script, Speaker speaker, DialogueContext context, Consumer<DialogueEffect> effects)
	{
		if (isOpen())
		{
			return;
		}
		Player player = client.getLocalPlayer();
		openedAt = player != null ? player.getWorldLocation() : null;
		DialogueInput input = new DialogueInput(client, clientThread, chatboxPanelManager,
			script, speaker, context, effects, this::onClosed);
		current = input;
		chatboxPanelManager.openInput(input);
		log.debug("Opened dialogue with {}", speaker.getName());
	}

	public void close()
	{
		if (isOpen())
		{
			chatboxPanelManager.close();
		}
	}

	/** Call from the plugin's GameTick. Closes the dialogue if the player has moved. */
	public void onGameTick()
	{
		if (!isOpen() || openedAt == null)
		{
			return;
		}
		Player player = client.getLocalPlayer();
		if (player != null && !openedAt.equals(player.getWorldLocation()))
		{
			close();
		}
	}

	private void onClosed()
	{
		current = null;
		openedAt = null;
	}
}
