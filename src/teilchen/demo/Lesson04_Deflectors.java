/*
 * Particles
 *
 * Copyright (C) 2012
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * {@link http://www.gnu.org/licenses/lgpl.html}
 *
 */


package teilchen.demo;


import processing.core.PApplet;
import teilchen.Particle;
import teilchen.Physics;
import teilchen.ShortLivedParticle;
import teilchen.force.Gravity;
import teilchen.force.PlaneDeflector;
import teilchen.force.ViscousDrag;


/**
 * this sketch shows
 * 1 how to create and use plane deflectors
 * 2 how to use 'ShortLivedParticle'
 */
public class Lesson04_Deflectors
        extends PApplet {

    private Physics mPhysics;

    private PlaneDeflector mDeflector;

    public void setup() {
        size(640, 480, OPENGL);
        smooth();
        frameRate(30);

        /* create a particle system */
        mPhysics = new Physics();

        /* create a deflector and add it to the particle system.
         * the that defines the deflection area is defined by an
         * origin and a normal. this also means that the plane s size
         * is infinite.
         * note that there is also a triangle delfector that is constraint
         * by three points.
         */
        mDeflector = new PlaneDeflector();
        /* set plane origin into the center of the screen */
        mDeflector.plane().origin.set(width / 2, height / 2, 0);
        mDeflector.plane().normal.set(0, -1, 0);
        /* the coefficient of restitution defines how hard particles bounce of the deflector */
        mDeflector.coefficientofrestitution(0.7f);
        mPhysics.add(mDeflector);

        /* create gravitiy */
        Gravity myGravity = new Gravity();
        myGravity.force().y = 50;
        mPhysics.add(myGravity);

        /* create drag */
        ViscousDrag myViscousDrag = new ViscousDrag();
        myViscousDrag.coefficient = 0.1f;
        mPhysics.add(myViscousDrag);
    }

    public void draw() {
        /* rotate deflector plane */
        if (mousePressed) {
            final float myAngle = 2 * PI * (float)mouseX / width - PI;
            mDeflector.plane().normal.set(sin(myAngle), -cos(myAngle), 0);
        }

        /* create a special particle */
        ShortLivedParticle myNewParticle = new ShortLivedParticle();
        myNewParticle.position().set(mouseX, mouseY);
        myNewParticle.velocity().set(0, random(100) + 50);
        /* this particle is removed after a specific interval */
        myNewParticle.setMaxAge(4);
        /* add particle manually to the particle system */
        mPhysics.add(myNewParticle);

        /* update physics */
        final float mDeltaTime = 1.0f / frameRate;
        mPhysics.step(mDeltaTime);

        /* draw all the particles in the particle system */
        background(255);
        for (int i = 0; i < mPhysics.particles().size(); i++) {
            Particle myParticle = mPhysics.particles(i);
            /* this special particle can tell you how much time it has to live.
             * we map this information to its transparency.
             */
            float myRatio = 1 - ((ShortLivedParticle)myParticle).ageRatio();
            stroke(0, 64 * myRatio);
            fill(0, 32 * myRatio);
            ellipse(myParticle.position().x, myParticle.position().y, 12, 12);
        }

        /* draw deflector */
        stroke(0, 127);
        line(mDeflector.plane().origin.x - mDeflector.plane().normal.y * -width,
             mDeflector.plane().origin.y + mDeflector.plane().normal.x * -width,
             mDeflector.plane().origin.x - mDeflector.plane().normal.y * width,
             mDeflector.plane().origin.y + mDeflector.plane().normal.x * width);

        stroke(255, 0, 0, 127);
        line(mDeflector.plane().origin.x,
             mDeflector.plane().origin.y,
             mDeflector.plane().origin.x + mDeflector.plane().normal.x * 20,
             mDeflector.plane().origin.y + mDeflector.plane().normal.y * 20);
    }

    public static void main(String[] args) {
        PApplet.main(new String[] {Lesson04_Deflectors.class.getName()});
    }
}
