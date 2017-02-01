package xyz.yluo.ruisiapp.widget.htmlview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.BulletSpan;

/**
 * 列表Span
 */
public class CustomListSpan extends BulletSpan {
    private static final int tab = 40;
    private static final int mGapWidth = 40;
    private static final int BULLET_RADIUS = 6;

    private final boolean mWantColor;
    private int level;
    private final int mColor;
    private final String index;
    private int margin;

    private static Path circleBulletPath = null;
    private static Path rectBulletPath = null;

    public CustomListSpan(int level, int color, int pointIndex) {
        super(mGapWidth, color);
        this.level = level;
        if (pointIndex > 0) {
            this.index = pointIndex + "";
        } else {
            index = null;
        }
        mWantColor = true;
        mColor = color;
    }


    @Override
    public int getLeadingMargin(boolean first) {
        margin = (2 * BULLET_RADIUS + mGapWidth) * (level + 1) + tab;
        return margin;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout l) {
        if (((Spanned) text).getSpanStart(this) == start) {
            int oldcolor = 0;
            if (mWantColor) {
                oldcolor = p.getColor();
                p.setColor(mColor);
            }
            if (index != null) {
                c.drawText(index + '.', x - p.measureText(index) + margin - mGapWidth, baseline, p);
            } else {
                Paint.Style style = p.getStyle();
                if (level == 1) {
                    p.setStyle(Paint.Style.STROKE);
                } else {
                    p.setStyle(Paint.Style.FILL);
                }

                if (c.isHardwareAccelerated()) {
                    Path path;
                    if (level >= 2) {
                        if (rectBulletPath == null) {
                            rectBulletPath = new Path();
                            float w = 1.2f * BULLET_RADIUS;
                            rectBulletPath.addRect(-w, -w, w, w, Path.Direction.CW);
                        }
                        path = rectBulletPath;
                    } else {
                        if (circleBulletPath == null) {
                            circleBulletPath = new Path();
                            // Bullet is slightly better to avoid aliasing artifacts on mdpi devices.
                            circleBulletPath.addCircle(0.0f, 0.0f, 1.2f * BULLET_RADIUS, Path.Direction.CW);
                        }
                        path = circleBulletPath;
                    }

                    c.save();
                    c.translate(x + margin - mGapWidth, (top + bottom) / 2.0f);
                    c.drawPath(path, p);
                    c.restore();
                } else {
                    c.drawCircle(x + margin - mGapWidth, (top + bottom) / 2.0f, BULLET_RADIUS, p);
                }

                p.setStyle(style);
            }
            if (mWantColor) {
                p.setColor(oldcolor);
            }
        }
    }

}
