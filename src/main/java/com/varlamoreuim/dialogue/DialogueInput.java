package com.varlamoreuim.dialogue;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.FontID;
import net.runelite.api.widgets.JavaScriptCallback;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetModelType;
import net.runelite.api.widgets.WidgetPositionMode;
import net.runelite.api.widgets.WidgetSizeMode;
import net.runelite.api.widgets.WidgetTextAlignment;
import net.runelite.api.widgets.WidgetType;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.chatbox.ChatboxInput;
import net.runelite.client.game.chatbox.ChatboxPanelManager;
import net.runelite.client.input.KeyListener;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.function.Consumer;

/**
 * Renders a {@link DialogueScript} on the chatbox message layer, one page at a time,
 * and advances on space, number keys, or clicks on the continue/option widgets.
 * All widget work runs on the client thread.
 */
@Slf4j
public class DialogueInput extends ChatboxInput implements KeyListener
{
	private final ClientThread clientThread;
	private final ChatboxPanelManager chatboxPanelManager;
	private final DialogueScript script;
	private final Speaker speaker;
	private final DialogueContext context;
	private final Consumer<DialogueEffect> effects;
	private final Consumer<DialogueInput> onClosed;

	private DialoguePage currentPage;
	private DialogueEffect pendingEffect;

	public DialogueInput(ClientThread clientThread, ChatboxPanelManager chatboxPanelManager,
		DialogueScript script, Speaker speaker, DialogueContext context,
		Consumer<DialogueEffect> effects, Consumer<DialogueInput> onClosed)
	{
		this.clientThread = clientThread;
		this.chatboxPanelManager = chatboxPanelManager;
		this.script = script;
		this.speaker = speaker;
		this.context = context;
		this.effects = effects;
		this.onClosed = onClosed;
	}

	@Override
	protected void open()
	{
		show(script.getStartId());
	}

	@Override
	protected void close()
	{
		onClosed.accept(this);
	}

	private void show(String id)
	{
		if (DialogueScript.END.equals(id))
		{
			if (pendingEffect != null && pendingEffect != DialogueEffect.NONE)
			{
				DialogueEffect effect = pendingEffect;
				pendingEffect = null;
				effects.accept(effect);
			}
			chatboxPanelManager.close();
			return;
		}
		DialoguePage page = script.page(id);
		if (page == null)
		{
			log.warn("Dialogue page '{}' missing, closing", id);
			chatboxPanelManager.close();
			return;
		}
		currentPage = page;
		render(page);
		if (page instanceof NpcLine)
		{
			// Fired when the page is left via the END branch, not when it is shown:
			// closing with escape must not apply the effect.
			pendingEffect = ((NpcLine) page).getEffect();
		}
	}

	private void render(DialoguePage page)
	{
		Widget container = chatboxPanelManager.getContainerWidget();
		if (container == null)
		{
			log.warn("Chatbox container missing, closing dialogue");
			chatboxPanelManager.close();
			return;
		}
		container.deleteAllChildren();

		if (page instanceof Options)
		{
			renderOptions(container, (Options) page);
		}
		else if (page instanceof PlayerLine)
		{
			PlayerLine p = (PlayerLine) page;
			renderSpeech(container, context.playerName(), p.getText(), true, Expression.DEFAULT, -1);
		}
		else
		{
			NpcLine n = (NpcLine) page;
			renderSpeech(container, speaker.getName(), n.getText(), false, n.getExpression(), speaker.getNpcId());
		}
	}

	private void renderSpeech(Widget container, String name, String text, boolean player, Expression expression, int npcId)
	{
		int width = container.getWidth();
		int headX = player ? width - DialogueLayout.HEAD_MARGIN_X - DialogueLayout.HEAD_SIZE : DialogueLayout.HEAD_MARGIN_X;

		Widget head = container.createChild(-1, WidgetType.MODEL);
		head.setModelType(player ? WidgetModelType.LOCAL_PLAYER_CHATHEAD : WidgetModelType.NPC_CHATHEAD);
		if (!player)
		{
			head.setModelId(npcId);
		}
		head.setAnimationId(expression.getTalkAnimationId());
		head.setModelZoom(DialogueLayout.HEAD_ZOOM);
		head.setRotationX(DialogueLayout.HEAD_ROTATION_X);
		head.setRotationY(DialogueLayout.HEAD_ROTATION_Y);
		head.setRotationZ(DialogueLayout.HEAD_ROTATION_Z);
		head.setOriginalX(headX);
		head.setOriginalY(DialogueLayout.HEAD_Y);
		head.setOriginalWidth(DialogueLayout.HEAD_SIZE);
		head.setOriginalHeight(DialogueLayout.HEAD_SIZE);
		head.revalidate();

		// Text column is the space not taken by the head.
		int textX = player ? DialogueLayout.HEAD_MARGIN_X : DialogueLayout.HEAD_MARGIN_X + DialogueLayout.HEAD_SIZE;
		int textWidth = width - 2 * DialogueLayout.HEAD_MARGIN_X - DialogueLayout.HEAD_SIZE;

		Widget nameWidget = container.createChild(-1, WidgetType.TEXT);
		nameWidget.setText(name);
		nameWidget.setTextColor(DialogueLayout.COLOR_NAME);
		nameWidget.setFontId(FontID.QUILL_8);
		nameWidget.setOriginalX(textX);
		nameWidget.setOriginalY(DialogueLayout.NAME_Y);
		nameWidget.setOriginalWidth(textWidth);
		nameWidget.setOriginalHeight(DialogueLayout.LINE_HEIGHT);
		nameWidget.setXTextAlignment(WidgetTextAlignment.CENTER);
		nameWidget.setYTextAlignment(WidgetTextAlignment.CENTER);
		nameWidget.revalidate();

		List<String> lines = DialogueText.wrap(text, DialogueText.MAX_CHARS_PER_LINE);
		Widget body = container.createChild(-1, WidgetType.TEXT);
		body.setText(String.join("<br>", lines));
		body.setTextColor(DialogueLayout.COLOR_TEXT);
		body.setFontId(FontID.QUILL_8);
		body.setOriginalX(textX);
		body.setOriginalY(DialogueLayout.TEXT_Y);
		body.setOriginalWidth(textWidth);
		body.setOriginalHeight(DialogueLayout.LINE_HEIGHT * DialogueText.MAX_LINES_PER_PAGE);
		body.setXTextAlignment(WidgetTextAlignment.CENTER);
		body.setYTextAlignment(WidgetTextAlignment.CENTER);
		body.setAction(0, "Continue");
		body.setOnOpListener((JavaScriptCallback) ev -> advance());
		body.setHasListener(true);
		body.revalidate();

		Widget cont = container.createChild(-1, WidgetType.TEXT);
		cont.setText("Click here to continue");
		cont.setTextColor(DialogueLayout.COLOR_CONTINUE);
		cont.setFontId(FontID.QUILL_8);
		cont.setOriginalX(textX);
		cont.setOriginalY(container.getHeight() - DialogueLayout.CONTINUE_BOTTOM_MARGIN - DialogueLayout.LINE_HEIGHT);
		cont.setOriginalWidth(textWidth);
		cont.setOriginalHeight(DialogueLayout.LINE_HEIGHT);
		cont.setXTextAlignment(WidgetTextAlignment.CENTER);
		cont.setYTextAlignment(WidgetTextAlignment.CENTER);
		cont.setAction(0, "Continue");
		cont.setOnOpListener((JavaScriptCallback) ev -> advance());
		cont.setOnMouseOverListener((JavaScriptCallback) ev -> cont.setTextColor(DialogueLayout.COLOR_HOVER));
		cont.setOnMouseLeaveListener((JavaScriptCallback) ev -> cont.setTextColor(DialogueLayout.COLOR_CONTINUE));
		cont.setHasListener(true);
		cont.revalidate();
	}

	private void renderOptions(Widget container, Options page)
	{
		Widget title = container.createChild(-1, WidgetType.TEXT);
		title.setText(page.getTitle());
		title.setTextColor(DialogueLayout.COLOR_NAME);
		title.setFontId(FontID.QUILL_8);
		title.setXPositionMode(WidgetPositionMode.ABSOLUTE_CENTER);
		title.setOriginalX(0);
		title.setOriginalY(8);
		title.setOriginalHeight(24);
		title.setXTextAlignment(WidgetTextAlignment.CENTER);
		title.setYTextAlignment(WidgetTextAlignment.CENTER);
		title.setWidthMode(WidgetSizeMode.MINUS);
		title.revalidate();

		List<Option> options = page.getOptions();
		int y = title.getOriginalY() + title.getHeight() + 6;
		int height = container.getHeight() - y - 8;
		int step = height / options.size();
		int maxStep = options.size() >= 3 ? 25 : 30;
		if (step > maxStep)
		{
			int ds = step - maxStep;
			step = maxStep;
			y += (ds * options.size()) / 2;
		}
		for (Option option : options)
		{
			Widget w = container.createChild(-1, WidgetType.TEXT);
			w.setText(option.getLabel());
			w.setTextColor(DialogueLayout.COLOR_TEXT);
			w.setFontId(FontID.QUILL_8);
			w.setXPositionMode(WidgetPositionMode.ABSOLUTE_CENTER);
			w.setOriginalX(0);
			w.setOriginalY(y);
			w.setOriginalHeight(24);
			w.setXTextAlignment(WidgetTextAlignment.CENTER);
			w.setYTextAlignment(WidgetTextAlignment.CENTER);
			w.setWidthMode(WidgetSizeMode.MINUS);
			w.setAction(0, "Continue");
			w.setOnOpListener((JavaScriptCallback) ev -> select(option));
			w.setOnMouseOverListener((JavaScriptCallback) ev -> w.setTextColor(DialogueLayout.COLOR_HOVER));
			w.setOnMouseLeaveListener((JavaScriptCallback) ev -> w.setTextColor(DialogueLayout.COLOR_TEXT));
			w.setHasListener(true);
			w.revalidate();
			y += step;
		}
	}

	private void advance()
	{
		if (currentPage instanceof PlayerLine)
		{
			show(((PlayerLine) currentPage).getNext());
		}
		else if (currentPage instanceof NpcLine)
		{
			show(((NpcLine) currentPage).getNext());
		}
	}

	private void select(Option option)
	{
		show(option.resolve(context));
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		if (!chatboxPanelManager.shouldTakeInput())
		{
			return;
		}
		char c = e.getKeyChar();
		if (c == '\033')
		{
			e.consume();
			clientThread.invokeLater(chatboxPanelManager::close);
			return;
		}
		if (currentPage instanceof Options)
		{
			int n = c - '1';
			List<Option> options = ((Options) currentPage).getOptions();
			if (n >= 0 && n < options.size())
			{
				e.consume();
				Option option = options.get(n);
				clientThread.invokeLater(() -> select(option));
			}
			return;
		}
		if (c == ' ')
		{
			e.consume();
			clientThread.invokeLater(this::advance);
		}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (!chatboxPanelManager.shouldTakeInput())
		{
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			e.consume();
			return;
		}
		// Options pages take number keys, not space — leave space to the client.
		if (e.getKeyCode() == KeyEvent.VK_SPACE && !(currentPage instanceof Options))
		{
			e.consume();
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
	}
}
