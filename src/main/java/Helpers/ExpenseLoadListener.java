package Helpers;


import java.util.Date;

/**
 * Created by Umut Seven on 18.1.2015, for Graviton.
 */
public interface ExpenseLoadListener {

    /**
     * Gider ekleme ekranindan ciktigizda calisan event.
     */
    public void onDismissed();

    /**
     * Tarih girme ekraninda tarih secilginde calisan event.
     *
     * @param date  Secilen tarih
     */
    public void onDateSelected(Date date);
}
