package com.example.pildoraclicker;

public final class NumberFormatter {

    // Each tier: { threshold (as power of 10 exponent), suffix }
    // Stored as double thresholds in descending order for fast lookup.
    private static final Object[][] TIERS = {
        { 1e102, "Tt"  },  // Tretrigintillion
        { 1e99,  "Dt"  },  // Duotrigintillion
        { 1e96,  "Ut"  },  // Untrigintillion
        { 1e93,  "Tg"  },  // Trigintillion
        { 1e90,  "Nv"  },  // Novemvigintillion
        { 1e87,  "Ov"  },  // Octovigintillion
        { 1e84,  "Spv" },  // Septenvigintillion
        { 1e81,  "Sv"  },  // Sexvigintillion
        { 1e78,  "Qnv" },  // Quinvigintillion
        { 1e75,  "Qv"  },  // Quattuorvigintillion
        { 1e72,  "Tv"  },  // Trevigintillion
        { 1e69,  "Dv"  },  // Duovigintillion
        { 1e66,  "Uv"  },  // Unvigintillion
        { 1e63,  "Vg"  },  // Vigintillion
        { 1e60,  "Nd"  },  // Novemdecillion
        { 1e57,  "Od"  },  // Octodecillion
        { 1e54,  "Spd" },  // Septendecillion
        { 1e51,  "Sd"  },  // Sexdecillion
        { 1e48,  "Qnd" },  // Quindecillion
        { 1e45,  "Qd"  },  // Quattuordecillion
        { 1e42,  "Td"  },  // Tredecillion
        { 1e39,  "Dd"  },  // Duodecillion
        { 1e36,  "Ud"  },  // Undecillion
        { 1e33,  "Dc"  },  // Decillion
        { 1e30,  "No"  },  // Nonillion
        { 1e27,  "Oc"  },  // Octillion
        { 1e24,  "Sp"  },  // Septillion
        { 1e21,  "Sx"  },  // Sextillion
        { 1e18,  "Qi"  },  // Quintillion
        { 1e15,  "Qa"  },  // Quadrillion
        { 1e12,  "T"   },  // Trillion
        { 1e9,   "B"   },  // Billion
        { 1e6,   "M"   },  // Million
        { 1e3,   "K"   },  // Thousand
    };

    private NumberFormatter() {}

    public static String format(double value) {
        if (value < 0) return "-" + format(-value);
        for (Object[] tier : TIERS) {
            double threshold = (double) tier[0];
            String suffix = (String) tier[1];
            if (value >= threshold) {
                return String.format("%.2f%s", value / threshold, suffix);
            }
        }
        if (value == Math.floor(value) && !Double.isInfinite(value))
            return String.format("%.0f", value);
        return String.format("%.2f", value);
    }
}
