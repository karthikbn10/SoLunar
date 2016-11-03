package com.example.android.sunandmoon;

/**
 * Created by Karthik on 10/19/2016.
 */

public class MoonCalculation {

    private static final int    day_year[] = { -1, -1, 30, 58, 89, 119,
            150, 180, 211, 241, 272,
            303, 333 };

    // moon_phase_name - the English name for the different phases.
    // Change this if you need to localise the software.

    private static final String moon_phase_name[] = { "New moon",
            "Waxing crescent",
            "First quarter",
            "Waxing gibbous",
            "Full moon",
            "Waning gibbous",
            "Third quarter",
            "Waning crescent" };


    public int  moonPhase(int year, int month, int day) {

        int             phase;          // Moon phase
        int             cent;           // Century number (1979 = 20)
        int             epact;          // Age of the moon on Jan. 1
        int             diy;            // Day in the year
        int             golden;         // Moon's golden number

        if (month < 0 || month > 12) month = 0;     // Just in case
        diy = day + day_year[month];                // Day in the year
        if ((month > 2) && this.isLeapYearP(year))
            diy++;                                  // Leapyear fixup
        cent = (year / 100) + 1;                    // Century number
        golden = (year % 19) + 1;                   // Golden number
        epact = ((11 * golden) + 20                 // Golden number
                + (((8 * cent) + 5) / 25) - 5       // 400 year cycle
                - (((3 * cent) / 4) - 12)) % 30;    //Leap year correction
        if (epact <= 0)
            epact += 30;                        // Age range is 1 .. 30
        if ((epact == 25 && golden > 11) ||
                epact == 24)
            epact++;

        // Calculate the phase, using the magic numbers defined above.
        // Note that (phase and 7) is equivalent to (phase mod 8) and
        // is needed on two days per year (when the algorithm yields 8).

        phase = (((((diy + epact) * 6) + 11) % 177) / 22) & 7;

        return(phase);
    }

    // daysInMonth
    //
    // Returns the number of days in a month given the month and the year.

    int daysInMonth(int month,int year) {
        int result = 31;
        switch (month) {
            case 4:
            case 6:
            case 9:
            case 11:
                result = 30;
                break;
            case 2:
                result = ( this.isLeapYearP(year) ? 29 : 28 );
        }
        return result;
    }

    // isLeapYearP
    //
    // Return true if the year is a leapyear

    public  boolean isLeapYearP(int year) {
        return ((year % 4 == 0) &&
                ((year % 400 == 0) || (year % 100 != 0)));
    }

    // phaseName
    //
    // Return the name of a given phase

    String  phaseName(int phase) {
        return moon_phase_name[phase];
    }
}
