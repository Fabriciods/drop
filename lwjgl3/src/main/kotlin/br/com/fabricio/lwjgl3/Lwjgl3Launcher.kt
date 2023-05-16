package br.com.fabricio.lwjgl3

import br.com.fabricio.Main
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration


fun main() {
    Lwjgl3Application(Main(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("drop")
        useVsync(true)
        setForegroundFPS(60)
        setWindowedMode(640, 480)
        setWindowIcon(
            "libgdx128.png",
            "libgdx64.png",
            "libgdx32.png",
            "libgdx16.png"
        )
    })
}


