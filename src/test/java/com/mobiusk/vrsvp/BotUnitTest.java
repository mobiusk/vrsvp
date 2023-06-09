package com.mobiusk.vrsvp;

import com.mobiusk.vrsvp.button.ButtonListener;
import com.mobiusk.vrsvp.command.SlashCommandListener;
import com.mobiusk.vrsvp.modal.ModalListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BotUnitTest extends TestBase {

	@InjectMocks private Bot bot;

	@Spy private JDABuilder jdaBuilder = Bot.create("Testing");

	@Mock private JDA jda;
	@Mock private CommandListUpdateAction commandListUpdateAction;

	@BeforeEach
	public void beforeEach() {

		doReturn(jda).when(jdaBuilder).build();

		when(jda.updateCommands()).thenReturn(commandListUpdateAction);
		when(jda.getGuilds()).thenReturn(Collections.emptyList());

		when(commandListUpdateAction.addCommands(any(CommandData.class))).thenReturn(commandListUpdateAction);
	}

	@Test
	void startingDiscordBotWithoutTokenThrowsException() {

		// In direct contradiction to what we set up in beforeEach for other tests, check this edge case
		doCallRealMethod().when(jdaBuilder).build();

		jdaBuilder.setToken(null);
		var ex = assertThrows(IllegalArgumentException.class, () -> bot.start());
		assertEquals("Token may not be null", ex.getMessage());
	}

	@Test
	void startingDiscordBotWaitsForItToBeReady() throws InterruptedException {

		startDiscordBot();

		verify(jdaBuilder).build();
		verify(jda).awaitReady();
	}

	@Test
	void eventListenersAreAdded() {

		startDiscordBot();

		verify(jda).addEventListener(any(ButtonListener.class));
		verify(jda).addEventListener(any(SlashCommandListener.class));
		verify(jda).addEventListener(any(ModalListener.class));
	}

	@Test
	void knownGuildsAreLogged() {

		startDiscordBot();

		verify(jda).getGuilds();
	}

	@Test
	void botSlashCommandsAreAdded() {

		startDiscordBot();

		verify(jda).updateCommands();
		verify(commandListUpdateAction).addCommands(any(CommandData.class));
		verify(commandListUpdateAction).queue();
	}

	// Test utility method(s)

	private void startDiscordBot() {
		try {
			bot.start();
		} catch (InterruptedException e) {
			fail("Discord bot should have started without interruption for this test case", e);
		}
	}

}
