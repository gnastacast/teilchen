import teilchen.*; 
import teilchen.behavior.*; 
import teilchen.constraint.*; 
import teilchen.cubicle.*; 
import teilchen.force.*; 
import teilchen.integration.*; 
import teilchen.util.*; 


/*
 * this sketch demonstrates how to create a `Spring` that connects two particles. it also
 * demonstrates how to create a `ViscousDrag` to slow down particle motion over time.
 */
Physics mPhysics;
Spring mSpring;
void settings() {
    size(640, 480, P3D);
}
void setup() {
    /* create a particle system */
    mPhysics = new Physics();
    /* create a viscous force that slows down all motion; 0 means no slowing down. */
    ViscousDrag myDrag = new ViscousDrag(0.25f);
    mPhysics.add(myDrag);
    /* create two particles that we can connect with a spring */
    Particle myA = mPhysics.makeParticle();
    myA.position().set(width / 2.0f - 50, height / 2.0f);
    Particle myB = mPhysics.makeParticle();
    myB.position().set(width / 2.0f + 50, height / 2.0f);
    /* create a spring force that connects two particles.
     * note that there is more than one way to create a spring.
     * in our case the restlength of the spring is defined by the
     * particles current position.
     */
    mSpring = mPhysics.makeSpring(myA, myB);
}
void draw() {
    /* set first particle to mouse position */
    if (mousePressed) {
        mSpring.a().position().set(mouseX, mouseY);
    }
    /* update the particle system */
    final float mDeltaTime = 1.0f / frameRate;
    mPhysics.step(mDeltaTime);
    /* draw particles and connecting line */
    background(255);
    noFill();
    stroke(255, 0, 127, 64);
    line(mSpring.a().position().x, mSpring.a().position().y,
         mSpring.b().position().x, mSpring.b().position().y);
    fill(245);
    stroke(164);
    ellipse(mSpring.a().position().x, mSpring.a().position().y, 12, 12);
    ellipse(mSpring.b().position().x, mSpring.b().position().y, 12, 12);
}
