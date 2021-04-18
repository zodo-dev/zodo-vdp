package dev.zodo.vdp.cobranca.pdf;

import org.apache.pdfbox.pdmodel.font.PDFont;

public enum TextAlign {
    LEFT {
        @Override
        public float calculate(String text, float x, PDFont pdFont, float size) {
            return x;
        }
    },
    CENTER() {
        @Override
        public float calculate(String text, float x, PDFont pdFont, float size) {
            float textWidth = calculateTextWidth(text, pdFont, size);
            return x - (textWidth / 2);
        }
    },
    RIGHT() {
        @Override
        public float calculate(String text, float x, PDFont pdFont, float size) {
            float textWidth = calculateTextWidth(text, pdFont, size);
            return x - textWidth;
        }
    };

    public abstract float calculate(String text, float x, PDFont pdFont, float size);

    public static float calculateTextWidth(String text, PDFont pdFont, float size) {
        if (text == null) {
            text = " ";
        }
        try {
            return (pdFont.getStringWidth(text) / 1000) * size;
        } catch (Exception e) {
            return ((pdFont.getAverageFontWidth() / 1000) * text.length()) * size;
        }
    }
}
