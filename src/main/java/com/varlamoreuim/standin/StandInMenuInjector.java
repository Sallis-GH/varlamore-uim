package com.varlamoreuim.standin;

import com.varlamoreuim.dialogue.DialogueContext;
import com.varlamoreuim.dialogue.DialogueEffect;
import com.varlamoreuim.dialogue.DialogueManager;
import com.varlamoreuim.dialogue.Speaker;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.Model;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.RuneLiteObject;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;

import java.awt.Color;
import java.awt.Shape;

/**
 * Adds "Talk-to" and "Examine" menu entries when the mouse is over a stand-in's
 * projected model, handles those clicks, and implements walk-then-talk.
 */
@Slf4j
public class StandInMenuInjector
{
	private static final String TALK = "Talk-to";
	private static final String EXAMINE = "Examine";

	private final Client client;
	private final ChatMessageManager chatMessageManager;
	private final DialogueManager dialogueManager;
	private final StandInRegistry registry;
	private final DialogueContext context;
	private final Runnable onUnlockRequested;

	@Setter
	private boolean walkToEnabled = true;
	@Setter
	private boolean nativeDialogueEnabled = true;

	private PendingTalk pending;

	public StandInMenuInjector(Client client, ChatMessageManager chatMessageManager, DialogueManager dialogueManager,
		StandInRegistry registry, DialogueContext context, Runnable onUnlockRequested)
	{
		this.client = client;
		this.chatMessageManager = chatMessageManager;
		this.dialogueManager = dialogueManager;
		this.registry = registry;
		this.context = context;
		this.onUnlockRequested = onUnlockRequested;
	}

	public void onPostMenuSort()
	{
		if (registry.active().isEmpty() || client.isMenuOpen())
		{
			return;
		}
		Point mouse = client.getMouseCanvasPosition();
		if (mouse == null)
		{
			return;
		}
		WorldView wv = client.getTopLevelWorldView();
		for (StandIn s : registry.active())
		{
			if (hit(wv, s.getObject(), mouse))
			{
				String target = "<col=ffff00>" + s.getPersona().getDisplayName() + "</col>";
				int index = s.getNpc().getIndex();
				client.getMenu().createMenuEntry(-1)
					.setOption(EXAMINE)
					.setTarget(target)
					.setType(MenuAction.RUNELITE)
					.setIdentifier(index);
				client.getMenu().createMenuEntry(-1)
					.setOption(TALK)
					.setTarget(target)
					.setType(MenuAction.RUNELITE)
					.setIdentifier(index);
				return;
			}
		}
	}

	private boolean hit(WorldView wv, RuneLiteObject object, Point mouse)
	{
		try
		{
			Model model = object.getModel();
			LocalPoint lp = object.getLocation();
			if (model == null || lp == null || !object.isActive())
			{
				return false;
			}
			int z = Perspective.getTileHeight(client, lp, wv.getPlane());
			Shape clickbox = Perspective.getClickbox(client, wv, model, object.getOrientation(), lp.getX(), lp.getY(), z);
			return clickbox != null && clickbox.contains(mouse.getX(), mouse.getY());
		}
		catch (Exception e)
		{
			log.debug("clickbox projection failed", e);
			return false;
		}
	}

	/** Returns true if the click was ours. */
	public boolean onMenuOptionClicked(MenuOptionClicked event)
	{
		MenuEntry entry = event.getMenuEntry();
		if (entry.getType() != MenuAction.RUNELITE)
		{
			pending = null;
			return false;
		}
		String option = entry.getOption();
		if (!TALK.equals(option) && !EXAMINE.equals(option))
		{
			pending = null;
			return false;
		}
		StandIn s = registry.byNpcIndex(entry.getIdentifier()).orElse(null);
		if (s == null)
		{
			return false;
		}
		if (EXAMINE.equals(option))
		{
			event.consume();
			examine(s);
			return true;
		}
		Player player = client.getLocalPlayer();
		if (walkToEnabled && player != null && player.getWorldLocation().distanceTo(s.getNpc().getWorldLocation()) > 1)
		{
			WorldPoint target = WalkTarget.adjacentTile(s.getNpc().getWorldLocation(), player.getWorldLocation());
			LocalPoint lp = LocalPoint.fromWorld(client.getTopLevelWorldView(), target);
			if (lp != null)
			{
				entry.setType(MenuAction.WALK);
				entry.setIdentifier(0);
				entry.setParam0(lp.getSceneX());
				entry.setParam1(lp.getSceneY());
				pending = new PendingTalk(s.getNpc().getIndex(), client.getTickCount() + PendingTalk.TIMEOUT_TICKS);
				log.debug("Walking to {} before talking to {}", target, s.getPersona().getId());
				return true; // not consumed: the client performs the walk
			}
		}
		event.consume();
		talk(s);
		return true;
	}

	public void onGameTick(int tick)
	{
		if (pending == null)
		{
			return;
		}
		if (pending.isExpired(tick))
		{
			pending = null;
			return;
		}
		StandIn s = registry.byNpcIndex(pending.getNpcIndex()).orElse(null);
		Player player = client.getLocalPlayer();
		if (s == null || player == null)
		{
			pending = null;
			return;
		}
		boolean adjacent = player.getWorldLocation().distanceTo(s.getNpc().getWorldLocation()) <= 1;
		boolean idle = player.getPoseAnimation() == player.getIdlePoseAnimation();
		if (adjacent && idle)
		{
			pending = null;
			talk(s);
		}
	}

	private void talk(StandIn s)
	{
		Persona p = s.getPersona();
		if (!nativeDialogueEnabled)
		{
			String message = new ChatMessageBuilder()
				.append(Color.WHITE, p.getDisplayName() + ": \"The ships aren't running. Come back with Dizana's quiver.\"")
				.build();
			chatMessageManager.queue(QueuedMessage.builder()
				.type(ChatMessageType.GAMEMESSAGE)
				.runeLiteFormattedMessage(message)
				.build());
			return;
		}
		dialogueManager.open(p.getScript(), new Speaker(p.getDisplayName(), p.getNpcId()), context, effect ->
		{
			if (effect == DialogueEffect.UNLOCK_CHARTER)
			{
				onUnlockRequested.run();
			}
		});
	}

	private void examine(StandIn s)
	{
		chatMessageManager.queue(QueuedMessage.builder()
			.type(ChatMessageType.NPC_EXAMINE)
			.runeLiteFormattedMessage(s.getPersona().getExamine())
			.build());
	}
}
