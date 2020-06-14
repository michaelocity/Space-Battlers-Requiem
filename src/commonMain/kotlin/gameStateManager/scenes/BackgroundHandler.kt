package gameStateManager.scenes

import com.soywiz.klock.milliseconds
import com.soywiz.korge.animate.animate
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.async
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import org.jbox2d.common.MathUtils.Companion.round
import org.jbox2d.common.Vec2
import kotlin.math.round
import kotlin.time.seconds
import com.soywiz.korio.async.launchImmediately
import kotlinx.coroutines.GlobalScope

class BackgroundHandler(private val bm : Bitmap,  val spriteSize : Int =64,  var scale :Double = 32.0){

    var animations = ArrayList<Sprite>()
    val maxBackgroundTiles =  258

        fun updateSpritePos(playerPos : Vec2, currentScene : Container)
        {
            var relativeX = playerPos.x%spriteSize
            var relativeY = playerPos.y%spriteSize

            if(relativeX<spriteSize/2&&relativeY<spriteSize/2)
            {

                animations.add(createAnimation(playerPos,-1,-1))
                currentScene.addChildAt(animations.get(animations.size-1),0)
                animations.add(createAnimation(playerPos,0,-1))
                currentScene.addChildAt(animations.get(animations.size-1),0)
                animations.add(createAnimation(playerPos,-1,0))
                currentScene.addChildAt(animations.get(animations.size-1),0)


            }
            else if(relativeX<spriteSize/2&&relativeY>spriteSize/2)
            {
                animations.add(createAnimation(playerPos,-1,0))
                currentScene.addChildAt(animations.get(animations.size-1),0)
                animations.add(createAnimation(playerPos,-1,1))
                currentScene.addChildAt(animations.get(animations.size-1),0)
                animations.add(createAnimation(playerPos,0,1))
                currentScene.addChildAt(animations.get(animations.size-1),0)
            }

            else if(relativeX>spriteSize/2&&relativeY>spriteSize/2)
            {
                animations.add(createAnimation(playerPos,1,1))
                currentScene.addChildAt(animations.get(animations.size-1),0)
                animations.add(createAnimation(playerPos,0,1))
                currentScene.addChildAt(animations.get(animations.size-1),0)
                animations.add(createAnimation(playerPos,1,0))
                currentScene.addChildAt(animations.get(animations.size-1),0)
            }
            else
            {
                animations.add(createAnimation(playerPos,1,-1))
                currentScene.addChildAt(animations.get(animations.size-1),0)
                animations.add(createAnimation(playerPos,0,-1))
                currentScene.addChildAt(animations.get(animations.size-1),0)
                animations.add(createAnimation(playerPos,1,0))
                currentScene.addChildAt(animations.get(animations.size-1),0)
            }

            deleteUnusedTextures(currentScene)

        }

        private fun deleteUnusedTextures(currentScene : Container)
        {
            println(animations.size)
            if(animations.size>maxBackgroundTiles) {
                var job = GlobalScope.launchImmediately {
                    while (animations.size > maxBackgroundTiles / 2) {
                        currentScene.removeChild(animations.removeAt(0))
                    }
                }
            }
        }

        private fun createAnimation(playerPos: Vec2, xOffset: Int=0,yOffset :Int =0) : Sprite
        {
            val animation1 = Sprite(SpriteAnimation(
                    spriteMap = bm,
                    spriteWidth = spriteSize,
                    spriteHeight = spriteSize,
                    columns = 4,
                    rows = 1))
            animation1.scale(scale)

            animation1.xy((playerPos.x.toInt()/(spriteSize*scale).toInt()+xOffset)*spriteSize*scale,
                    (playerPos.y.toInt()/(spriteSize*scale).toInt()+yOffset)*spriteSize*scale)

            animation1.center()
            animation1.playAnimation(spriteDisplayTime = 125.milliseconds)
            return animation1
        }
    }

