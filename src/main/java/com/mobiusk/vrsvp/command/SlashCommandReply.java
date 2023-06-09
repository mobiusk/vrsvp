package com.mobiusk.vrsvp.command;

import com.mobiusk.vrsvp.modal.ModalEnum;
import com.mobiusk.vrsvp.modal.ModalUi;
import com.mobiusk.vrsvp.util.Parser;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.Objects;

@RequiredArgsConstructor
public class SlashCommandReply {

	/**
	 * Build a modal with generated event title and slots for admins to confirm/edit before sending message to channel.
	 */
	public void rsvpCreation(@Nonnull SlashCommandInteractionEvent event, @Nonnull SlashCommandInputs inputs) {

		var description = buildDescription(inputs);
		var modal = ModalUi.editText(ModalEnum.EVENT_CREATION, description);

		event.replyModal(modal).queue();
	}

	/**
	 * Build an RSVP form everyone can see with a given number of time slot, each with an index and incremented timestamp title.
	 */
	private String buildDescription(@Nonnull SlashCommandInputs inputs) {

		var slotDurationInSeconds = inputs.getDurationInMinutes() * 60;

		var description = new LinkedList<String>();
		description.add("**New Event**\n");
		description.add(String.format("- Starts <t:%d:R> on <t:%d:F>", inputs.getStartTimestamp(), inputs.getStartTimestamp()));
		description.add(String.format("- Each slot is %d minutes long", inputs.getDurationInMinutes()));
		description.add(buildRsvpLimitAddendum(SlashCommandEnum.RSVP_LIMIT_PER_SLOT, inputs.getRsvpLimitPerSlot()));
		description.add(buildRsvpLimitAddendum(SlashCommandEnum.RSVP_LIMIT_PER_PERSON, inputs.getRsvpLimitPerPerson()));
		description.add("");

		for (var slotIndex = 0; slotIndex < inputs.getSlots(); slotIndex++) {
			var slotTimestamp = inputs.getStartTimestamp() + (slotDurationInSeconds * slotIndex);
			var line = String.format("> #%d%s<t:%d:t>", slotIndex + 1, Parser.SIGNUP_DELIMITER, slotTimestamp);
			description.add(line);
		}

		description.removeIf(Objects::isNull);

		return String.join(Parser.SLOT_DELIMITER, description);
	}

	private String buildRsvpLimitAddendum(SlashCommandEnum slashCommandEnum, Integer limit) {

		if (limit != null) {
			return String.format("- %s: %d", slashCommandEnum.getDescription(), limit);
		}

		return null;
	}

}
