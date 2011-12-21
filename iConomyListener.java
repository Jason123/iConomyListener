import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//test
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VoteListener;

import net.milkbowl.vault.economy.Economy;

/**
 * A VoteListener that rewards via iConomy.
 * 
 * @author Blake Beaupain
 */
public class iConomyListener implements VoteListener {

	/** The logger instance. */
	private static Logger logger = Logger.getLogger("iConomyListener");

	/** The amount to reward. */
	private int amount = 100;

	/**
	 * Instantiates a new iConomy listener.
	 */
	public iConomyListener() {
		Properties props = new Properties();
		try {
			// Create the file if it doesn't exist.
			File configFile = new File("./plugins/Votifier/iConomyListener.ini");
			if (!configFile.exists()) {
				configFile.createNewFile();

				// Load the configuration.
				props.load(new FileReader(configFile));

				// Write the default configuration.
				props.setProperty("reward_amount", Integer.toString(amount));
				props.store(new FileWriter(configFile), "iConomy Listener Configuration");
			} else {
				// Load the configuration.
				props.load(new FileReader(configFile));
				setupEconomy();
			}

			amount = Integer.parseInt(props.getProperty("reward_amount", "100"));
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Unable to load iConomyListener.ini, using default reward value of: " + amount);
		}
	}
	
	
	public static Economy economy = null;
	
    private Boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

	@Override
	public void voteMade(Vote vote) {
		String username = vote.getUsername();
		Player player = Bukkit.getServer().getPlayer(username);
		
		
		if (player != null){
		economy.depositPlayer(username, amount);
		player.sendMessage("Thanks for voting on " + vote.getServiceName() + "!");
		player.sendMessage(amount + " has been added to your iConomy balance.");
		
			}
	}
}