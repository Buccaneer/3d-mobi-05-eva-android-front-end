package be.evavzw.eva21daychallenge.activity.challenges;

/**
 * Created by Pieter-Jan on 3/11/2015.
 */
import java.util.Random;

import be.evavzw.eva21daychallenge.R;

public class Images
{

    private static final Random RANDOM = new Random();

    public static int getRandomCheeseDrawable() {
        switch (RANDOM.nextInt(5)) {
            default:
            case 0:
                return R.drawable.cheese_1;
            case 1:
                return R.drawable.cheese_2;
            case 2:
                return R.drawable.cheese_3;
            case 3:
                return R.drawable.cheese_4;
            case 4:
                return R.drawable.cheese_5;
        }
    }

}
