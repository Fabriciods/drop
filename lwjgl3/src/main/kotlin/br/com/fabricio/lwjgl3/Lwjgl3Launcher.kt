package br.com.fabricio.lwjgl3

import br.com.fabricio.Drop
import br.com.fabricio.Main
import br.com.fabricio.MainMenuScreen
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration


fun main() {
    Lwjgl3Application(Drop(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("drop")
        useVsync(true)
        setForegroundFPS(60)
        setWindowedMode(800, 480)
        setWindowIcon(
            "libgdx128.png",
            "libgdx64.png",
            "libgdx32.png",
            "libgdx16.png"
        )
    })
}


