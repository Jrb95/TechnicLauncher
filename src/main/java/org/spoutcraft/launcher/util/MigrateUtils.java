/*
 * This file is part of Technic Launcher.
 * Copyright (C) 2013 Syndicate, LLC
 *
 * Technic Launcher is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Technic Launcher is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Technic Launcher.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.spoutcraft.launcher.util;

import net.technicpack.launchercore.install.InstalledPack;
import net.technicpack.launchercore.install.InstalledPacks;
import net.technicpack.launchercore.util.Utils;
import org.spoutcraft.launcher.settings.OldSettings;
import net.technicpack.launchercore.util.Settings;
import org.spoutcraft.launcher.util.yml.YAMLFormat;
import org.spoutcraft.launcher.util.yml.YAMLProcessor;

import java.io.File;

public class MigrateUtils {

	@SuppressWarnings("deprecation")
	public static void migrateSettings() {
		File settingsFile = new File(Utils.getSettingsDirectory(), "settings.yml");
		if (!settingsFile.exists()) {
			Settings.load();
			return;
		}

		System.out.println("Old settings found, migrating...");

		YAMLProcessor settings = new YAMLProcessor(settingsFile, false, YAMLFormat.EXTENDED);
		OldSettings.setYAML(settings);

		Settings.setBuild(OldSettings.getLauncherBuild());
		Settings.setMemory(OldSettings.getMemory());
		Settings.setBuildStream(OldSettings.getBuildStream());
		Settings.setDirectory(OldSettings.getLauncherDir());
		Settings.setShowConsole(OldSettings.getShowLauncherConsole());

		InstalledPacks installedPacks = new InstalledPacks();

		for (String modpack : OldSettings.getInstalledPacks()) {
			boolean custom = OldSettings.isPackCustom(modpack);
			String build = OldSettings.getModpackBuild(modpack);
			String directory = OldSettings.getPackDirectory(modpack);
			InstalledPack pack = new InstalledPack(modpack, custom, build, directory);
			installedPacks.add(pack);
		}

		File lastLogin = new File(Utils.getSettingsDirectory(), "lastlogin");
		lastLogin.delete();
		settingsFile.delete();
		System.out.println("Finished migration");
		System.out.println(Settings.instance);
	}

}