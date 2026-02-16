package com.varlamoreuim;

import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.ColorScheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class VarlamoreUimPanel extends PluginPanel
{
	private final JLabel boundaryStatusLabel;

	public VarlamoreUimPanel()
	{
		super(false);
		setBackground(ColorScheme.DARK_GRAY_COLOR);
		setLayout(new BorderLayout());

		// Header
		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
		headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		JLabel titleLabel = new JLabel("Varlamore UIM");
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
		titleLabel.setForeground(Color.WHITE);
		headerPanel.add(titleLabel);
		add(headerPanel, BorderLayout.NORTH);

		// Content area (scrollable)
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

		// Status section
		JPanel statusSection = new JPanel();
		statusSection.setLayout(new BoxLayout(statusSection, BoxLayout.Y_AXIS));
		statusSection.setBackground(ColorScheme.DARK_GRAY_COLOR);
		statusSection.setBorder(new TitledBorder(
			BorderFactory.createLineBorder(ColorScheme.LIGHT_GRAY_COLOR),
			"Status",
			TitledBorder.LEFT,
			TitledBorder.TOP,
			null,
			Color.WHITE
		));

		boundaryStatusLabel = new JLabel("Location: Unknown");
		boundaryStatusLabel.setForeground(Color.LIGHT_GRAY);
		boundaryStatusLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
		statusSection.add(boundaryStatusLabel);

		contentPanel.add(statusSection);
		contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Restrictions section
		JPanel restrictionsSection = new JPanel();
		restrictionsSection.setLayout(new BoxLayout(restrictionsSection, BoxLayout.Y_AXIS));
		restrictionsSection.setBackground(ColorScheme.DARK_GRAY_COLOR);
		restrictionsSection.setBorder(new TitledBorder(
			BorderFactory.createLineBorder(ColorScheme.LIGHT_GRAY_COLOR),
			"Restrictions",
			TitledBorder.LEFT,
			TitledBorder.TOP,
			null,
			Color.WHITE
		));

		JLabel restrictionsLabel = new JLabel("Boundary enforcement: Active");
		restrictionsLabel.setForeground(Color.LIGHT_GRAY);
		restrictionsLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
		restrictionsSection.add(restrictionsLabel);

		contentPanel.add(restrictionsSection);
		contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Quality of Adventure section (placeholder)
		JPanel qoaSection = new JPanel();
		qoaSection.setLayout(new BoxLayout(qoaSection, BoxLayout.Y_AXIS));
		qoaSection.setBackground(ColorScheme.DARK_GRAY_COLOR);
		qoaSection.setBorder(new TitledBorder(
			BorderFactory.createLineBorder(ColorScheme.LIGHT_GRAY_COLOR),
			"Quality of Adventure",
			TitledBorder.LEFT,
			TitledBorder.TOP,
			null,
			Color.WHITE
		));

		JLabel qoaLabel = new JLabel("Coming soon");
		qoaLabel.setForeground(Color.LIGHT_GRAY);
		qoaLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
		qoaSection.add(qoaLabel);

		contentPanel.add(qoaSection);
		contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Progress Tracking section (placeholder)
		JPanel trackingSection = new JPanel();
		trackingSection.setLayout(new BoxLayout(trackingSection, BoxLayout.Y_AXIS));
		trackingSection.setBackground(ColorScheme.DARK_GRAY_COLOR);
		trackingSection.setBorder(new TitledBorder(
			BorderFactory.createLineBorder(ColorScheme.LIGHT_GRAY_COLOR),
			"Progress Tracking",
			TitledBorder.LEFT,
			TitledBorder.TOP,
			null,
			Color.WHITE
		));

		JLabel trackingLabel = new JLabel("Coming soon");
		trackingLabel.setForeground(Color.LIGHT_GRAY);
		trackingLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
		trackingSection.add(trackingLabel);

		contentPanel.add(trackingSection);
		contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Unlocks section (placeholder)
		JPanel unlocksSection = new JPanel();
		unlocksSection.setLayout(new BoxLayout(unlocksSection, BoxLayout.Y_AXIS));
		unlocksSection.setBackground(ColorScheme.DARK_GRAY_COLOR);
		unlocksSection.setBorder(new TitledBorder(
			BorderFactory.createLineBorder(ColorScheme.LIGHT_GRAY_COLOR),
			"Unlocks",
			TitledBorder.LEFT,
			TitledBorder.TOP,
			null,
			Color.WHITE
		));

		JLabel unlocksLabel = new JLabel("Coming soon");
		unlocksLabel.setForeground(Color.LIGHT_GRAY);
		unlocksLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
		unlocksSection.add(unlocksLabel);

		contentPanel.add(unlocksSection);

		// Wrap content in scroll pane
		JScrollPane scrollPane = new JScrollPane(contentPanel);
		scrollPane.setBackground(ColorScheme.DARK_GRAY_COLOR);
		scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * Update boundary status display
	 * @param inVarlamore true if player is inside Varlamore, false otherwise
	 */
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

	/**
	 * Reset status display to unknown state
	 */
	public void resetStatus()
	{
		boundaryStatusLabel.setText("Location: Unknown");
		boundaryStatusLabel.setForeground(Color.LIGHT_GRAY);
	}
}
