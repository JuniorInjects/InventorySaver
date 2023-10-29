package de.juniorinjects.inventorysaver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTException;
import org.jglrxavpok.hephaistos.nbt.NBTType;
import org.jglrxavpok.hephaistos.parser.SNBTParser;

import net.minestom.server.entity.Player;
import net.minestom.server.extensions.Extension;
import net.minestom.server.item.ItemStack;

/**
 * 
 * Save the inventory of a player with the help of minestom (Minestom is required).
 *
 * @author  JuniorInjects
 * @since   1.0
 * 
 */
public class InventorySaver extends Extension {

	private static Player player;
	private static File file;
	private static String folder;

    /**
     *
     * Creates {@code folder} given from you and initializes all variables.
     *
     * @param   player  The player you want to save the inventory.
     * @param   folder  The folder you want to save the inventory data. (if is null saves files to same folder as your Minestom File is in.
     * 
     * @throws  NullPointerException
     *          If the {@code player} argument is {@code null}
     * 
     */
	@SuppressWarnings("static-access")
	public InventorySaver(Player player, String folder) {
		if(folder!=null) {
	        if(!new File(folder).exists()) {
	        	new File(folder).mkdir();
	        }
			this.file = new File(folder+"/"+player.getUuid().toString()+".txt");
			this.folder = folder+"/";
		}else {
			this.file = new File(player.getUuid().toString()+".txt");
			this.folder = "";
		}
		this.player = player;
	}

    /**
    *
    * Loads the inventory data of a player from a file.
    * 
    * @return  {@code true} if the player's inventory data already exists and
    *          could be deserialized.; {@code false} if the player had no inventory data before.
    *
    */
	public boolean deserializ() {
		String deserialized = null;
		if(!fileExist()) return false;
		if(file.length()==0) return false;
		try {
			Scanner reader = new Scanner(file);
			deserialized = reader.nextLine();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
		    if(deserialized!=null) {
				@SuppressWarnings({ "unchecked", "resource" })
				List<NBTCompound> list = (List<NBTCompound>) new SNBTParser(new StringReader(deserialized)).parse().getValue();
			    for (int i = 0; i < list.size(); i++) {
			        player.getInventory().setItemStack(i, ItemStack.fromItemNBT(list.get(i)));
			    }
			    return true;
		    }
		} catch (NBTException e) {
		    e.printStackTrace();
		}
		return false;
	}

    /**
    *
    * Writes the inventory data of the player to a string and saves it in file.
    * 
    */
	public void serialize() {
		String serialized = NBT.List(NBTType.TAG_Compound, Arrays.stream(player.getInventory().getItemStacks())
		        .map(ItemStack::toItemNBT)
		        .toList()).toSNBT();
		saveToFile(serialized);
	}

    /**
    *
    * Saves the inventory data to file.
    * 
    * @param   save  The inventory data as String.
    * 
    */
	private void saveToFile(String save) {
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(save);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    /**
    *
    * Checks if inventory data of player already exists.
    *
    * @return  {@code true} if the player's inventory data already exists.; 
    *          {@code false} if the player had no inventory data before and creates 
    *          a new File with the uuid of the player.
    *
    */
	private boolean fileExist() {
        if(!new File(folder+player.getUuid().toString()+".txt").exists()) {
        	try {
				new File(folder+player.getUuid().toString()+".txt").createNewFile();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
			}
        }else
        	return true;
        return false;
	}

    /**
    *
    * @return  {@code file} in wich the inventory data will be saved.
    * @since 1.0.1
    *
    */
	public File getFile() {
		return file;
	}

    /**
    *
    * Changing the file in wich the inventory data will be saved. It is recomended to do it before the {@link #serialize serialize} method.
    *
    * @since 1.0.1
    *
    */
	@SuppressWarnings("static-access")
	public void setFile(File newFile) {
		this.file = newFile;
	}

	/**
	 * Just initialize method for Minestom Extension
	 */
    @Override
    public void initialize() {
    	System.out.println("[InventorySaver] thanks for using InventorySaver.");
    }
	/**
	 * Just terminate method for Minestom Extension
	 */
	@Override
	public void terminate() { }
}
