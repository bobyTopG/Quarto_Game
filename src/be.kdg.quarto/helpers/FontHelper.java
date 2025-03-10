package be.kdg.quarto.helpers;

import javafx.scene.text.Font;

public class FontHelper {

    private static final String BOLD_FONT_FILE = "/fonts/berlinBold.ttf"; // Relative path
    private static final String FONT_FILE = "/fonts/berlin.ttf"; // Relative path

    private static Font smallFont;
    private static Font mediumFont;
    private static Font largeFont;
    private static Font extraLargeFont;
    private static Font descriptionFont;

    static {
        //loading the fonts
        Font loadedBoldFont = Font.loadFont(FontHelper.class.getResourceAsStream(BOLD_FONT_FILE) != null ? FontHelper.class.getResourceAsStream(BOLD_FONT_FILE): null, 12);
        Font loadedRegularFont = Font.loadFont(FontHelper.class.getResourceAsStream(FONT_FILE) != null ? FontHelper.class.getResourceAsStream(FONT_FILE) : null, 12);

        if (loadedBoldFont != null && loadedRegularFont != null) {
            //getting the font family from the fonts and changing the size based on our needs
            smallFont = Font.font(loadedBoldFont.getFamily(), 18);
            mediumFont = Font.font(loadedBoldFont.getFamily(), 24);
            largeFont = Font.font(loadedBoldFont.getFamily(), 48);
            extraLargeFont = Font.font(loadedBoldFont.getFamily(), 64);
            descriptionFont = Font.font(loadedRegularFont.getFamily(), 24);
        } else {
            System.err.println("Failed to load one or more fonts.");
            smallFont = Font.getDefault();
            mediumFont = Font.getDefault();
            largeFont = Font.getDefault();
            extraLargeFont = Font.getDefault();
            descriptionFont = Font.getDefault();
        }
    }

    public static Font getSmallFont() {
        return smallFont;
    }

    public static Font getMediumFont() {
        return mediumFont;
    }

    public static Font getLargeFont() {
        return largeFont;
    }

    public static Font getExtraLargeFont() {
        return extraLargeFont;
    }

    public static Font getDescriptionFont() {
        return descriptionFont;
    }
}