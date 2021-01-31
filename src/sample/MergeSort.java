package sample;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MergeSort
{
    void merge(List<Timetable> arr, int l, int m, int r)
    {

        // Find sizes of two subarrays to be merged
        int lengthOfN1 = m - l + 1;
        int lengthOfN2 = r - m;

        /* Create temp arrays */
        Timetable L[] = new Timetable[lengthOfN1];
        Timetable R[] = new Timetable[lengthOfN2];

        /*Copy data to temp arrays*/
        for (int i = 0; i < lengthOfN1; ++i)
            L[i] = arr.get(l + i);
        for (int j = 0; j < lengthOfN2; ++j)
            R[j] = arr.get(m + 1 + j);

        for (Timetable i : L) {
            System.out.println(i.getDate());
        }

        for (Timetable i : R) {
            System.out.println(i.getDate());
        }

        /* Merge the temp arrays */

        // Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarry array
        int k = l;
        while (i < lengthOfN1 && j < lengthOfN2) {
            if (!parseDatetimetoTimespa(L[i].getDate(), L[i].getStartAtHour(),L[i].getStartAtMinute()).isBefore(parseDatetimetoTimespa(R[j].getDate(), R[j].getStartAtHour(),R[j].getStartAtMinute())) ) {
                arr.set(k, L[i]);
                i++;
            }
            else {
                arr.set(k, R[j]);
                j++;
            }
            k++;
        }

        /* Copy remaining elements of L[] if any */
        while (i < lengthOfN1) {
            arr.set(k, L[i]);
            i++;
            k++;
        }

        /* Copy remaining elements of R[] if any */
        while (j < lengthOfN2) {
            arr.set(k, R[j]);
            j++;
            k++;
        }

    }

    static LocalDateTime parseDatetimetoTimespa(LocalDate date, int start, int end)
    {
        LocalDateTime dateTime = date.atTime(start, end);
        return dateTime;
    }

    // Main function that sorts arr[l..r] using
    // merge()
    void sort(List<Timetable> arr, int l, int r)
    {
        if (l < r) {
            // Find the middle point
            int m = (l + r) / 2;

            // Sort first and second halves
            sort(arr, l, m);
            sort(arr, m + 1, r);

            // Merge the sorted halves
            merge(arr, l, m, r);
        }
    }

}
