package br.com.fabricio

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.TimeUtils


class GameScreen(private val game: Drop) : Screen {

    private var dropImage: Texture = Texture(Gdx.files.internal("drop.png"))
    private var bucketImage: Texture = Texture(Gdx.files.internal("bucket.png"))
    private var dropSound: Sound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"))
    private var rainMusic: Music = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"))
    private var camera: OrthographicCamera
    private var bucket: Rectangle
    private var rainDrops: Array<Rectangle>
    private var lastDropTime: Long = 0
    private var dropsGathered: Int = 0

    init {
        rainMusic.isLooping = true
        camera = OrthographicCamera()
        camera.setToOrtho(false, 800f, 480f)

        bucket = Rectangle()
        bucket.x = (800 / 2 - 62 / 2).toFloat()
        bucket.y = 20f

        bucket.width = 64f
        bucket.height = 64f

        rainDrops = Array<Rectangle>()
        spawnRaindrop()
    }

    private fun spawnRaindrop() {
        val rainDrop = Rectangle()
        rainDrop.x = MathUtils.random(0, 800 - 64).toFloat()
        rainDrop.y = 480f
        rainDrop.width = 64f
        rainDrop.height = 64f
        rainDrops.add(rainDrop)
        lastDropTime = TimeUtils.nanoTime()
    }

    override fun show() {
        rainMusic.play()
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(0f, 0f, 0.2f, 1f)

        camera.update()

        game.batch.projectionMatrix = camera.combined

        game.batch.begin()
        game.font.draw(game.batch, "Drops Collected $dropsGathered", 0f, 480f)
        game.batch.draw(bucketImage, bucket.x, bucket.y, bucket.width, bucket.height)
        for (raindrop in rainDrops) {
            game.batch.draw(dropImage, raindrop.x, raindrop.y)
        }
        game.batch.end()

        if (Gdx.input.isTouched) {
            val touchPos = Vector3()
            touchPos[Gdx.input.x.toFloat(), Gdx.input.y.toFloat()] = 0f
            camera.unproject(touchPos)
            bucket.x = touchPos.x - 64 / 2
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.deltaTime
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.deltaTime

        if (bucket.x < 0f)
            bucket.x = 0f
        if (bucket.x > 800f - 64f)
            bucket.x = 800f - 64f
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
            spawnRaindrop()
        val iter: MutableIterator<Rectangle> = rainDrops.iterator()
        while (iter.hasNext()) {
            val raindrop = iter.next()
            raindrop.y -= 200 * Gdx.graphics.deltaTime
            if (raindrop.y + 64 < 0) iter.remove()
            if (raindrop.overlaps(bucket)) {
                dropsGathered++
                dropSound.play()
                iter.remove()
            }
        }
    }

    override fun resize(width: Int, height: Int) {

    }

    override fun pause() {
    }

    override fun resume() {

    }

    override fun hide() {

    }

    override fun dispose() {
        dropImage.dispose();
        bucketImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
    }
}
