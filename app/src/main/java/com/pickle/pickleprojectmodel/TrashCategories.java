package com.pickle.pickleprojectmodel;

/**
 * Created by Yanuar Wicaksana on 10/25/15.
 */
public enum TrashCategories {
    UNUSED, GENERAL, RECYCLED, GREEN;

    @Override
    public String toString() {

        switch (this) {

            case UNUSED:
                return "Unused Goods";
            case GENERAL:
                return "General Waste";
            case RECYCLED:
                return "Recycleable Waste";
            case GREEN:
                return "Green Waste";
            default:
                return "Unspecified";

        }
    }
}
