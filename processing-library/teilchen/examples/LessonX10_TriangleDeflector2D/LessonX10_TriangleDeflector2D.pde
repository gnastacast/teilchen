import teilchen.*; 
import teilchen.behavior.*; 
import teilchen.constraint.*; 
import teilchen.cubicle.*; 
import teilchen.force.*; 
import teilchen.integration.*; 
import teilchen.util.*; 


/*
 * this sketch demonstrates how to use `TriangleDeflectors` in a 2D context to make particles
 * bounce off a triangle ( that looks like a line ). it also demonstrates how to use
 * `MortalParticle` to remove particles automatically once they leave the screen.
 */
Physics mPhysics;
TriangleDeflector mTriangleDeflector;
void settings() {
    size(640, 480, P3D);
}
void setup() {
    /* physics */
    mPhysics = new Physics();
    Gravity myGravity = new Gravity(0, 20, 0);
    mPhysics.add(myGravity);
    /* triangle deflector */
    final float mPadding = 50;
    mTriangleDeflector = teilchen.util.Util.createTriangleDeflector2D(mPadding,
                                                                      height - mPadding - 40,
                                                                      width - mPadding,
                                                                      height - mPadding + 40,
                                                                      1.0f);
    mPhysics.add(mTriangleDeflector);
}
void draw() {
    final float mDeltaTime = 1.0f / frameRate;
    mPhysics.step(mDeltaTime);
    /* draw particles */
    background(255);
    for (int i = 0; i < mPhysics.particles().size(); i++) {
        Particle mParticle = mPhysics.particles(i);
        stroke(0, 127);
        if (mParticle.tagged()) {
            fill(255, 127, 64);
        } else {
            fill(0, 32);
        }
        pushMatrix();
        translate(mParticle.position().x, mParticle.position().y);
        ellipse(0, 0, 10, 10);
        popMatrix();
    }
    /* draw deflectors */
    noFill();
    stroke(0);
    line(mTriangleDeflector.a().x, mTriangleDeflector.a().y, mTriangleDeflector.b().x, mTriangleDeflector.b().y);
    /* finally remove the collision tag */
    mPhysics.removeTags();
    if (mousePressed) {
        /* create and add a particle to the system */
        MyMortalParticle mParticle = new MyMortalParticle();
        mPhysics.add(mParticle);
        /* set particle to mouse position with random velocity */
        mParticle.position().set(mouseX, mouseY);
        mParticle.velocity().set(random(-20, 20), 0);
    }
}
class MyMortalParticle extends MortalParticle {
    boolean isDead() {
        return position().y > height || still();
    }
}
