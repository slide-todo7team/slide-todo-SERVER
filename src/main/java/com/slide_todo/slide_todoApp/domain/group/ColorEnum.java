package com.slide_todo.slide_todoApp.domain.group;


public enum ColorEnum {
    COLOR_1("#B07100"),
    COLOR_2("#FF284F"),
    COLOR_3("#FF7020"),
    COLOR_4("#FFAD29"),
    COLOR_5("#FFEB3E"),
    COLOR_6("#FF769C"),
    COLOR_7("#AE3BF6"),
    COLOR_8("#BBC4DE"),
    COLOR_9("#6E7089"),
    COLOR_10("#6A3B0C"),
    COLOR_11("#27F15D"),
    COLOR_12("#17BF43"),
    COLOR_13("#0D6000"),
    COLOR_14("#044109"),
    COLOR_15("#870E02"),
    COLOR_16("#3BE0F6"),
    COLOR_17("#356EFF"),
    COLOR_18("#00639D"),
    COLOR_19("#231476"),
    COLOR_20("#000132");

    private final String hexCode;

    // 생성자
    ColorEnum(String hexCode) {
        this.hexCode = hexCode;
    }

}
