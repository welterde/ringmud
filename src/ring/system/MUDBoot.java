package ring.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import ring.commands.CommandHandler;
import ring.commands.CommandIndexer;
import ring.commands.IndexerFactory;
import ring.deployer.DeployedMUDFactory;
import ring.events.EventDispatcher;
import ring.events.EventLoader;
import ring.movement.WorldBuilder;
import ring.persistence.ExistDB;
import ring.persistence.ResourceList;
import ring.persistence.XQuery;
import ring.python.Interpreter;
import ring.world.Ticker;

/**
 * This class is what "boots" the MUD. It retrieves information from the various
 * data files and then loads them into memory. The boot sequence is very
 * defined; the MUD first loads all effects defined in files, followed by class
 * features and then MobileClasses. It then continues with items, NPCs, and
 * finally the world itself. Each section of the boot depends on the previous
 * section being finished and having all information available.
 * 
 * @author jeff
 */
public class MUDBoot {
	/**
	 * Boots the mud server.
	 */
	public static void boot() {
		System.out.println("Loading RingMUD.");
		
		System.out.println("Loading Jython...");
		Interpreter.INSTANCE.getInterpreter();
		
		//Load all event handlers
		System.out.println("Loading event handlers...");
		loadEventHandlers();
		
		//Synchronize with static
		System.out.println("Synchronziing with STATIC...");
		System.err.println("WARNING: Syncing not implemented yet.");
		
		//Restore world state from DB
		System.out.println("Restoring world state...");
		System.err.println("WARNING: Restoring of world state not implemented yet.");

		//Load commands
		System.out.println("Loading commands...");
		loadCommands();

		//Load effects

		//Start the world ticker
		System.out.println("Starting the world ticker...");
		Ticker ticker = Ticker.getTicker();
		Thread t = new Thread(ticker);
		t.setName("World Ticker");
		t.start();

		// Load classes

		// Load items

		// Load NPCs

		// Load the universe (world)
		System.out.println("Buidling world...");
		try {
			WorldBuilder.buildWorld();
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Done loading RingMUD.");
	}

	/**
	 * Loads both internal commands (in packages) and Jython-based commands
	 * (from script files).
	 */
	private static void loadCommands() {
		Properties pkgProps = MUDConfig.getPluginProperties("pkgIndexer");
		Properties jythonProps = MUDConfig.getPluginProperties("jythonIndexer");

		CommandIndexer pkgIndexer = IndexerFactory.getIndexer(
				"ring.commands.PackageIndexer", pkgProps);
		CommandHandler.addCommands(pkgIndexer.getCommands());

		CommandIndexer jythonIndexer = IndexerFactory.getIndexer(
				"ring.commands.JythonIndexer", jythonProps);
		CommandHandler.addCommands(jythonIndexer.getCommands());
	}

	/**
	 * Loads event handlers.
	 */
	private static void loadEventHandlers() {
		EventLoader loader = new EventLoader();
		try {
			loader.loadEvents();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
