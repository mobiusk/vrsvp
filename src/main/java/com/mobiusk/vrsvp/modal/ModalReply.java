package com.mobiusk.vrsvp.modal;

import com.mobiusk.vrsvp.button.ButtonUi;
import com.mobiusk.vrsvp.embed.EmbedUi;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

import javax.annotation.Nonnull;

public class ModalReply {

	/**
	 * Build an RSVP form everyone can see with a given number of time slot, each with an index and incremented timestamp title.
	 */
	public void createEmbedFormFromAdmin(@Nonnull ModalInteractionEvent event, String description) {
		event.reply("")
			.addEmbeds(EmbedUi.createEmbedDescriptionFromAdmin(description))
			.addActionRow(ButtonUi.buildRsvpActionPrompts())
			.queue();
	}

	/**
	 * Edit a message by changing the description and then edit the original ephemeral message to confirm the action.
	 */
	public void editEmbedDescriptionFromAdmin(
		@Nonnull ModalInteractionEvent event,
		@Nonnull Message message,
		String description
	) {

		var embed = EmbedUi.editEmbedDescriptionFromAdmin(message, description);
		message.editMessageEmbeds(embed).queue();

		event.editMessage("Description has been updated.").queue();
	}

	/**
	 * Generic ephemeral reply in response to a modal submission, mostly for validation errors or development feedback.
	 */
	public void ephemeral(@Nonnull ModalInteractionEvent event, String message) {
		event.reply(message).setEphemeral(true).queue();
	}

}
