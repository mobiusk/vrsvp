package com.mobiusk.vrsvp.modal;

import com.mobiusk.vrsvp.embed.EmbedUi;
import com.mobiusk.vrsvp.util.Formatter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

import javax.annotation.Nonnull;

@RequiredArgsConstructor
public class ModalReply {

	// Class constructor field(s)
	private final EmbedUi embedUi;

	/**
	 * Edit a message by changing the main text, and then edit the original ephemeral message to confirm the action.
	 */
	public void editDescription(
		@Nonnull ModalInteractionEvent event,
		@Nonnull Message message,
		String description
	) {

		message.editMessage(description).queue();

		var reply = Formatter.replies("Description has been updated.");
		event.editMessage(reply).queue();
	}

	/**
	 * Edit an embed by changing the main description, and then edit the original ephemeral message to confirm the action.
	 */
	public void editEmbed(
		@Nonnull ModalInteractionEvent event,
		@Nonnull Message message,
		String textInput,
		int embedIndex
	) {

		var existingEmbeds = message.getEmbeds();
		var editedEmbeds = embedUi.editEmbed(existingEmbeds, textInput, embedIndex);
		message.editMessageEmbeds(editedEmbeds).queue();

		var reply = Formatter.replies(String.format("Block #%d has been updated.", embedIndex + 1));
		event.getHook().editOriginal(reply).queue();
	}

	/**
	 * Edit a field by changing the title, and then edit the original ephemeral message to confirm the action.
	 */
	public void editFieldTitle(
		@Nonnull ModalInteractionEvent event,
		@Nonnull Message message,
		String textInput,
		int fieldIndex
	) {

		var existingEmbeds = message.getEmbeds();
		var editedEmbeds = embedUi.editFieldTitle(existingEmbeds, textInput, fieldIndex);
		message.editMessageEmbeds(editedEmbeds).queue();

		var reply = Formatter.replies(String.format("Slot #%d title has been updated.", fieldIndex + 1));
		event.getHook().editOriginal(reply).queue();
	}

	/**
	 * Edit a field by changing the value, and then edit the original ephemeral message to confirm the action.
	 */
	public void editFieldValue(
		@Nonnull ModalInteractionEvent event,
		@Nonnull Message message,
		String textInput,
		int fieldIndex
	) {

		var existingEmbeds = message.getEmbeds();
		var editedEmbeds = embedUi.editFieldValue(existingEmbeds, textInput, fieldIndex);
		message.editMessageEmbeds(editedEmbeds).queue();

		var reply = Formatter.replies(String.format("Slot #%d value has been updated.", fieldIndex + 1));
		event.getHook().editOriginal(reply).queue();
	}

	/**
	 * Generic ephemeral reply in response to a modal submission, mostly for validation errors or development feedback.
	 */
	public void ephemeral(@Nonnull ModalInteractionEvent event, String message) {
		event.reply(message).setEphemeral(true).queue();
	}

}