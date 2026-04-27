<?php

/**
    Design a sorting algorithm that does NOT rely on any conventional approach
    (e.g., bubble sort, merge sort, quicksort, insertion sort, selection sort, heap sort, etc.).
    Your algorithm must accept an unsorted array of integers and return a sorted array.

    $array = [9, 51, 4, 654, 1, 8748, 65, 9741, 231, 105, 368, 411 ];
 */

 function sort($array) {
    for ($i = 0; $i < count($array); $i++) {
        for ($j = 0; $j < count($array); $j++) {
            if ($array[$j] < $array[$i]) {
                $temp = $array[$i];
                $array[$j] = $array[$i];
                $array[$i] = $temp;
            }
        }
    }
    return $array;
 }

 function sortTwo($array) {

 }

