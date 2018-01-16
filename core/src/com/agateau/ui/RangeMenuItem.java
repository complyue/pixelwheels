/*
 * Copyright 2017 Aurélien Gâteau <mail@agateau.com>
 *
 * This file is part of Tiny Wheels.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.agateau.ui;

import com.agateau.ui.anchor.Anchor;
import com.agateau.ui.anchor.AnchorGroup;
import com.agateau.ui.anchor.EdgeRule;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Base class for all menu items with plus|minus buttons and a UI between those
 */
abstract class RangeMenuItem extends AnchorGroup implements MenuItem {
    private final Menu mMenu;
    private final Button mLeftButton;
    private final Button mRightButton;
    private final Rectangle mRect = new Rectangle();
    private final RangeMenuItemStyle mStyle;
    private Actor mMainActor;

    public static class RangeMenuItemStyle {
        Drawable frame;
        float framePadding;
        Drawable incIcon;
        Drawable decIcon;
    }

    public RangeMenuItem(Menu menu) {
        mMenu = menu;
        mStyle = menu.getSkin().get(RangeMenuItemStyle.class);

        mLeftButton = createButton(mStyle.decIcon, menu.getSkin());
        mLeftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                decreaseValue();
                Scene2dUtils.fireChangeEvent(RangeMenuItem.this);
            }
        });

        mRightButton = createButton(mStyle.incIcon, menu.getSkin());
        mRightButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                increaseValue();
                Scene2dUtils.fireChangeEvent(RangeMenuItem.this);
            }
        });

        setHeight(mLeftButton.getPrefHeight());
    }

    @Override
    public void layout() {
        if (mMainActor == null) {
            mMainActor = createMainActor(mMenu);
            float padding = mStyle.framePadding;
            float buttonSize = getHeight() - 2 * padding;
            addPositionRule(mLeftButton, Anchor.TOP_LEFT, this, Anchor.TOP_LEFT, padding, -padding);
            addPositionRule(mRightButton, Anchor.TOP_RIGHT, this, Anchor.TOP_RIGHT, -padding, -padding);
            mLeftButton.setSize(buttonSize, buttonSize);
            mRightButton.setSize(buttonSize, buttonSize);

            addPositionRule(mMainActor, Anchor.TOP_LEFT, mLeftButton, Anchor.TOP_RIGHT);
            addRule(new EdgeRule(mMainActor, EdgeRule.Edge.RIGHT, mRightButton, EdgeRule.Edge.LEFT));
            addRule(new EdgeRule(mMainActor, EdgeRule.Edge.BOTTOM, mRightButton, EdgeRule.Edge.BOTTOM));

            updateMainActor();
        }
        super.layout();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        mStyle.frame.draw(batch, getX(), getY(), getWidth(), getHeight());
        super.draw(batch, parentAlpha);
    }

    /**
     * Must create the actor to show between the left and right buttons
     */
    protected abstract Actor createMainActor(Menu menu);

    /**
     * Called when main actor must be updated because value changed
     */
    protected abstract void updateMainActor();

    protected abstract void decreaseValue();

    protected abstract void increaseValue();

    @Override
    public Actor getActor() {
        return this;
    }

    @Override
    public void trigger() {

    }

    @Override
    public boolean goUp() {
        return false;
    }

    @Override
    public boolean goDown() {
        return false;
    }

    @Override
    public void goLeft() {
        Scene2dUtils.simulateClick(mLeftButton);
    }

    @Override
    public void goRight() {
        Scene2dUtils.simulateClick(mRightButton);
    }

    @Override
    public Rectangle getFocusRectangle() {
        mRect.x = 0;
        mRect.y = 0;
        mRect.width = getWidth();
        mRect.height = getHeight();
        return mRect;
    }

    @Override
    public void setDefaultColumnWidth(float width) {
        setWidth(width);
    }

    private static Button createButton(Drawable drawable, Skin skin) {
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle(skin.get(ImageButton.ImageButtonStyle.class));
        style.imageUp = drawable;
        return new ImageButton(style);
    }
}
