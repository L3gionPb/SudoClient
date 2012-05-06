package com.sudoclient.widgets.preloaded.worldmap;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * User: deprecated
 * Date: 5/5/12
 * Time: 12:52 AM
 */

class ImageViewport {
    private final Component ctx;
    private final BufferedImage image;
    private final Point center;
    private double zoom;

    ImageViewport(final Component ctx, final BufferedImage image) {
        this(ctx, image, new Point(image.getWidth() / 2, image.getHeight() / 2));
    }

    ImageViewport(final Component ctx, final BufferedImage image, final Point center) {
        this.ctx = ctx;
        this.image = image;
        this.center = center;
        zoom = 1;
    }

    BufferedImage getViewportClip() {
        final Dimension dim = getFixedDimension();
        final Point origin = getFixedOrigin();
        return image.getSubimage(origin.x, origin.y, dim.width, dim.height);
    }

    void hop(final Point ctxPoint) {
        translate(-((ctx.getWidth() / 2) - ctxPoint.x), -((ctx.getHeight() / 2) - ctxPoint.y));
    }

    void translate(final int xOff, final int yOff) {
        center.translate((int) (xOff * zoom), (int) (yOff * zoom));
    }

    void zoom(int zoomChange) {
        zoom += (zoomChange / 2.0);

        if (zoom <= 0) {
            zoom = 0.5;
        }
    }

    private Dimension getFixedDimension() {
        final Dimension dim = getViewportDimension();

        if (dim.getHeight() > image.getHeight() || dim.getWidth() > image.getWidth()) {
            zoom -= 0.5;
            return getFixedDimension();
        }

        return dim;
    }

    private Point getFixedOrigin() {
        final Dimension dim = getViewportDimension();
        final Point origin = centerToOrigin();
        int x = origin.x, y = origin.y;

        if (origin.getX() <= 0) {
            x = 0;
        } else if (origin.getX() > image.getWidth() - dim.width) {
            x = image.getWidth() - dim.width;
        }

        if (origin.getY() <= 0) {
            y = 0;
        } else if (origin.getY() > image.getHeight() - dim.height) {
            y = image.getHeight() - dim.height;
        }

        final Point adjustedOrigin = new Point(x, y);
        adjustCenter(adjustedOrigin);
        return adjustedOrigin;
    }

    private Dimension getViewportDimension() {
        return new Dimension((int) (ctx.getWidth() * zoom), (int) (ctx.getHeight() * zoom));
    }

    private Point centerToOrigin() {
        return new Point(center.x - (int) ((ctx.getWidth() / 2) * zoom), center.y - (int) ((ctx.getHeight() / 2) * zoom));
    }

    private void adjustCenter(final Point origin) {
        center.setLocation(origin.x + (int) ((ctx.getWidth() / 2) * zoom), origin.y + (int) ((ctx.getHeight() / 2) * zoom));
    }
}
