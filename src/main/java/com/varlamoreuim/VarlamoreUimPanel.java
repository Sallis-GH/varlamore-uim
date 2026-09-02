package com.varlamoreuim;

import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.ColorScheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VarlamoreUimPanel extends PluginPanel
{
	private final JLabel boundaryStatusLabel;

	public VarlamoreUimPanel()
	{
		super(true);

		// Header
		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
		headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		JLabel titleLabel = new JLabel("Varlamore UIM");
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
		titleLabel.setForeground(Color.WHITE);
		headerPanel.add(titleLabel);
		add(headerPanel);

		// Status section (always open)
		JPanel statusContent = new JPanel();
		statusContent.setLayout(new BoxLayout(statusContent, BoxLayout.Y_AXIS));
		statusContent.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		boundaryStatusLabel = new JLabel("Location: Unknown");
		boundaryStatusLabel.setForeground(Color.LIGHT_GRAY);
		boundaryStatusLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
		statusContent.add(boundaryStatusLabel);
		add(createCollapsibleSection("Status", statusContent, true));

		// Restrictions section
		JPanel restrictionsContent = new JPanel();
		restrictionsContent.setLayout(new BoxLayout(restrictionsContent, BoxLayout.Y_AXIS));
		restrictionsContent.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		JLabel restrictionsLabel = new JLabel("Boundary enforcement: Active");
		restrictionsLabel.setForeground(Color.LIGHT_GRAY);
		restrictionsLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
		restrictionsContent.add(restrictionsLabel);
		add(createCollapsibleSection("Restrictions", restrictionsContent, true));

		// Quality of Adventure section (placeholder, collapsed)
		JPanel qoaContent = new JPanel();
		qoaContent.setLayout(new BoxLayout(qoaContent, BoxLayout.Y_AXIS));
		qoaContent.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		JLabel qoaLabel = new JLabel("Coming soon");
		qoaLabel.setForeground(Color.GRAY);
		qoaLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
		qoaContent.add(qoaLabel);
		add(createCollapsibleSection("Quality of Adventure", qoaContent, false));

		// Progress Tracking section (placeholder, collapsed)
		JPanel trackingContent = new JPanel();
		trackingContent.setLayout(new BoxLayout(trackingContent, BoxLayout.Y_AXIS));
		trackingContent.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		JLabel trackingLabel = new JLabel("Coming soon");
		trackingLabel.setForeground(Color.GRAY);
		trackingLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
		trackingContent.add(trackingLabel);
		add(createCollapsibleSection("Progress Tracking", trackingContent, false));

		// Unlocks section (placeholder, collapsed)
		JPanel unlocksContent = new JPanel();
		unlocksContent.setLayout(new BoxLayout(unlocksContent, BoxLayout.Y_AXIS));
		unlocksContent.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		JLabel unlocksLabel = new JLabel("Coming soon");
		unlocksLabel.setForeground(Color.GRAY);
		unlocksLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
		unlocksContent.add(unlocksLabel);
		add(createCollapsibleSection("Unlocks", unlocksContent, false));
	}

	private JPanel createCollapsibleSection(String title, JPanel content, boolean startExpanded)
	{
		JPanel section = new JPanel();
		section.setLayout(new BorderLayout());
		section.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		// Header bar
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(ColorScheme.MEDIUM_GRAY_COLOR);
		header.setBorder(new EmptyBorder(6, 10, 6, 10));
		header.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		JLabel arrow = new JLabel(startExpanded ? "\u25BC" : "\u25B6");
		arrow.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
		arrow.setBorder(new EmptyBorder(0, 0, 0, 6));

		JLabel titleLabel = new JLabel(title);
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 12f));

		header.add(arrow, BorderLayout.WEST);
		header.add(titleLabel, BorderLayout.CENTER);

		content.setVisible(startExpanded);

		// Toggle on click
		header.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				boolean visible = !content.isVisible();
				content.setVisible(visible);
				arrow.setText(visible ? "\u25BC" : "\u25B6");
				section.revalidate();
				section.repaint();
			}
		});

		section.add(header, BorderLayout.NORTH);
		section.add(content, BorderLayout.CENTER);

		return section;
	}

	public void updateBoundaryStatus(boolean inVarlamore)
	{
		if (inVarlamore)
		{
			boundaryStatusLabel.setText("<html>Location: <span style='color: #00ff00'>Inside Varlamore</span></html>");
		}
		else
		{
			boundaryStatusLabel.setText("<html>Location: <span style='color: #ff6666'>Outside Varlamore</span></html>");
		}
	}

	public void resetStatus()
	{
		boundaryStatusLabel.setText("Location: Unknown");
		boundaryStatusLabel.setForeground(Color.LIGHT_GRAY);
	}
}
