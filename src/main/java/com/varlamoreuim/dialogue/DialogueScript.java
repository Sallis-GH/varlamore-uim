package com.varlamoreuim.dialogue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * An immutable graph of dialogue pages keyed by id. Built via {@link #builder(String)},
 * which validates that every page reference resolves and splits speech pages that
 * exceed the chatbox height into a chain of pages ("id", "id#1", "id#2", ...).
 */
public final class DialogueScript
{
	public static final String END = "END";

	private final String startId;
	private final Map<String, DialoguePage> pages;

	private DialogueScript(String startId, Map<String, DialoguePage> pages)
	{
		this.startId = startId;
		this.pages = Collections.unmodifiableMap(pages);
	}

	public static Builder builder(String startId)
	{
		return new Builder(startId);
	}

	public String getStartId()
	{
		return startId;
	}

	public DialoguePage page(String id)
	{
		return pages.get(id);
	}

	public Map<String, DialoguePage> getPages()
	{
		return pages;
	}

	public static final class Builder
	{
		private final String startId;
		private final Map<String, DialoguePage> pages = new LinkedHashMap<>();

		private Builder(String startId)
		{
			this.startId = startId;
		}

		public Builder player(String id, String text, String next)
		{
			pages.put(id, new PlayerLine(text, next));
			return this;
		}

		public Builder npc(String id, String text, String next)
		{
			return npc(id, text, Expression.DEFAULT, DialogueEffect.NONE, next);
		}

		public Builder npc(String id, String text, Expression expression, DialogueEffect effect, String next)
		{
			pages.put(id, new NpcLine(text, expression, effect, next));
			return this;
		}

		public Builder options(String id, String title, Option... options)
		{
			pages.put(id, new Options(title, List.of(options)));
			return this;
		}

		public DialogueScript build()
		{
			Map<String, DialoguePage> expanded = expand(pages);
			validate(startId, expanded);
			return new DialogueScript(startId, expanded);
		}

		private static Map<String, DialoguePage> expand(Map<String, DialoguePage> source)
		{
			Map<String, DialoguePage> out = new LinkedHashMap<>();
			for (Map.Entry<String, DialoguePage> e : source.entrySet())
			{
				String id = e.getKey();
				DialoguePage page = e.getValue();
				if (page instanceof Options)
				{
					out.put(id, page);
					continue;
				}
				String text = page instanceof PlayerLine ? ((PlayerLine) page).getText() : ((NpcLine) page).getText();
				String next = page instanceof PlayerLine ? ((PlayerLine) page).getNext() : ((NpcLine) page).getNext();
				List<String> chunks = chunk(text);
				for (int i = 0; i < chunks.size(); i++)
				{
					boolean last = i == chunks.size() - 1;
					String chunkId = i == 0 ? id : id + "#" + i;
					String chunkNext = last ? next : id + "#" + (i + 1);
					if (page instanceof PlayerLine)
					{
						out.put(chunkId, new PlayerLine(chunks.get(i), chunkNext));
					}
					else
					{
						NpcLine n = (NpcLine) page;
						DialogueEffect effect = last ? n.getEffect() : DialogueEffect.NONE;
						out.put(chunkId, new NpcLine(chunks.get(i), n.getExpression(), effect, chunkNext));
					}
				}
			}
			return out;
		}

		private static List<String> chunk(String text)
		{
			List<String> lines = DialogueText.wrap(text, DialogueText.MAX_CHARS_PER_LINE);
			List<String> chunks = new ArrayList<>();
			for (int i = 0; i < lines.size(); i += DialogueText.MAX_LINES_PER_PAGE)
			{
				int end = Math.min(lines.size(), i + DialogueText.MAX_LINES_PER_PAGE);
				chunks.add(String.join(" ", lines.subList(i, end)));
			}
			if (chunks.isEmpty())
			{
				chunks.add("");
			}
			return chunks;
		}

		private static void validate(String startId, Map<String, DialoguePage> pages)
		{
			if (!pages.containsKey(startId))
			{
				throw new IllegalStateException("Start page '" + startId + "' does not exist");
			}
			for (Map.Entry<String, DialoguePage> e : pages.entrySet())
			{
				DialoguePage p = e.getValue();
				if (p instanceof PlayerLine)
				{
					require(pages, e.getKey(), ((PlayerLine) p).getNext());
				}
				else if (p instanceof NpcLine)
				{
					require(pages, e.getKey(), ((NpcLine) p).getNext());
				}
				else if (p instanceof Options)
				{
					for (Option o : ((Options) p).getOptions())
					{
						require(pages, e.getKey(), o.getNext());
						if (o.getCondition() != null)
						{
							require(pages, e.getKey(), o.getElseNext());
						}
					}
				}
			}
		}

		private static void require(Map<String, DialoguePage> pages, String from, String target)
		{
			if (target == null || (!END.equals(target) && !pages.containsKey(target)))
			{
				throw new IllegalStateException("Page '" + from + "' references missing page '" + target + "'");
			}
		}
	}
}
