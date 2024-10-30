package com.ohlavrac.meu_dinheirinho_api.utils;
import java.time.LocalDate;

import com.ohlavrac.meu_dinheirinho_api.domain.enums.Periods;

public class VerifyPeriod {
    public LocalDate verifyPeriod(Periods period, LocalDate date) {
        if (period == Periods.PAST_WEEK) {
            return date.minusDays(7);
        } else if (period == Periods.PAST_MONTH) {
            return date.minusMonths(1);
        } else if (period == Periods.PAST_YEAR) {
            return date.minusYears(1);
        } else {
            return date.minusDays(7);
        }
    }
}
