package ring.aspects;

import java.util.logging.Logger;

import ring.commands.*;
import ring.events.EventDispatcher;

/**
 * Aspects that captures events and logging relating to commands.
 * @author projectmoon
 *
 */
public privileged aspect Commands {
	private static final String COMMAND_ON_BEGIN = "onBegin";
	private static final String COMMAND_ON_END = "onEnd";
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	/**
	 * Matches the execute method of commands. Used for sending an onBegin and
	 * onEnd events for commands, and logging information related to command
	 * execution.
	 * @param cmd
	 */
	pointcut commandExecute(Command cmd):
		call(CommandResult Command.execute(..)) &&
		target(cmd);
	
	before(Command cmd): commandExecute(cmd) {
		System.out.println("Begin executing: " + cmd);
		EventDispatcher.dispatchGlobal(COMMAND_ON_BEGIN, cmd);
	}
	
	after(Command cmd) returning(CommandResult result): commandExecute(cmd) {
		System.out.println("End executing: " + cmd);
		EventDispatcher.dispatchGlobal(COMMAND_ON_END, cmd, result);
		
		if (result == null) {
			log.warning("Execution of command [" + cmd.getCommandName() + "] did not return a CommandResult!");
		}
	}
	
	/**
	 * Used for logging of when a command is sent.
	 * @param command
	 */
	pointcut sendCommand(String command):
		call(CommandResult CommandHandler.sendCommand(String)) && args(command);
	
	before(CommandHandler handler, String command): sendCommand(command) && target(handler) {
		log.info("received from [" + handler.sender.toString() + "]: " + command);
	}
	
	after(CommandHandler handler, String command): sendCommand(command) && target(handler) {
		log.info("handled command [" + command + "] from " + handler.sender.toString());
	}
	
}
