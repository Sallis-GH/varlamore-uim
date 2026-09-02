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
		if (wv == null)
		{
			return;
		}
		if (!inViewport(mouse))
		{
			return;
		}
		for (StandIn s : registry.active())
		{
			try
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
					// Talk-to is a real WALK entry when the player needs to move first, so the
					// client performs the walk natively; otherwise a RuneLite entry that opens
					// the dialogue at once. Retyping a RuneLite entry on click is ignored by
					// the client, so the decision has to be made here at menu-build time.
					LocalPoint walk = walkTarget(wv, s);
					MenuEntry talk = client.getMenu().createMenuEntry(-1)
						.setOption(TALK)
						.setTarget(target);
					if (walk != null)
					{
						talk.setType(MenuAction.WALK)
							.setIdentifier(0)
							.setParam0(walk.getSceneX())
							.setParam1(walk.getSceneY());
					}
					else
					{
						talk.setType(MenuAction.RUNELITE)
							.setIdentifier(index);
					}
					return;
				}
			}
			catch (Exception e)
			{
				log.debug("menu injection failed for stand-in", e);
			}
		}
	}

	/**
	 * Cheap prefilter: true when the mouse is inside the 3D viewport rectangle.
	 * Skips clickbox projection entirely while the cursor is over the fixed-mode
	 * chrome, the inventory or the chatbox. Fails open if the client refuses.
	 */
	private boolean inViewport(Point mouse)
	{
		try
		{
			int x = client.getViewportXOffset();
			int y = client.getViewportYOffset();
			int w = client.getViewportWidth();
			int h = client.getViewportHeight();
			if (w <= 0 || h <= 0)
			{
				return true;
			}
			return mouse.getX() >= x && mouse.getX() < x + w
				&& mouse.getY() >= y && mouse.getY() < y + h;
		}
		catch (Exception e)
		{
			log.debug("viewport prefilter failed", e);
			return true;
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
		pending = null;
		MenuEntry entry = event.getMenuEntry();
		String option = entry.getOption();
		if (entry.getType() == MenuAction.WALK && TALK.equals(option))
		{
			// Our walk-first Talk-to entry. Let the client walk; open on arrival.
			StandIn s = byTargetName(entry.getTarget());
			if (s == null)
			{
				return false;
			}
			pending = new PendingTalk(s.getNpc().getIndex(), client.getTickCount() + PendingTalk.TIMEOUT_TICKS);
			log.debug("Walking before talking to {}", s.getPersona().getId());
			return true; // not consumed: the client performs the walk
		}
		if (entry.getType() != MenuAction.RUNELITE)
		{
			return false;
		}
		if (!TALK.equals(option) && !EXAMINE.equals(option))
		{
			return false;
		}
		StandIn s = registry.byNpcIndex(entry.getIdentifier()).orElse(null);
		if (s == null)
		{
			return false;
		}
		event.consume();
		if (EXAMINE.equals(option))
		{
			examine(s);
		}
		else
		{
			talk(s);
		}
		return true;
	}

	/**
	 * Scene-local point of the tile the player should walk to before talking to
	 * this stand-in, or null when no walk is needed (walk-to disabled, already
	 * adjacent, or the tile is outside the scene).
	 */
	private LocalPoint walkTarget(WorldView wv, StandIn s)
	{
		if (!walkToEnabled)
		{
			return null;
		}
		Player player = client.getLocalPlayer();
		if (player == null || wv == null)
		{
			return null;
		}
		WorldPoint npcLocation = s.getNpc().getWorldLocation();
		if (player.getWorldLocation().distanceTo(npcLocation) <= 1)
		{
			return null;
		}
		WorldPoint target = WalkTarget.adjacentTile(npcLocation, player.getWorldLocation());
		return LocalPoint.fromWorld(wv, target);
	}

	/** Finds the active stand-in whose persona name matches a menu target, ignoring colour tags. */
	private StandIn byTargetName(String target)
	{
		if (target == null)
		{
			return null;
		}
		String name = target.replaceAll("<[^>]*>", "").trim();
		for (StandIn s : registry.active())
		{
			if (s.getPersona().getDisplayName().equals(name))
			{
				return s;
			}
		}
		return null;
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
