/*
 * Copyright 2019 Aurélien Gâteau <mail@agateau.com>
 *
 * This file is part of Pixel Wheels.
 *
 * Pixel Wheels is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.agateau.pixelwheels.gameinput;

import com.agateau.pixelwheels.GamePlay;
import com.badlogic.gdx.math.MathUtils;

public class DigitalSteering {

  private float rawDirection = 0;

  public float steer(boolean left, boolean right) {
    if (left && right) {
      // full steer in original direction,
      // assuming player intended to reverse at that direction
      rawDirection = Math.signum(rawDirection);
    } else if (left) {
      rawDirection += GamePlay.instance.steeringStep;
    } else if (right) {
      rawDirection -= GamePlay.instance.steeringStep;
    } else {
      rawDirection *= 0.4;
    }
    rawDirection = MathUtils.clamp(rawDirection, -1, 1);
    // use parabolic curve to smooth the ctrl
    return rawDirection * rawDirection * Math.signum(rawDirection);
  }

}
