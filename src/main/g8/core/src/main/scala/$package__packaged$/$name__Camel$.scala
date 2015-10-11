package $package$

import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.{Gdx, Game}

class $name;format="Camel"$ extends Game {
    override def create() {}

    override def render(): Unit = {
        Gdx.gl.glClearColor(MathUtils.random(),MathUtils.random(),MathUtils.random(),1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }
}
