package br.com.fabricio

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.TimeUtils


class Main : ApplicationAdapter() {
    private lateinit var dropImage: Texture
    private lateinit var bucketImage: Texture
    private lateinit var dropSound: Sound
    private lateinit var rainMusic: Music
    private lateinit var camera: OrthographicCamera
    private lateinit var batch: SpriteBatch
    private lateinit var bucket: Rectangle
    private lateinit var rainDrops: Array<Rectangle>
    private var lastDropTime: Long = 0
    private lateinit var touchPos: Vector3
    override fun create() {
        camera = OrthographicCamera()
        camera.setToOrtho(false, 800f, 400f)
        batch = SpriteBatch()

        dropImage = Texture(Gdx.files.internal("drop.png"))
        bucketImage = Texture(Gdx.files.internal("bucket.png"))

        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"))
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"))
        rainMusic.isLooping = true
        rainMusic.play()

        bucket = Rectangle()
        bucket.x = (800 / 2 - 64 / 2).toFloat()
        bucket.y = 20f
        bucket.width = 64f
        bucket.height = 64f

        rainDrops = Array()
        spawnDrops()
    }

    override fun render() {
        ScreenUtils.clear(0f, 0f, 0.2f, 1f)
        camera.update()
        batch.projectionMatrix = camera.combined
        batch.begin()
        batch.draw(bucketImage, bucket.x, bucket.y)
        for (rainDrop in rainDrops){
            batch.draw(dropImage,rainDrop.x,rainDrop.y)
        }
        batch.end()
        if (Gdx.input.isTouched) {
            touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            camera.unproject(touchPos) //converte as coordenadas do toque em coordenadas da camera
            bucket.x = touchPos.x - (64 / 2)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.deltaTime
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.deltaTime
        if (bucket.x < 0) bucket.x = 0f
        if (bucket.x > 800 - 64) bucket.x = 800f - 64f

        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnDrops()

        val iter: MutableIterator<Rectangle> = rainDrops.iterator()
        while (iter.hasNext()) {
            val raindrop = iter.next()
            raindrop.y -= 200 * Gdx.graphics.deltaTime
            if (raindrop.y + 64 < 0) iter.remove()
            if(raindrop.overlaps(bucket)){
                dropSound.play()
                iter.remove()
            }
        }
    }

    private fun spawnDrops() {
        val rainDrop = Rectangle()
        rainDrop.x = MathUtils.random(0f, 800f - 64f)
        rainDrop.y = 480f
        rainDrop.width = 64f
        rainDrop.height = 64f
        rainDrops.add(rainDrop)
        lastDropTime = TimeUtils.nanoTime()
    }

    override fun dispose() {
        dropImage.dispose()
        bucketImage.dispose()
        dropSound.dispose()
        rainMusic.dispose()
        batch.dispose()
    }
}
