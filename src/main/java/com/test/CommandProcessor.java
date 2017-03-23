package com.test;

import com.test.errors.ItemNotInMarket;
import com.test.errors.UserIsAlreadyLogged;
import com.test.errors.UserNotLogged;
import com.test.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * Created by Ken on 23.03.2017.
 */
@Component
public class CommandProcessor {

	@Autowired
	private MarketService marketService;

	enum Command {
		LOGOUT("logout", 0),
		LOGIN("login", 1),
		VIEWSHOP("viewshop", 0),
		MYINFO("myinfo", 0),
		BUY("buy", 1),
		SELL("sell", 1);
		
		final String command;
		final Integer arguments;

		private Command(String var3, Integer var4) {
			this.command = var3;
			this.arguments = var4;
		}

		public String toString() {
			return "-" + this.name().toLowerCase();
		}

		public static String getCommands(){
			StringJoiner stringJoiner = new StringJoiner(", ");
			Arrays.stream(Command.values()).map(i -> i.command).sorted()
					.forEach(stringJoiner::add);
			return stringJoiner.toString();
		}

	}

	private CommandDefinition parseCommand(String command) throws
			WrongParametersCount, WrongCommandException, EmptyCommandException {
		String[] parameters = command.split(" ");
		if(parameters.length > 0 && !parameters[0].isEmpty()) {
			for (Command possibleCommand : Command.values()) {
				if (possibleCommand.command.equalsIgnoreCase(parameters[0])) {
					if(possibleCommand.arguments == parameters.length -
							1) {
						return new CommandDefinition(possibleCommand,
								Arrays.copyOfRange(parameters, 1,
										parameters.length));
					} else {
						throw new WrongParametersCount(possibleCommand,
								parameters.length - 1);
					}
				}
			}
			throw new WrongCommandException(parameters[0]);
		} else {
			throw new EmptyCommandException();
		}
	}

	private class EmptyCommandException extends Throwable {
		@Override
		public String toString() {
			return "The command is empty";
		}
	}

	private class WrongParametersCount extends Throwable {

		private final Command command;
		private final Integer currentParams;

		public WrongParametersCount(Command possibleCommand, Integer
				currentParams) {
			command = possibleCommand;
			this.currentParams = currentParams;
		}

		@Override
		public String toString() {
			return "Wrong parameters count for command "
					+ command.command +
					". Got " + currentParams + " parameters, need " + command.arguments;
		}
	}

	private class WrongCommandException extends Throwable {

		private final String command;

		public WrongCommandException(String command){
			this.command = command;
		}
		@Override
		public String toString() {
			return String.format("Command \"%s\" is wrong, available commands" +
					" are [%s]", command, Command.getCommands());
		}
	}

	public StringBuilder processCommand(String stringCommand){
		StringBuilder stringBuilder = new StringBuilder("OK");
		try {
			CommandDefinition commandDefinition = parseCommand(stringCommand);
			switch (commandDefinition.getCommand()) {
				case BUY:
					marketService.buy(commandDefinition.getParameters()[0]);
					break;
				case LOGIN:
					marketService.login(commandDefinition.getParameters()[0]);
					break;
				case LOGOUT:
					marketService.logout();
					break;
				case MYINFO:
					stringBuilder = marketService.myinfo();
					break;
				case SELL:
					marketService.sell(commandDefinition.getParameters()[0]);
					break;
				case VIEWSHOP:
					stringBuilder = marketService.viewShop();
					break;
				default:
					throw new WrongCommandException(commandDefinition.command.command);
			}
		} catch (WrongParametersCount wrongParametersCount) {
			return outString(wrongParametersCount.toString());
		} catch (WrongCommandException wrongCommandException) {
			return outString(wrongCommandException.toString());
		} catch (EmptyCommandException e) {
			return outString(e.toString());
		} catch (UserIsAlreadyLogged userIsAlreadyLogged) {
			return outString(userIsAlreadyLogged.toString());
		} catch (ItemNotInMarket itemNotInMarket) {
			return outString(itemNotInMarket.toString());
		} catch (UserNotLogged userNotLogged) {
			return outString(userNotLogged.toString());
		}
		return stringBuilder;
	}

	private StringBuilder outString(String output){
		return new StringBuilder(output);
	}

	private static class CommandDefinition {
		private final Command command;
		private final String[] parameters;

		CommandDefinition(Command command, String[] parameters){
			this.command = command;
			this.parameters = parameters;
		}

		public Command getCommand() {
			return command;
		}

		public String[] getParameters() {
			return parameters;
		}
	}
}
