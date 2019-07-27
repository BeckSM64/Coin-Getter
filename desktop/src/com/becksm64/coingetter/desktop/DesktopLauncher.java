package com.becksm64.coingetter.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.becksm64.coingetter.CoinGetter;

public class DesktopLauncher {
	public static void main (String[] arg) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = 600;
		config.height = 300;

		//Add icons
		config.addIcon("icons/icon_128.png", Files.FileType.Internal);
        config.addIcon("icons/icon_32.png", Files.FileType.Internal);
        config.addIcon("icons/icon_16.png", Files.FileType.Internal);

        //Prohibit window resizing
        config.resizable = false;

		new LwjglApplication(new CoinGetter(), config);
	}
}
